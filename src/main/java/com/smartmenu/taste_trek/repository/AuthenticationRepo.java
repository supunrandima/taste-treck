package com.smartmenu.taste_trek.repository;

import com.smartmenu.taste_trek.entity.AuthenticationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepo extends MongoRepository<AuthenticationToken,Long> {
    AuthenticationToken findByTokenValue(String authTokenValue);

}