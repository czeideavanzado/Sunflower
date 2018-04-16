package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
public interface ProductService {

    Product sellProduct(ProductDTO productDTO);
    Product sellMockProduct(ProductDTO productDTO);
    void updateProduct(Product product, Long id);
    void removeProduct(Long id);

    List<Product> findAll();
    List<Product> findByCategory(Category category);
    Product find(Long id);
    List<Product> findByKeyword(String keyword);
    BigDecimal findPrice(Long id);

    void archiveProduct(Long id);
    void unarchiveProduct(Long id);
    void archiveSellerProduct(Long userid);
    void setPending(Long id);
    void setOpen(Long id);

    User findByUserByUsername(String username);
}
