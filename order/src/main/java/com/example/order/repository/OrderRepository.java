package com.example.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.order.model.Order;
import com.example.order.model.Order.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByProductId(Long productId);
    List<Order> findByStatus(Order.OrderStatus status);
}
