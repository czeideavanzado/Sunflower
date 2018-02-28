package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Product;
import org.hamster.sunflower_v2.domain.models.ProductDTO;
import org.hamster.sunflower_v2.domain.models.ProductRepository;
import org.hamster.sunflower_v2.domain.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ONB-CZEIDE on 02/27/2018
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private UserService userService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Override
    public Product sellProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
//        product.setPhoto(productDTO.getPhoto());
        User seller = getSeller(SecurityContextHolder.getContext().getAuthentication().getName());
        product.setSeller(seller);
        return productRepository.save(product);
    }

    @Override
    public void orderProduct(Long id) {
        String buyer = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.addProductToOrders(productRepository.findOne(id), buyer);
    }

    @Override
    public void updateProduct(Product product, Long id) {
        Product updateProduct = productRepository.findOne(id);
        updateProduct.setName(product.getName());
        updateProduct.setPrice(product.getPrice());
        updateProduct.setDescription(product.getDescription());
//        updateProduct.setPhoto(product.getPhoto());
        productRepository.save(updateProduct);
    }

    @Override
    public void removeProduct(Long id) {
        productRepository.delete(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product find(Long id) {
        return productRepository.findOne(id);
    }

    @Override
    public BigDecimal findPrice(Long id) {
        Product product = productRepository.findOne(id);
        return product.getPrice();
    }

    @Override
    public User findBySellerByUsername(String username) {
        return getSeller(username);
    }

    private User getSeller(String username) {
        return userService.findByUsername(username);
    }
}
