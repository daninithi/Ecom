package com.example.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.product.dto.ProductDTO;
import com.example.common.event.ProductEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, ProductEvent> kafkaTemplate;
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    private static final String PRODUCT_CREATE_TOPIC = "PRODUCT_CREATED";

    public Product createProduct(ProductDTO productDTO) {
        // Validate product data
        if (productDTO == null) {
            throw new BadRequestException("Product data cannot be null");
        }
        
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new BadRequestException("Product name cannot be empty");
        }
        
        if (productDTO.getPrice() == null || productDTO.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Product price must be greater than zero");
        }
        
        // Check if product with same name already exists
        if (productRepository.existsByName(productDTO.getName())) {
            throw new BadRequestException("Product with name '" + productDTO.getName() + "' already exists");
        }
        
        // Create new product
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        Product savedProduct = productRepository.save(product);

        Integer stock = productDTO.getInitialStock() != null ? productDTO.getInitialStock() : 0;
        ProductEvent event = new ProductEvent("PRODUCT_CREATED", savedProduct.getId(), stock);
        kafkaTemplate.send(PRODUCT_CREATE_TOPIC, event);

        return savedProduct;
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }
    
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product product = getProductById(id);
        
        // Validate and update name
        if (productDTO.getName() != null) {
            if (productDTO.getName().trim().isEmpty()) {
                throw new BadRequestException("Product name cannot be empty");
            }
            // Check if updating to a name that already exists (but not the current product's name)
            if (!productDTO.getName().equals(product.getName()) && 
                productRepository.existsByName(productDTO.getName())) {
                throw new BadRequestException("Product with name '" + productDTO.getName() + "' already exists");
            }
            product.setName(productDTO.getName());
        }
        
        // Validate and update price
        if (productDTO.getPrice() != null) {
            if (productDTO.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Product price must be greater than zero");
            }
            product.setPrice(productDTO.getPrice());
        }
        
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
