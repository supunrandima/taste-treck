package com.smartmenu.taste_trek.service;

import com.smartmenu.taste_trek.dto.StaffResponseDTO;
import com.smartmenu.taste_trek.entity.Staff;
import com.smartmenu.taste_trek.repository.StaffRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {

    private final StaffRepo staffRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public StaffService(StaffRepo staffRepo) {
        this.staffRepo = staffRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Helper: map Staff -> StaffResponseDTO
    private StaffResponseDTO mapToResponseDTO(Staff staff) {
        return new StaffResponseDTO(
                staff.getId(), // No conversion needed, it's already String
                staff.getFirstName(),
                staff.getLastName(),
                staff.getStaffId(),
                staff.getPhone(),
                staff.getAddress(),
                staff.getRole(),
                staff.getStatus(),
                staff.getCreatedAt(),
                staff.getUpdatedAt()
        );
    }

    // Get all staffs
    public List<StaffResponseDTO> getAllStaffs() {
        return staffRepo.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get staff by ID (String)
    public Optional<StaffResponseDTO> getStaffById(String id) {
        return staffRepo.findById(id)
                .map(this::mapToResponseDTO);
    }

    // Add a new staff (encrypt password)
    public StaffResponseDTO addStaff(Staff staff) {

        // --- LAYER 1: Existence Checks for Uniqueness ---
        if (staffRepo.findByStaffId(staff.getStaffId()).isPresent()) {
            throw new IllegalArgumentException("Staff ID '" + staff.getStaffId() + "' is already taken.");
        }

        if (staffRepo.findByPhone(staff.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone number '" + staff.getPhone() + "' is already registered.");
        }
        // --------------------------------------------------

        staff.setPassword(passwordEncoder.encode(staff.getPassword())); // Encrypt password
        Staff savedStaff = staffRepo.save(staff);
        return mapToResponseDTO(savedStaff);
    }

    // Update an existing staff (String ID)
    public Optional<StaffResponseDTO> updateStaff(String id, Staff staff) {
        if (staffRepo.existsById(id)) {
            staff.setId(id);

            // Only encode password if it's updated
            if (staff.getPassword() != null && !staff.getPassword().isEmpty()) {
                staff.setPassword(passwordEncoder.encode(staff.getPassword()));
            }

            Staff updatedStaff = staffRepo.save(staff);
            return Optional.of(mapToResponseDTO(updatedStaff));
        }
        return Optional.empty();
    }

    // Delete an staff (String ID)
    public boolean deleteStaff(String id) {
        if (staffRepo.existsById(id)) {
            staffRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // Authenticate staff (for login)
    public Optional<StaffResponseDTO> authenticateStaff(String staffId, String password) {
        Optional<Staff> staffOpt = staffRepo.findByStaffId(staffId);

        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();

            if (passwordEncoder.matches(password, staff.getPassword())) {
                return Optional.of(mapToResponseDTO(staff));
            }
        }
        return Optional.empty();
    }
}