package com.example.inventory.service;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.inventory.model.Inventory;
import com.example.inventory.repository.InventoryRepository;
import com.example.common.event.OrderEvent;
import com.example.common.event.OrderStatusEvent;
import com.example.common.event.ProductEvent;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.inventory.dto.InventoryDTO;
import com.example.inventory.dto.RestockDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final RestTemplate restTemplate;

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;
 
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }
    
    public Inventory createInventory(InventoryDTO inventory) {
        // Validate inventory data
        if (inventory == null) {
            throw new BadRequestException("Inventory data cannot be null");
        }
    
        if (inventory.getProductId() == null) {
            throw new BadRequestException("Product ID is required");
        }
        
        if (inventory.getStock() == null || inventory.getStock() < 0) {
            throw new BadRequestException("Stock quantity cannot be negative");
        }
        
        // Check if inventory for this product already exists
        if (inventoryRepository.existsByProductId(inventory.getProductId())) {
            throw new BadRequestException("Inventory for product ID " + inventory.getProductId() + " already exists");
        }
        
        // Create Inventory entity from DTO
        Inventory newInventory = new Inventory();
        newInventory.setProductId(inventory.getProductId());
        newInventory.setStock(inventory.getStock());
        
        return inventoryRepository.save(newInventory);
    }
    
    @KafkaListener(topics = "PRODUCT_CREATED", groupId = "inventory-group")
    public void handleProductCreatedEvent(ProductEvent event) { 
        if ("PRODUCT_CREATED".equals(event.getEventType())) {
            Inventory inventory = new Inventory();
            inventory.setProductId(event.getProductId());
            inventory.setStock(event.getStock() != null ? event.getStock() : 0);
            inventoryRepository.save(inventory);
        }
    }

    @KafkaListener(topics = "ORDER_CREATED", groupId = "inventory-group")
    public void handleOrderCreatedEvent(OrderEvent event) {
        if ("ORDER_CREATED".equals(event.getEventType())) {
            Inventory inventory = inventoryRepository.findByProductId(event.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", event.getProductId()));
            
            if (inventory.getStock() >= event.getQuantity()) {
                inventory.setStock(inventory.getStock() - event.getQuantity());
                inventoryRepository.save(inventory);

                kafkaTemplate.send("ORDER_STATUS",
                    new OrderStatusEvent(event.getOrderId(), "SUCCESS")
                );
            } else {
                // send failure event
                kafkaTemplate.send("ORDER_STATUS",
                    new OrderStatusEvent(event.getOrderId(), "FAILED")
                );
            }
        }
    }


    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));
    }
    
    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", productId));
    }
    
    public Inventory updateInventory(Long id, InventoryDTO inventory) {
        Inventory existingInventory = getInventoryById(id);
        
        // Validate stock if provided
        if (inventory.getStock() != null) {
            if (inventory.getStock() < 0) {
                throw new BadRequestException("Stock quantity cannot be negative");
            }
            existingInventory.setStock(inventory.getStock());
        }
        
        return inventoryRepository.save(existingInventory);
    }

    public Inventory restockInventory(RestockDTO restockDTO) {
        // Validate restock data
        if (restockDTO == null || restockDTO.getProductId() == null) {
            throw new BadRequestException("Product ID is required");
        }
        
        if (restockDTO.getStock() == null || restockDTO.getStock() <= 0) {
            throw new BadRequestException("Restock quantity must be greater than zero");
        }
        
        // Get existing inventory by product ID
        Inventory inventory = inventoryRepository.findByProductId(restockDTO.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Inventory", "productId", restockDTO.getProductId()));
        
        // Add new stock to existing stock
        Integer newStock = inventory.getStock() + restockDTO.getStock();
        inventory.setStock(newStock);
        
        return inventoryRepository.save(inventory);
    }
    
    public Inventory reduceStock(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity to reduce must be greater than zero");
        }
        
        Inventory inventory = getInventoryById(id);
        
        if (inventory.getStock() < quantity) {
            throw new BadRequestException("Insufficient stock. Available: " + inventory.getStock() + ", Requested: " + quantity);
        }
        
        inventory.setStock(inventory.getStock() - quantity);
        return inventoryRepository.save(inventory);
    }
    
    public void deleteInventory(Long id) {
        Inventory inventory = getInventoryById(id);
        inventoryRepository.delete(inventory);
    }
}
