package com.smartmenu.taste_treck.service;

import com.smartmenu.taste_treck.dto.AdminDTO;
import com.smartmenu.taste_treck.entity.Admin;
import com.smartmenu.taste_treck.repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    public List<AdminDTO> getAllAdmins() {
        return adminRepo.findAll().stream()
                .map(admin -> new AdminDTO(admin.getAdminId(), admin.getUsername(), admin.getPassword(), admin.getEmail(), admin.getPhone(), admin.getAddress(), admin.getRole(), admin.getStatus(), admin.getCreatedAt(), admin.getUpdatedAt()))
                .collect(Collectors.toList());
    }
}
