package bbk.challenge.atm.service;

import bbk.challenge.atm.data.Transaction;
import bbk.challenge.atm.data.TransactionType;
import bbk.challenge.atm.utils.InputInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ATMService {

    @Autowired private InputValidationService inputValidationService;

    @Autowired private TransactionsService transactionsService;

    private ConcurrentMap<String, Integer> denominatorToCount = new ConcurrentHashMap<>();
    private Integer noBills = 0;

    public void addCash(String userName, Map<String, Integer> denominatorToCount) throws InputInvalidException {

        inputValidationService.validateAddingCashInput(denominatorToCount, noBills);

        Integer addingNoBills = 0;
        Integer addingAmount = 0;

        for (Map.Entry<String, Integer> entry : denominatorToCount.entrySet()) {
            String addingKey = entry.getKey();
            Integer addingValue = entry.getValue();
            this.denominatorToCount.computeIfPresent(addingKey,
                    (existingKey, existingValue) -> existingValue + addingValue);
            addingNoBills += addingValue;
            addingAmount += Integer.valueOf(addingKey) * addingValue;
        }

        Transaction transaction = new Transaction(userName, addingNoBills, addingAmount, System.currentTimeMillis(),
                TransactionType.ADD_CASH);

        transactionsService.saveTransaction(transaction);

    }

    public Map<String, Integer> withdrawCash(Integer amount) {

        return null;
    }

    @PreDestroy
    public void clear() {
        denominatorToCount.clear();
        noBills = 0;
    }

}

