package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.dto.AdminResponseDTO;
import com.smartmenu.taste_trek.entity.Admin;
import com.smartmenu.taste_trek.entity.AuthenticationToken;
import com.smartmenu.taste_trek.service.AdminService;
import com.smartmenu.taste_trek.service.AuthenticationService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "http://localhost:5173/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Get all admins
    @GetMapping(value = "/allAdmins", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AdminResponseDTO> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    // ✅ Get admin by ID
    @GetMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable String adminId) {
        if (!ObjectId.isValid(adminId)) {
            return ResponseEntity.badRequest().build();
        }
        return adminService.getAdminById(new ObjectId(adminId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Register a new admin (with password encryption)
    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody Admin admin) {
        try {
            // Ensure the password encryption happens AFTER validation,
            // but BEFORE saving. The service layer handles this now.
            AdminResponseDTO newAdmin = adminService.addAdmin(admin);
            return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Catch the exception thrown by the service for duplicates
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            // Return 400 Bad Request or 409 Conflict
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // ✅ Update an existing admin
    @PutMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable String adminId, @Valid @RequestBody Admin admin) {
        if (!ObjectId.isValid(adminId)) {
            return ResponseEntity.badRequest().build();
        }
        return adminService.updateAdmin(new ObjectId(adminId), admin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete an admin
    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String adminId) {
        if (!ObjectId.isValid(adminId)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deleted = adminService.deleteAdmin(new ObjectId(adminId));
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ✅ Admin login (with token generation)
    @PostMapping("/adminLogin")
    public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> loginRequest) {
        String userId = loginRequest.get("userId");
//        String password = loginRequest.get("password");

        try {
            AuthenticationToken token = authenticationService.generateTokenForAdmin(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", userId);
            response.put("token", token.getTokenValue());
            response.put("tokenCreationDateTime", token.getTokenCreationDateTime());

            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // ✅ Validate admin using token
//    @PostMapping("/validate")
//    public ResponseEntity<?> validateAdmin(@RequestBody Map<String, String> request) {
//        String userId = request.get("userId");
//        String tokenValue = request.get("token");
//
//        boolean isValid = authenticationService.authenticateAdmin(userId, tokenValue);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("userId", userId);
//        response.put("isValid", isValid);
//
//        return ResponseEntity.ok(response);
//    }
}
