package bbk.challenge.atm.service;

import bbk.challenge.atm.model.Transaction;
import bbk.challenge.atm.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionsService {

    @Autowired private TransactionRepository transactionRepository;

    public void saveTransaction(Transaction transaction) {

        transactionRepository.save(transaction);
    }
}
