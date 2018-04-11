package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }


    @Override
    public void cancelTransaction(Long id) {
        Transaction transaction = transactionRepository.findOne(id);
        transaction.getTransactionOrder().setTransactionStatus("CANCELLED");
        transactionRepository.save(transaction);
    }

    @Override
    public void cancelBuyerTransaction(Long userid){
        for (Transaction transaction : transactionRepository.findAll()) {
            if(transaction.getTransactionOrder().getBuyer().getId() == userid) {
                transaction.getTransactionOrder().setTransactionStatus("CANCELLED");
                transactionRepository.save(transaction);
            }
        }
    }
}
