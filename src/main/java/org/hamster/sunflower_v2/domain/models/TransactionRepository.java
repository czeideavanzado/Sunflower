package org.hamster.sunflower_v2.domain.models;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ONB-CZEIDE on 03/21/2018
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
