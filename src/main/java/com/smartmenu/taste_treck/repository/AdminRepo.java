package com.smartmenu.taste_treck.repository;

import com.smartmenu.taste_treck.entity.Admin;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepo extends MongoRepository<Admin, ObjectId> {

}
