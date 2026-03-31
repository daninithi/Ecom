package com.example.order.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.common.event.OrderEvent;
import com.example.common.event.OrderStatusEvent;
import com.example.common.event.ProductEvent;
import com.example.order.dto.OrderDTO;
import com.example.order.model.Order;
import com.example.order.model.Order.OrderStatus;
import com.example.order.repository.OrderRepository;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;
    
    private void verifyProductExists(Long productId) {
        try {
            String url = "http://product/api/products/" + productId;
            restTemplate.getForEntity(url, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Product with ID " + productId + " does not exist");
        } catch (Exception e) {
            throw new BadRequestException("Unable to verify product existence: " + e.getMessage());
        }
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order createOrder(OrderDTO orderDTO) {
        // Validate order data
        if (orderDTO == null) {
            throw new BadRequestException("Order data cannot be null");
        }
        
        if (orderDTO.getProductId() == null) {
            throw new BadRequestException("Product ID is required");
        }
        
        if (orderDTO.getQuantity() == null || orderDTO.getQuantity() <= 0) {
            throw new BadRequestException("Order quantity must be greater than zero");
        }
        
        // Verify product exists in Product service
        verifyProductExists(orderDTO.getProductId());
        
        // Convert DTO to Entity
        Order order = new Order();
        order.setProductId(orderDTO.getProductId());
        order.setQuantity(orderDTO.getQuantity());
        order.setStatus(OrderStatus.PENDING);
        
        Order savedOrder = orderRepository.save(order);

        OrderEvent orderEvent = new OrderEvent(
            "ORDER_CREATED",
            savedOrder.getId(),
            savedOrder.getProductId(),
            savedOrder.getQuantity()
        );

        kafkaTemplate.send("ORDER_CREATED", orderEvent);

        return savedOrder;
    }

    @KafkaListener(topics = "ORDER_STATUS", groupId = "order-group")
    public void updateOrderStatus(OrderStatusEvent event) {

        Order order = orderRepository.findById(event.getOrderId()).orElseThrow();

        order.setStatus(OrderStatus.valueOf(event.getStatus()));

        orderRepository.save(order);
    }
    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }
    
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        if (status == null) {
            throw new BadRequestException("Order status cannot be null");
        }
        
        Order order = getOrderById(id);
        order.setStatus(status);
        
        return orderRepository.save(order);
    }
    
    public List<Order> getOrdersByProductId(Long productId) {
        if (productId == null) {
            throw new BadRequestException("Product ID cannot be null");
        }
        return orderRepository.findByProductId(productId);
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        if (status == null) {
            throw new BadRequestException("Order status cannot be null");
        }
        return orderRepository.findByStatus(status);
    }
    
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed order");
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order is already cancelled");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
    
    public Order completeOrder(Long id) {
        Order order = getOrderById(id);
        
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException("Order is already completed");
        }
        
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot complete a cancelled order");
        }
        
        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }
    
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }
}
