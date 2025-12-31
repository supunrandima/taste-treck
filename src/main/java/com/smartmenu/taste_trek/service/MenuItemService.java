package com.smartmenu.taste_trek.service;

import com.smartmenu.taste_trek.dto.MenuItemDTO;
import com.smartmenu.taste_trek.entity.MenuItem;
import com.smartmenu.taste_trek.repository.MenuItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    private final MenuItemRepo menuItemRepo;

    @Autowired
    public MenuItemService(MenuItemRepo menuItemRepo) {
        this.menuItemRepo = menuItemRepo;
    }

    // --- Helper: Mapping Entity to DTO ---
    private MenuItemDTO mapToResponseDTO(MenuItem item) {
        return new MenuItemDTO(
                item.getItemId(), // No conversion needed
                item.getItemCode(),
                item.getName(),
                item.getCategory(),
                item.getDescription(),
                item.getPrice(),
                item.getImageUrl(),
                item.getStatus(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    // --- CRUD Operations ---

    // 1. Get all Menu Items
    public List<MenuItemDTO> getAllItems() {
        return menuItemRepo.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 2. Get Menu Item by ID (String)
    public Optional<MenuItemDTO> getItemById(String itemId) {
        return menuItemRepo.findById(itemId)
                .map(this::mapToResponseDTO);
    }
    public Optional<MenuItemDTO> getItemByItemCode(String itemCode) {
        return menuItemRepo.findByItemCode(itemCode)
                .map(this::mapToResponseDTO);
    }

    // 3. Search Menu Items by Name
    public List<MenuItemDTO> searchItemsByName(String name) {
        return menuItemRepo.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // 4. Add a new Menu Item
    public MenuItemDTO addItem(MenuItem item) {
        // Automatically set timestamps upon creation
        item.setTimestamps();

        // Uniqueness is enforced by @Indexed(unique=true) on 'name' in the Entity.
        MenuItem savedItem = menuItemRepo.save(item);
        return mapToResponseDTO(savedItem);
    }

    // 5. Update an existing Menu Item (String ID)
    public Optional<MenuItemDTO> updateItem(String itemId, MenuItem updatedItem) {
        // Check if item exists
        return menuItemRepo.findById(itemId).map(existingItem -> {

            // Check for name uniqueness conflict (if name is changed to an existing name of another item)
            Optional<MenuItem> itemWithSameName = menuItemRepo.findByName(updatedItem.getName());
            if (itemWithSameName.isPresent() && !itemWithSameName.get().getItemId().equals(itemId)) {
                // Throw an exception if another item already uses this name
                throw new IllegalArgumentException("Menu item name '" + updatedItem.getName() + "' already exists.");
            }

            // Apply updates: fields are updated directly from the incoming request body
            existingItem.setName(updatedItem.getName());
            existingItem.setCategory(updatedItem.getCategory());
            existingItem.setDescription(updatedItem.getDescription());
            existingItem.setPrice(updatedItem.getPrice());
            existingItem.setImageUrl(updatedItem.getImageUrl());
            existingItem.setStatus(updatedItem.getStatus());

            // Update timestamps
            existingItem.setUpdatedAt(LocalDateTime.now());

            MenuItem savedItem = menuItemRepo.save(existingItem);
            return mapToResponseDTO(savedItem);
        });
    }

    // 6. Delete a Menu Item (String ID)
    public boolean deleteItem(String itemId) {
        if (menuItemRepo.existsById(itemId)) {
            menuItemRepo.deleteById(itemId);
            return true;
        }
        return false;
    }
}