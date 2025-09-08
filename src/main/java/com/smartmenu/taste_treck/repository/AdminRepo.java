package com.smartmenu.taste_treck.repository;

import com.smartmenu.taste_treck.entity.Admin;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends MongoRepository<Admin, ObjectId> {

}
