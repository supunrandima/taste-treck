package com.smartmenu.taste_trek.service;

import com.smartmenu.taste_trek.dto.CustomerDTO;
import com.smartmenu.taste_trek.entity.Customer;
import com.smartmenu.taste_trek.repository.CustomerRepo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    // map Customer -> CustomerDTO (for responses)
    private CustomerDTO mapToResponseDTO(Customer customer) {
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getBirthday(),
                customer.getStatus(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

    // Get all customers
    public List<CustomerDTO> getAllCustomers() {
        return customerRepo.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get customer by ID
    public Optional<CustomerDTO> getCustomerById(ObjectId customerId) {
        return customerRepo.findById(customerId).map(this::mapToResponseDTO);
    }

    // Add a new customer
    public CustomerDTO addCustomer(Customer customer) {
        Customer savedCustomer = customerRepo.save(customer);
        return mapToResponseDTO(savedCustomer);
    }

    // Update an existing customer
    public Optional<CustomerDTO> updateCustomer(ObjectId customerId, Customer customer) {
        if (customerRepo.existsById(customerId)) {
            customer.setCustomerId(customerId);
            Customer updatedCustomer = customerRepo.save(customer);
            return Optional.of(mapToResponseDTO(updatedCustomer));
        }
        return Optional.empty();
    }

    // Delete an customer
    public boolean deleteCustomer(ObjectId customerId) {
        if (customerRepo.existsById(customerId)) {
            customerRepo.deleteById(customerId);
            return true;
        }
        return false;
    }
}
