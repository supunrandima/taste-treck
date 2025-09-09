package com.smartmenu.taste_trek.service;

import com.smartmenu.taste_trek.dto.AdminResponseDTO;
import com.smartmenu.taste_trek.entity.Admin;
import com.smartmenu.taste_trek.repository.AdminRepo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminRepo adminRepo;

    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    // map Admin -> AdminResponseDTO (for responses)
    private AdminResponseDTO mapToResponseDTO(Admin admin) {
        return new AdminResponseDTO(
                admin.getAdminId(),
                admin.getUsername(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getAddress(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getUpdatedAt()
        );
    }

    // Get all admins
    public List<AdminResponseDTO> getAllAdmins() {
        return adminRepo.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get admin by ID
    public Optional<AdminResponseDTO> getAdminById(ObjectId adminId) {
        return adminRepo.findById(adminId).map(this::mapToResponseDTO);
    }

    // Add a new admin
    public AdminResponseDTO addAdmin(Admin admin) {
        Admin savedAdmin = adminRepo.save(admin);
        return mapToResponseDTO(savedAdmin);
    }

    // Update an existing admin
    public Optional<AdminResponseDTO> updateAdmin(ObjectId adminId, Admin admin) {
        if (adminRepo.existsById(adminId)) {
            admin.setAdminId(adminId);
            Admin updatedAdmin = adminRepo.save(admin);
            return Optional.of(mapToResponseDTO(updatedAdmin));
        }
        return Optional.empty();
    }

    // Delete an admin
    public boolean deleteAdmin(ObjectId adminId) {
        if (adminRepo.existsById(adminId)) {
            adminRepo.deleteById(adminId);
            return true;
        }
        return false;
    }
}

