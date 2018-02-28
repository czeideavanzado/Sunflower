package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.domain.models.ProductDTO;
import org.hamster.sunflower_v2.domain.models.User;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
public interface ProductService {

    Product sellProduct(ProductDTO productDTO);
    void updateProduct(Product product, Long id);
    void removeProduct(Long id);
    List<Product> findAll();
    Product find(Long id);


    User findBySellerByUsername(String username);
}
