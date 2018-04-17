package org.hamster.sunflower_v2.domain.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContaining(String keyword);

    Page<Product> findAllByNameContaining(String keyword, Pageable pageable);
    Page<Product> findByCategoryAndArchivedIsFalse(Category category, Pageable pageable);
}
