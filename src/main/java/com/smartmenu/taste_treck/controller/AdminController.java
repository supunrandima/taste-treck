package com.smartmenu.taste_treck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartmenu.taste_treck.repository.AdminRepo;
import com.smartmenu.taste_treck.entity.Admin;

@RestController
public class AdminController {

    @Autowired
    AdminRepo adminRepo;

    @PostMapping("/registerAdmin")
    private void registerAdmin(@RequestBody Admin admin){

        adminRepo.save(admin);

}
}
