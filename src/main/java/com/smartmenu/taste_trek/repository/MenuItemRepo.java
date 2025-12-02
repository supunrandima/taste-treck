package com.smartmenu.taste_trek.repository;


import com.smartmenu.taste_trek.entity.MenuItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepo extends MongoRepository<MenuItem, ObjectId> {

    // Custom query method for uniqueness check and retrieval
    Optional<MenuItem> findByName(String name);
    Optional<MenuItem> findByItemCode(String itemCode);
    // For checking existence before saving/updating
    boolean existsByName(String name);


    // For search functionality (e.g., used by the Admin Dashboard)
    List<MenuItem> findByNameContainingIgnoreCase(String name);
}
