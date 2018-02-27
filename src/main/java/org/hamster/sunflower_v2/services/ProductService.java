package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Product;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
public interface ProductService {
    public List<Product> findAll();
    public Product find(Long id);
}
