package com.myproject.e_commerce.service.CategoryService;

import com.myproject.e_commerce.dto.AddCategoryDTO;
import com.myproject.e_commerce.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
    void addCategory(AddCategoryDTO addCategoryDTO);
    void deleteCategory(Integer id);
    void updateCategory(List<Integer> categoryId,Integer ProductId);
}
