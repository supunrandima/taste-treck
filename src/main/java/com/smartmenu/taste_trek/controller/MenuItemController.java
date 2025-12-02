package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.dto.MenuItemDTO;
import com.smartmenu.taste_trek.entity.MenuItem;
import com.smartmenu.taste_trek.service.MenuItemService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DuplicateKeyException; // MongoDB duplicate key exception
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/admin/menu")
@CrossOrigin(origins = "*")
public class MenuItemController {

    @Autowired
    private  MenuItemService menuItemService;


    // --- CREATE (Add New Menu Item) ---
    // Endpoint: POST /api/v1/admin/menu
    // Note: Requires authentication, handled by Spring Security setup.
    @PostMapping(value = "/newMenuItem",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMenuItem(@Valid @RequestBody MenuItem menuItem) {
        try {
            // Set timestamps automatically on creation
            menuItem.setTimestamps();
            MenuItemDTO newItem = menuItemService.addItem(menuItem);
            return new ResponseEntity<>(newItem, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            // Catches error if @Indexed(unique=true) on 'name' constraint fails
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "A menu item with the name '" + menuItem.getName() + "' already exists.")
            );
        }
        catch (IllegalArgumentException e) {
            // Catches errors thrown by the service layer (e.g., uniqueness check during update)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // --- READ (Get All Menu Items) ---
    // Endpoint: GET /api/v1/admin/menu
    @GetMapping(value = "/allItems",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemService.getAllItems();
    }

    // --- READ (Get Item by ID) ---
    // Endpoint: GET /api/v1/admin/menu/{itemId}
    @GetMapping("/{itemId}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable String itemId) {
        if (!ObjectId.isValid(itemId)) {
            return ResponseEntity.badRequest().build();
        }
        return menuItemService.getItemById(new ObjectId(itemId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- READ (Search Items by Name) ---
    // Endpoint: GET /api/v1/admin/menu/search?name={searchName}
    @GetMapping("/search")
    public List<MenuItemDTO> searchMenuItems(@RequestParam String name) {
        return menuItemService.searchItemsByName(name);
    }

    // --- UPDATE (Update Existing Menu Item) ---
    // Endpoint: PUT /api/v1/admin/menu/{itemId}
    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateMenuItem(@PathVariable String itemId, @Valid @RequestBody MenuItem menuItem) {
        if (!ObjectId.isValid(itemId)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // The service handles uniqueness checks and updates the existing item
            Optional<MenuItemDTO> updatedItem = menuItemService.updateItem(new ObjectId(itemId), menuItem);

            return updatedItem
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            // Catches uniqueness conflict (e.g., trying to rename item to a name already used by another item)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    // --- DELETE (Delete Menu Item) ---
    // Endpoint: DELETE /api/v1/admin/menu/{itemId}
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String itemId) {
        if (!ObjectId.isValid(itemId)) {
            return ResponseEntity.badRequest().build();
        }

        boolean deleted = menuItemService.deleteItem(new ObjectId(itemId));
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
