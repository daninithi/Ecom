package com.example.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.dto.ResponseMessage;
import com.example.common.enums.ResponseStatus;
import com.example.order.dto.OrderDTO;
import com.example.order.model.Order;
import com.example.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<ResponseMessage<List<Order>>> getOrders() {
        List<Order> orders = orderService.getAllOrders();
        ResponseMessage<List<Order>> response = new ResponseMessage<>(ResponseStatus.SUCCESS, orders);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<Order>> getOrderById(@PathVariable Long id) { 
        Order order = orderService.getOrderById(id);
        ResponseMessage<Order> response = new ResponseMessage<>(ResponseStatus.SUCCESS, order);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ResponseMessage<Order>> createOrder(@Valid @RequestBody OrderDTO order) {
        Order createdOrder = orderService.createOrder(order);           
        ResponseMessage<Order> response = new ResponseMessage<>(ResponseStatus.SUCCESS, createdOrder);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ResponseMessage<Order>> cancelOrder(@PathVariable Long id) {
        Order cancelledOrder = orderService.cancelOrder(id);
        ResponseMessage<Order> response = new ResponseMessage<>(ResponseStatus.SUCCESS, cancelledOrder);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<ResponseMessage<Order>> completeOrder(@PathVariable Long id) {
        Order completedOrder = orderService.completeOrder(id);
        ResponseMessage<Order> response = new ResponseMessage<>(ResponseStatus.SUCCESS, completedOrder);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<ResponseMessage<List<Order>>> getOrdersByProductId(@PathVariable Long productId) {
        List<Order> orders = orderService.getOrdersByProductId(productId);
        ResponseMessage<List<Order>> response = new ResponseMessage<>(ResponseStatus.SUCCESS, orders);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<String>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        ResponseMessage<String> response = new ResponseMessage<>(ResponseStatus.SUCCESS, "Order deleted successfully");
        return ResponseEntity.ok(response);
    }
}
