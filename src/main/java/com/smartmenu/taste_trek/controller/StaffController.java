package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.dto.StaffResponseDTO;
import com.smartmenu.taste_trek.entity.Staff;
import com.smartmenu.taste_trek.entity.AuthenticationToken;
import com.smartmenu.taste_trek.service.StaffService;
import com.smartmenu.taste_trek.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/allStaffs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffResponseDTO> getAllStaffs() {
        return staffService.getAllStaffs();
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffResponseDTO> getStaffById(@PathVariable String staffId) {
        // Removed ObjectId validation
        return staffService.getStaffById(staffId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registerStaff")
    public ResponseEntity<?> registerStaff(@Valid @RequestBody Staff staff) {
        try {
            StaffResponseDTO newStaff = staffService.addStaff(staff);
            return new ResponseEntity<>(newStaff, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<StaffResponseDTO> updateStaff(@PathVariable String staffId, @Valid @RequestBody Staff staff) {
        // Removed ObjectId validation
        return staffService.updateStaff(staffId, staff)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable String staffId) {
        // Removed ObjectId validation
        boolean deleted = staffService.deleteStaff(staffId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/staffLogin")
    public ResponseEntity<?> loginStaff(@RequestBody Map<String, String> loginRequest) {

        String staffId = loginRequest.get("staffId");
        String password = loginRequest.get("password");

        if (staffId == null || password == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Missing staffId or password in request.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            Optional<StaffResponseDTO> authenticatedStaffOpt = staffService.authenticateStaff(staffId, password);

            if (authenticatedStaffOpt.isEmpty()) {
                throw new RuntimeException("Invalid Staff ID or password.");
            }

            AuthenticationToken token = authenticationService.generateTokenForStaff(staffId);
            StaffResponseDTO staff = authenticatedStaffOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("staffId", staffId);
            response.put("token", token.getTokenValue());
            response.put("tokenCreationDateTime", token.getTokenCreationDateTime());
            response.put("firstName", staff.firstName());

            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}