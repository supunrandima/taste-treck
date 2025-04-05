package com.smartmenu.taste_treck.service;

//import com.smartmenu.taste_treck.dto.AdminDTO;
import com.smartmenu.taste_treck.entity.Admin;
import com.smartmenu.taste_treck.repository.AdminRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

//    public List<AdminDTO> getAllAdmins() {
//        return adminRepo.findAll().stream()
//                .map(admin -> new AdminDTO(admin.getAdminId(), admin.getUsername(), admin.getPassword(), admin.getEmail(), admin.getPhone(), admin.getAddress(), admin.getRole(), admin.getStatus(), admin.getCreatedAt(), admin.getUpdatedAt()))
//                .collect(Collectors.toList());
//    }
// Get all admins
public List<Admin> getAllAdmins() {
    return adminRepo.findAll();
}

    // Get admin by ID
    public Optional<Admin> getAdminById(ObjectId adminId) {
        return adminRepo.findById(adminId);
    }

    // Add a new admin
    public Admin addAdmin(Admin admin) {
        return adminRepo.save(admin);
    }

    // Update an existing admin
    public Admin updateAdmin(ObjectId adminId, Admin admin) {
        if(adminRepo.existsById(adminId)) {
            admin.setAdminId(adminId);
            return adminRepo.save(admin);
        } else {
            return null;  // Admin not found
        }
    }

    // Delete an admin
    public boolean deleteAdmin(ObjectId adminId) {
        if(adminRepo.existsById(adminId)) {
            adminRepo.deleteById(adminId);
            return true;
        } else {
            return false;  // Admin not found
        }
    }
}
