package com.smartmenu.taste_trek.repository;

import com.smartmenu.taste_trek.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// ID is String
@Repository
public interface OrderRepo extends MongoRepository<Order, String> {
    // Custom query to find orders by status (useful for Kitchen/Admin Dashboard)
    List<Order> findByStatusOrderByCreatedAtAsc(String status);

    // Custom query to find orders by table number
    Optional<Order> findByOrderId(String orderId);
    List<Order> findByCustomerId(String customerId);
    List<Order> findByTableNumber(String tableNumber);

    List<Order> findByStatusIn(List<String> statuses);
}
