package com.example.product.controller;

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

import com.example.product.service.ProductService;
import com.example.common.dto.ResponseMessage;
import com.example.common.enums.ResponseStatus;
import com.example.product.dto.ProductDTO;
import com.example.product.model.Product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<ResponseMessage<List<Product>>> getProducts() {
        List<Product> products = productService.getAllProducts();
        ResponseMessage<List<Product>> response = new ResponseMessage<>(ResponseStatus.SUCCESS, products);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ResponseMessage<Product>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product createdProduct = productService.createProduct(productDTO);
        ResponseMessage<Product> response = new ResponseMessage<>(ResponseStatus.SUCCESS, createdProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        ResponseMessage<Product> response = new ResponseMessage<>(ResponseStatus.SUCCESS, product);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage<Product>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        Product updatedProduct = productService.updateProduct(id, productDTO);
        ResponseMessage<Product> response = new ResponseMessage<>(ResponseStatus.SUCCESS, updatedProduct);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        ResponseMessage<String> response = new ResponseMessage<>(ResponseStatus.SUCCESS, "Product deleted successfully");
        return ResponseEntity.ok(response);

    }
  
}
    


 
