package com.example.StoreManagement.controller;

import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.InventoryDtoPostRequest;
import com.example.StoreManagement.dtos.dtoRequest.StockMovementRequest;
import com.example.StoreManagement.dtos.dtoResponse.InventoryDtoResponse;
import com.example.StoreManagement.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories")
public class InventoryController {

    private final InventoryService inventoryService;


    @GetMapping
    public ResponseEntity<PagingResult<InventoryDtoResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection
    ) {
        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, sortDirection);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.inventoryService.findAll(request));
    }

    @GetMapping("/store/id/{id}")
    public ResponseEntity<PagingResult<InventoryDtoResponse>> getByStoreId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection
    ) {
        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, sortDirection);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.inventoryService.findByStoreId(id, request));
    }

    @PostMapping
    public ResponseEntity<InventoryDtoResponse> create(@RequestBody @Valid InventoryDtoPostRequest dtoPostRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.inventoryService.create(dtoPostRequest));
    }


    @PostMapping("/inventory/movements")
    public ResponseEntity<Void> movement(@RequestBody @Valid StockMovementRequest.DtoPostRequest request) {
        this.inventoryService.processMovement(request);

        return ResponseEntity.ok().build();
    }
}
