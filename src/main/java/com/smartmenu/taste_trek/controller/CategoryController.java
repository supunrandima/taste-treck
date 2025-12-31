package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.dto.CategoryDTO;
import com.smartmenu.taste_trek.entity.Category;
import com.smartmenu.taste_trek.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/admin/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value ="/allCategories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping(value = "/newCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCategory(@Valid @RequestBody Category category) {
        try {
            CategoryDTO newCategory = categoryService.addCategory(category);
            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PutMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCategory(@PathVariable String categoryId, @Valid @RequestBody Category category) {
        // REMOVED: ObjectId.isValid(categoryId) check as ID is now arbitrary String

        try {

            Optional<CategoryDTO> updatedCategory = categoryService.updateCategory(categoryId, category);

            return updatedCategory
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCategory(@PathVariable String categoryId) {

        boolean deleted = categoryService.deleteCategory(categoryId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}