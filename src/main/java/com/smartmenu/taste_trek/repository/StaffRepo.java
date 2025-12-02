package com.smartmenu.taste_trek.repository;

import com.smartmenu.taste_trek.entity.Staff;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepo extends MongoRepository<Staff, ObjectId> {
    Optional<Staff> findByStaffId(String staffId);

    Optional<Staff> findByPhone(String phone);
}
