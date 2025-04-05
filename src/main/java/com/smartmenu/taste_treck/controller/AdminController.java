package com.smartmenu.taste_treck.controller;

//import com.smartmenu.taste_treck.dto.AdminDTO;
import com.smartmenu.taste_treck.service.AdminService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartmenu.taste_treck.repository.AdminRepo;
import com.smartmenu.taste_treck.entity.Admin;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

//    @Autowired
//    AdminRepo adminRepo;
//
//    @PostMapping("/registerAdmin")
//    private void registerAdmin(@RequestBody Admin admin){
//        adminRepo.save(admin);
//    }

    @Autowired
    private AdminService adminService;

//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public List<AdminDTO>getAdmins(){
//
//        return adminService.getAllAdmins();
//
//    }
    // Get all admins
    @GetMapping("/allAdmins")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    //Get admin by Id
    @GetMapping("/{adminId}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String adminId) {
        try {
            ObjectId objectId = new ObjectId(adminId); // Convert String to ObjectId
            Optional<Admin> admin = adminService.getAdminById(objectId);
            return admin.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Handle invalid ObjectId
        }
    }

    // Add a new admin
    @PostMapping("/registerAdmin")
    public ResponseEntity<Admin> addAdmin(@Valid @RequestBody Admin admin) {
        Admin newAdmin = adminService.addAdmin(admin);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    //Update an Existing Admin
    @PutMapping("/{adminId}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable String adminId, @Valid @RequestBody Admin admin) {
        if (!ObjectId.isValid(adminId)) {
            return ResponseEntity.badRequest().build();
        }
        Admin updatedAdmin = adminService.updateAdmin(new ObjectId(adminId), admin);
        return updatedAdmin != null ? ResponseEntity.ok(updatedAdmin) : ResponseEntity.notFound().build();
    }

    //Delete an Admin
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String adminId) {
        if (!ObjectId.isValid(adminId)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deleted = adminService.deleteAdmin(new ObjectId(adminId));
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
