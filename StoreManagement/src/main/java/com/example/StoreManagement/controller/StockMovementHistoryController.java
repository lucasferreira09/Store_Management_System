package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoResponse.StockMovementHistoryDtoResponse;
import com.example.StoreManagement.service.StockMovementHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stockmovementshistory")
public class StockMovementHistoryController {

    private StockMovementHistoryService stockMovementHistoryService;

    public StockMovementHistoryController(StockMovementHistoryService stockMovementHistoryService) {
        this.stockMovementHistoryService = stockMovementHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<StockMovementHistoryDtoResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.stockMovementHistoryService.findAll());
    }
}
