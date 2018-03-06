package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.domain.models.ProductDTO;
import org.hamster.sunflower_v2.domain.models.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
public interface ProductService {

    Product sellProduct(ProductDTO productDTO);
    Product sellMockProduct(ProductDTO productDTO);
    void orderProduct(Long id);
    void updateProduct(Product product, Long id);
    void removeProduct(Long id);
    List<Product> findAll();
    Product find(Long id);
    BigDecimal findPrice(Long id);


    User findByUserByUsername(String username);
}
