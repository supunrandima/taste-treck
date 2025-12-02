package com.smartmenu.taste_trek.service;

import com.smartmenu.taste_trek.dto.CategoryDTO;
import com.smartmenu.taste_trek.entity.Category;
import com.smartmenu.taste_trek.repository.CategoryRepo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    private CategoryDTO mapToDTO(Category category) {
        // Assuming CategoryDTO is available
        return new CategoryDTO(
                category.getCategoryId(),
                category.getName(),
                category.getCategoryCode(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // CREATE
    public CategoryDTO addCategory(Category category) {
        // Uniqueness checks for ADD
        if (categoryRepo.findByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category name '" + category.getName() + "' already exists.");
        }
        if (categoryRepo.findByCategoryCode(category.getCategoryCode()).isPresent()) {
            throw new IllegalArgumentException("Category Code '" + category.getCategoryCode() + "' is already in use.");
        }

        category.setTimestamps();
        Category savedCategory = categoryRepo.save(category);
        return mapToDTO(savedCategory);
    }

    // NEW/MODIFIED: Update Category
    public Optional<CategoryDTO> updateCategory(ObjectId categoryId, Category updatedCategory) {
        return categoryRepo.findById(categoryId).map(existingCategory -> {

            // Check for name uniqueness conflict (if name is changed to an existing name of another category)
            Optional<Category> nameConflict = categoryRepo.findByName(updatedCategory.getName());
            if (nameConflict.isPresent() && !nameConflict.get().getCategoryId().equals(categoryId)) {
                throw new IllegalArgumentException("Category name '" + updatedCategory.getName() + "' already exists.");
            }

            // Check for code uniqueness conflict
            Optional<Category> codeConflict = categoryRepo.findByCategoryCode(updatedCategory.getCategoryCode());
            if (codeConflict.isPresent() && !codeConflict.get().getCategoryId().equals(categoryId)) {
                throw new IllegalArgumentException("Category Code '" + updatedCategory.getCategoryCode() + "' is already in use.");
            }

            // Apply updates
            existingCategory.setName(updatedCategory.getName());
            existingCategory.setCategoryCode(updatedCategory.getCategoryCode());
            existingCategory.setUpdatedAt(LocalDateTime.now());

            Category savedCategory = categoryRepo.save(existingCategory);
            return mapToDTO(savedCategory);
        });
    }

    // DELETE (Logic already exists, retained for context)
    public boolean deleteCategory(ObjectId categoryId) {
        if (categoryRepo.existsById(categoryId)) {
            // WARNING: Deletion logic should ideally check if any MenuItems reference this category before deleting.
            categoryRepo.deleteById(categoryId);
            return true;
        }
        return false;
    }
}