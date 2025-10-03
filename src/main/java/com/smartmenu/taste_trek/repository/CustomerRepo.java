package com.smartmenu.taste_trek.repository;

import com.smartmenu.taste_trek.entity.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends MongoRepository<Customer, ObjectId> {
        Optional<Customer> findByPhone(String phone);
        Optional<Customer> findByEmail(String email);
        boolean existsByPhone(String phone);
        boolean existsByEmail(String email);

}

