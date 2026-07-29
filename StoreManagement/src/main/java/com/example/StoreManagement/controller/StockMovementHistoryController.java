package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoResponse.StockMovementHistoryDtoResponse;
import com.example.StoreManagement.service.StockMovementHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stockmovementshistory")
public class StockMovementHistoryController {

    private final StockMovementHistoryService stockMovementHistoryService;


    @GetMapping
    public ResponseEntity<List<StockMovementHistoryDtoResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.stockMovementHistoryService.findAll());
    }
}
