package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Order;
import org.hamster.sunflower_v2.domain.models.Transaction;
import org.hamster.sunflower_v2.domain.models.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ONB-CZEIDE on 03/21/2018
 */
@Service(value = "transactionService")
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction createTransaction(Order order) {
        Transaction transaction = new Transaction();

        transaction.setTransaction_order(order);

        return transactionRepository.save(transaction);
    }
}
