package com.myproject.e_commerce.service.CategoryService;

import com.myproject.e_commerce.dto.AddCategoryDTO;
import com.myproject.e_commerce.dto.CategoryDTO;
import com.myproject.e_commerce.entity.Category;
import com.myproject.e_commerce.entity.Product;
import com.myproject.e_commerce.repository.CategoryRepository;
import com.myproject.e_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    public  CategoryServiceImpl(CategoryRepository categoryRepository,ProductRepository productRepository) {
        this.productRepository=productRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(cat -> CategoryDTO.builder()
                .name(cat.getName())
                .id(cat.getId())
                .build()).toList();
    }
    @Transactional
    @Override
    public void addCategory(AddCategoryDTO addCategoryDTO) {
        Category category = Category.builder()
                .name(addCategoryDTO.getName())
                .thumbnail(addCategoryDTO.getThumbnailUrl())
                .description(addCategoryDTO.getDescription())
                .build();
        categoryRepository.save(category);
    }
    @Transactional
    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElse(null);
        List<Product> products = category.getProducts();
        for (Product product : products) {
            product.getCategories().remove(category);
        }
        categoryRepository.deleteById(id);
    }
    @Transactional
    @Override
    public void updateCategory(List<Integer> categoryId, Integer ProductId) {
        Product product = productRepository.findById(ProductId).orElse(null);
        List<Category> categories = categoryRepository.findAllById(categoryId);
        product.getCategories().clear();
        product.setCategories(categories);
        productRepository.save(product);
    }
}
