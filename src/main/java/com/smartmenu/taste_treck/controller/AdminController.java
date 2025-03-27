package com.smartmenu.taste_treck.controller;

import com.smartmenu.taste_treck.dto.AdminDTO;
import com.smartmenu.taste_treck.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.smartmenu.taste_treck.repository.AdminRepo;
import com.smartmenu.taste_treck.entity.Admin;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    AdminRepo adminRepo;

    @PostMapping("/registerAdmin")
    private void registerAdmin(@RequestBody Admin admin){
        adminRepo.save(admin);
    }

    @Autowired
    private AdminService adminService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<AdminDTO>getAdmins(){

        return adminService.getAllAdmins();

    }
}
