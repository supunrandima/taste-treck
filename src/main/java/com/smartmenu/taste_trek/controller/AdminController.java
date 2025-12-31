package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.dto.AdminResponseDTO;
import com.smartmenu.taste_trek.entity.Admin;
import com.smartmenu.taste_trek.entity.AuthenticationToken;
import com.smartmenu.taste_trek.service.AdminService;
import com.smartmenu.taste_trek.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/allAdmins", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AdminResponseDTO> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable String adminId) {
        // Removed ObjectId validation since ID is now a String
        return adminService.getAdminById(adminId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody Admin admin) {
        try {
            AdminResponseDTO newAdmin = adminService.addAdmin(admin);
            return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable String adminId, @Valid @RequestBody Admin admin) {
        return adminService.updateAdmin(adminId, admin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String adminId) {
        boolean deleted = adminService.deleteAdmin(adminId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/adminLogin")
    public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> loginRequest) {

        String userId = loginRequest.get("userId");
        String password = loginRequest.get("password");

        if (userId == null || password == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Missing userId or password in request.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            Optional<AdminResponseDTO> authenticatedAdminOpt = adminService.authenticateAdmin(userId, password);

            if (authenticatedAdminOpt.isEmpty()) {
                throw new RuntimeException("Invalid User ID or password.");
            }

            AuthenticationToken token = authenticationService.generateTokenForAdmin(userId);
            AdminResponseDTO admin = authenticatedAdminOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("adminId", admin.adminId()); // Already String
            response.put("userId", userId);
            response.put("token", token.getTokenValue());
            response.put("tokenCreationDateTime", token.getTokenCreationDateTime());
            response.put("firstName", admin.firstName());

            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}