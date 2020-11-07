package bbk.challenge.atm.service;

import bbk.challenge.atm.data.Transaction;
import bbk.challenge.atm.data.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionsService {

    @Autowired private TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {

        return transactionRepository.save(transaction);
    }
}
