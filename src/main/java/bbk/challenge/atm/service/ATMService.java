package bbk.challenge.atm.service;

import bbk.challenge.atm.auth.AuthenticationService;
import bbk.challenge.atm.model.Transaction;
import bbk.challenge.atm.model.TransactionType;
import bbk.challenge.atm.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ATMService {

    @Autowired
    private InputValidationService inputValidationService;

    @Autowired
    private TransactionsService transactionsService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private KnapsackService knapsackService;

    private ConcurrentMap<String, Integer> denominatorToCount = new ConcurrentHashMap<>();
    private Integer noBills = 0;
    private Long maxAmount = 0L;

    public void addCash(String authentication, Map<String, Integer> denominatorToCount)
            throws InputInvalidException, AuthenticationException, AuthorizationException {

        if (!authenticationService.authenticatedUser(authentication)) {
            throw new AuthenticationException(String.format("%s authentication is not valid", authentication));
        }

        String userName = authenticationService.getUserName(authentication);

        if (!authenticationService.hasWriteAccess(authentication)) {
            throw new AuthorizationException(String.format("User: %s is not allowed to add money", userName));
        }

        inputValidationService.validateAddingCashInput(denominatorToCount, noBills);

        Integer addingNoBills = 0;
        Long addingAmount = 0L;

        for (Map.Entry<String, Integer> entry : denominatorToCount.entrySet()) {
            String addingKey = entry.getKey();
            Integer addingValue = entry.getValue();
            this.denominatorToCount.merge(addingKey, addingValue, Integer::sum);
            addingNoBills += addingValue;
            addingAmount += Integer.valueOf(addingKey) * addingValue;
        }

        this.noBills += addingNoBills;
        this.maxAmount += addingAmount;

        Transaction transaction = Transaction.builder()
                .userName(userName)
                .noBills(addingNoBills)
                .amount(addingAmount)
                .timestamp(System.currentTimeMillis())
                .transactionType(TransactionType.ADD_CASH)
                .build();

        transactionsService.saveTransaction(transaction);
    }

    public Map<String, Integer> withdrawCash(String authentication, Long amount)
            throws AuthenticationException, AuthorizationException, MaxAmountExceededException, PerformException {

        if (!authenticationService.authenticatedUser(authentication)) {
            throw new AuthenticationException(String.format("%s authentication is not valid", authentication));
        }

        String userName = authenticationService.getUserName(authentication);

        if (!authenticationService.hasReadAccess(authentication)) {
            throw new AuthorizationException(String.format("User: %s is not allowed to retrieve money", userName));
        }

        if (amount > maxAmount) {
            throw new MaxAmountExceededException(
                    String.format("Amount exceeded! Requested: %d! Max allowed: %d", amount, maxAmount));
        }

        Map<String, Integer> denominatorToCashOfWithdraw = knapsackService.getDenominatorToCashOfWithdraw(
                denominatorToCount, amount);

        if (denominatorToCashOfWithdraw == null || denominatorToCashOfWithdraw.isEmpty()) {
            throw new PerformException(String.format("Could not satisfy the withdraw! The bills doesn't allow to extract exactly %d", amount));
        }

        int noBillsToWithdraw = denominatorToCashOfWithdraw.values().stream().mapToInt(Integer::intValue).sum();

        this.noBills -= noBillsToWithdraw;
        this.maxAmount -= amount;

        return denominatorToCashOfWithdraw;
    }

    @PreDestroy
    public void clear() {

        denominatorToCount.clear();
        noBills = 0;
        maxAmount = 0L;
    }

}

