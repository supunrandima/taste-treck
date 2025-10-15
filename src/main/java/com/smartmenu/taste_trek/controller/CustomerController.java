package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.dto.CustomerDTO;
import com.smartmenu.taste_trek.entity.AuthenticationToken;
import com.smartmenu.taste_trek.entity.Customer;
import com.smartmenu.taste_trek.service.AuthenticationService;
import com.smartmenu.taste_trek.service.CustomerService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/customer")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthenticationService authenticationService;

    //Get all customers
    @GetMapping(value = "/allCustomers", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    //Get customer by ID
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String customerId) {
        if (!ObjectId.isValid(customerId)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<CustomerDTO> customer = customerService.getCustomerById(new ObjectId(customerId));
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Customer login (with token)
    @PostMapping("/customerLogin")
    public ResponseEntity<?> loginCustomer(@RequestBody Map<String, String> payload) {
        String phone = payload.get("phone");

        try {
            AuthenticationToken token = authenticationService.generateTokenForCustomer(phone);

            return ResponseEntity.ok(Map.of(
                    "token", token.getTokenValue(),
                    "customerId", token.getCustomer().getCustomerId().toString(),
                    "phone", token.getCustomer().getPhone()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    //Register a new customer
    @PostMapping("/registerCustomer")
    public ResponseEntity<CustomerDTO> addCustomer(@Valid @RequestBody Customer customer) {
        CustomerDTO newCustomer = customerService.addCustomer(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    //Update an existing customer
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable String customerId, @Valid @RequestBody Customer customer) {
        if (!ObjectId.isValid(customerId)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<CustomerDTO> updatedCustomer = customerService.updateCustomer(new ObjectId(customerId), customer);
        return updatedCustomer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Delete a customer
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        if (!ObjectId.isValid(customerId)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deleted = customerService.deleteCustomer(new ObjectId(customerId));
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
