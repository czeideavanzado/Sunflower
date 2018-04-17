package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Category;
import org.hamster.sunflower_v2.domain.models.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 04/15/2018
 */
@Service(value = "categoryService")
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long category_id) {
        return categoryRepository.findOne(category_id);
    }

    @Override
    public Category findByName(String category) {
        return categoryRepository.findByName(category);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}
