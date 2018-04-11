package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Order;
import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.domain.models.Transaction;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 03/21/2018
 */
public interface TransactionService {

    Transaction createTransaction(Order order);
    List<Transaction> findAll();
    void cancelTransaction(Long id);
    void cancelBuyerTransaction(Long userid);
}
