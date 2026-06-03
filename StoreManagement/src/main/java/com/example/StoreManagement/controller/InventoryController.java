package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoRequest.InventoryDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.InventoryMovementRequest;
import com.example.StoreManagement.model.dtoResponse.InventoryDtoResponse;
import com.example.StoreManagement.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventarios")
public class InventarioController {

    private InventoryService inventoryService;

    public InventarioController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryDtoResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.inventoryService.findAll());
    }

    @GetMapping("/store/id/{id}")
    public ResponseEntity<List<InventoryDtoResponse>> getByStoreId(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.inventoryService.findByStoreId(id));
    }

    @PostMapping
    public ResponseEntity<InventoryDtoResponse> create(@RequestBody @Valid InventoryDtoPostRequest dtoPostRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.inventoryService.create(dtoPostRequest));
    }


    @PostMapping("/inventory/movements")
    public ResponseEntity<Void> create(@RequestBody @Valid InventoryMovementRequest request) {
        this.inventoryService.processMovement(request);

        return ResponseEntity.ok().build();
    }
}
