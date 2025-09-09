package com.smartmenu.taste_trek.repository;

import com.smartmenu.taste_trek.entity.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends MongoRepository<Customer, ObjectId> {

}
