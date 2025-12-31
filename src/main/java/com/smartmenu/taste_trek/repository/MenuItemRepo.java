package com.smartmenu.taste_trek.repository;

import com.smartmenu.taste_trek.entity.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepo extends MongoRepository<MenuItem, String> {
    Optional<MenuItem> findByName(String name);
    Optional<MenuItem> findByItemCode(String itemCode);
    boolean existsByName(String name);
    List<MenuItem> findByNameContainingIgnoreCase(String name);

    // --- NEW: For Recommendations ---
    List<MenuItem> findByCategory(String category);
}