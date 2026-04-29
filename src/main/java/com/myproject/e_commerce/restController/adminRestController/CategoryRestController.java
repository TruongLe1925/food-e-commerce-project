package com.myproject.e_commerce.restController.adminRestController;

import com.myproject.e_commerce.dto.AddCategoryDTO;
import com.myproject.e_commerce.dto.CategoryDTO;
import com.myproject.e_commerce.service.CategoryService.CategoryService;
import com.myproject.e_commerce.service.FileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class CategoryRestController {
    private final CategoryService categoryService;
    private final FileService fileService;

    public CategoryRestController(CategoryService categoryService, FileService fileService) {
        this.categoryService = categoryService;
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> addCategory(@Valid @RequestBody AddCategoryDTO addCategoryDTO) {
        categoryService.addCategory(addCategoryDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-product")
    public ResponseEntity<Void> updateCategoryProduct(@RequestParam List<Integer> categoryId,
                                                      @RequestParam Integer productId) {
        categoryService.updateCategory(categoryId, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<String> uploadCategoryImage(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.saveCat(file);
        return ResponseEntity.ok(fileName);
    }
}
