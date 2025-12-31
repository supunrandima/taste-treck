package com.smartmenu.taste_trek.repository;

import com.smartmenu.taste_trek.entity.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends MongoRepository<Category, String> {
    Optional<Category> findByName(String name);
    Optional<Category> findByCategoryCode(String categoryCode);
}


