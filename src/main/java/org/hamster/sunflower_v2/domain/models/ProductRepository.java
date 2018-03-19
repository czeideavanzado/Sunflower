package org.hamster.sunflower_v2.domain.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

//    @Query("Select p from products p where p.name like %:keyword%")
    List<Product> findByNameContaining(String keyword);
}
