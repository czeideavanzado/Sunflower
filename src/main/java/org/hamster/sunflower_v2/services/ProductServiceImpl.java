package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public Product sellProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setPhoto(productDTO.getPhoto());

        User seller = getSeller(SecurityContextHolder.getContext().getAuthentication().getName());
        product.setSeller(seller);

        Category category = categoryService.findById(productDTO.getCategory_id());
        product.setCategory(category);
        return productRepository.save(product);
    }

    @Override
    public Product sellMockProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
//        product.setPhoto(productDTO.getPhoto());
        User seller = getSellerById(productDTO.getSeller_id());
        product.setSeller(seller);
        return productRepository.save(product);
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
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product find(Long id) {
        return productRepository.findOne(id);
    }

    @Override
    public List<Product> findByKeyword(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    @Override
    public BigDecimal findPrice(Long id) {
        Product product = productRepository.findOne(id);
        return product.getPrice();
    }

    @Override
    public User findByUserByUsername(String username) {
        return getSeller(username);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> findAllByOrderByPriceAsc(Pageable pageable) {
        return productRepository.findAllByOrderByPriceAsc(pageable);
    }

    @Override
    public Page<Product> findAllByOrderByPriceDesc(Pageable pageable) {
        return productRepository.findAllByOrderByPriceDesc(pageable);
    }

    @Override
    public Page<Product> findAllByOrderByCreatedDateDesc(Pageable pageable) {
        return productRepository.findAllByOrderByCreatedDateDesc(pageable);
    }

    private User getSeller(String username) {
        return userService.findByUsername(username);
    }

    private User getSellerById(Long id) {
        return userService.findById(id);
    }

    @Override
    public void archiveProduct(Long id){
        Product product = productRepository.findOne(id);
        product.setArchive(true);
        productRepository.save(product);
    }

    @Override
    public void archiveSellerProduct(Long userid){
        for (Product product : productRepository.findAll()) {
            if(product.getSeller().getId() == userid) {
                product.setArchive(true);
                productRepository.save(product);
            }
        }
    }

    @Override
    public void unarchiveProduct(Long id){
        Product product = productRepository.findOne(id);
        product.setArchive(false);
        productRepository.save(product);
    }

    @Override
    public void setPending(Long id){
        Product product = productRepository.findOne(id);
        product.setStatus("PENDING");
        productRepository.save(product);
    }

    @Override
    public void setOpen(Long id){
        Product product = productRepository.findOne(id);
        product.setStatus("OPEN");
        productRepository.save(product);
    }
}
