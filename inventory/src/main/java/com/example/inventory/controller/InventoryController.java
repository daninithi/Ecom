package com.example.inventory.controller;

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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.example.inventory.service.InventoryService;
import com.example.inventory.model.Inventory;
import com.example.inventory.dto.InventoryDTO;
import com.example.inventory.dto.RestockDTO;


@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;

    @GetMapping()
    public ResponseEntity<ResponseMessage<List<Inventory>>> getInventory() {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        ResponseMessage<List<Inventory>> response = new ResponseMessage<>(ResponseStatus.SUCCESS, inventoryList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<Inventory>> getInventoryById(@PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        ResponseMessage<Inventory> response = new ResponseMessage<>(ResponseStatus.SUCCESS, inventory);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ResponseMessage<Inventory>> createInventory(@Valid @RequestBody InventoryDTO inventory) {
        Inventory createdInventory = inventoryService.createInventory(inventory);
        ResponseMessage<Inventory> response = new ResponseMessage<>(ResponseStatus.SUCCESS, createdInventory);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/restock")
    public ResponseEntity<ResponseMessage<Inventory>> restockInventory(@Valid @RequestBody RestockDTO restockDTO) {
        Inventory restockedInventory = inventoryService.restockInventory(restockDTO);
        ResponseMessage<Inventory> response = new ResponseMessage<>(ResponseStatus.SUCCESS, restockedInventory);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage<String>> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        ResponseMessage<String> response = new ResponseMessage<>(ResponseStatus.SUCCESS, "Inventory deleted successfully");
        return ResponseEntity.ok(response);
    }
}
