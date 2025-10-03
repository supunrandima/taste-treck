package com.smartmenu.taste_trek.controller;

//import com.smartmenu.taste_trek.dto.AdminDTO;
import com.smartmenu.taste_trek.dto.AdminResponseDTO;
import com.smartmenu.taste_trek.service.AdminService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartmenu.taste_trek.entity.Admin;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "http://localhost:5173/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Get all admins
    @GetMapping(value = "/allAdmins", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AdminResponseDTO> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    // Get admin by ID
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable String adminId) {
        if (!ObjectId.isValid(adminId)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<AdminResponseDTO> admin = adminService.getAdminById(new ObjectId(adminId));
        return admin.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a new admin
    @PostMapping("/registerAdmin")
    public ResponseEntity<AdminResponseDTO> addAdmin(@Valid @RequestBody Admin admin) {
        AdminResponseDTO newAdmin = adminService.addAdmin(admin);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    // Update an existing admin
    @PutMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable String adminId, @Valid @RequestBody Admin admin) {
        if (!ObjectId.isValid(adminId)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<AdminResponseDTO> updatedAdmin = adminService.updateAdmin(new ObjectId(adminId), admin);
        return updatedAdmin.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete an admin
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String adminId) {
        if (!ObjectId.isValid(adminId)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deleted = adminService.deleteAdmin(new ObjectId(adminId));
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

