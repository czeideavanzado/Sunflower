package org.hamster.sunflower_v2.services;

import org.hamster.sunflower_v2.domain.models.Category;

import java.util.List;

/**
 * Created by ONB-CZEIDE on 04/15/2018
 */
public interface CategoryService {

    List<Category> findAll();
    Category findById(Long category_id);
    Category findByName(String category);
    Category save(Category category);
}
