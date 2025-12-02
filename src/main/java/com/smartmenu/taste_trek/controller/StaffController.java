package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.dto.StaffResponseDTO;
import com.smartmenu.taste_trek.entity.Staff;
import com.smartmenu.taste_trek.entity.AuthenticationToken;
import com.smartmenu.taste_trek.service.StaffService;
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
@RequestMapping("api/v1/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Get all staffs
    @GetMapping(value = "/allStaffs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffResponseDTO> getAllStaffs() {
        return staffService.getAllStaffs();
    }

    // ✅ Get staff by ID
    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponseDTO> getStaffById(@PathVariable String staffId) {
        if (!ObjectId.isValid(staffId)) {
            return ResponseEntity.badRequest().build();
        }
        return staffService.getStaffById(new ObjectId(staffId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Register a new staff (with password encryption)
    @PostMapping("/registerStaff")
    public ResponseEntity<?> registerStaff(@Valid @RequestBody Staff staff) {
        try {
            // Ensure the password encryption happens AFTER validation,
            // but BEFORE saving. The service layer handles this now.
            StaffResponseDTO newStaff = staffService.addStaff(staff);
            return new ResponseEntity<>(newStaff, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Catch the exception thrown by the service for duplicates
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            // Return 400 Bad Request or 409 Conflict
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    // ✅ Update an existing staff
    @PutMapping("/{staffId}")
    public ResponseEntity<StaffResponseDTO> updateStaff(@PathVariable String staffId, @Valid @RequestBody Staff staff) {
        if (!ObjectId.isValid(staffId)) {
            return ResponseEntity.badRequest().build();
        }
        return staffService.updateStaff(new ObjectId(staffId), staff)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete an staff
    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable String staffId) {
        if (!ObjectId.isValid(staffId)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deleted = staffService.deleteStaff(new ObjectId(staffId));
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ✅ Staff login (with token generation)
    @PostMapping("/staffLogin")
    public ResponseEntity<?> loginStaff(@RequestBody Map<String, String> loginRequest) {

        // Ensure keys match the frontend payload (staffId, password)
        String staffId = loginRequest.get("staffId");
        String password = loginRequest.get("password");

        if (staffId == null || password == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Missing staffId or password in request.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            // 1. Authenticate the staff using StaffService (checks password)
            Optional<StaffResponseDTO> authenticatedStaffOpt = staffService.authenticateStaff(staffId, password);

            if (authenticatedStaffOpt.isEmpty()) {
                throw new RuntimeException("Invalid Staff ID or password.");
            }

            // 2. If successful, generate the authentication token
            AuthenticationToken token = authenticationService.generateTokenForStaff(staffId);
            StaffResponseDTO staff = authenticatedStaffOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("staffId", staffId);
            response.put("token", token.getTokenValue());
            response.put("tokenCreationDateTime", token.getTokenCreationDateTime());
            response.put("firstName", staff.firstName()); // Add first name for frontend welcome message

            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            // Return UNAUTHORIZED (401) for login failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // ✅ Validate staff using token
//    @PostMapping("/validate")
//    public ResponseEntity<?> validateStaff(@RequestBody Map<String, String> request) {
//        String staffId = request.get("staffId");
//        String tokenValue = request.get("token");
//
//        boolean isValid = authenticationService.authenticateStaff(staffId, tokenValue);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("staffId", staffId);
//        response.put("isValid", isValid);
//
//        return ResponseEntity.ok(response);
//    }
}
