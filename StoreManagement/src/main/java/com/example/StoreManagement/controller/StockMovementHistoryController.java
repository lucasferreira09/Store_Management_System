package com.example.StoreManagement.controller;

import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoResponse.StockMovementHistoryDtoResponse;
import com.example.StoreManagement.service.StockMovementHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stockmovementshistory")
public class StockMovementHistoryController {

    private final StockMovementHistoryService stockMovementHistoryService;


    @GetMapping
    public ResponseEntity<PagingResult<StockMovementHistoryDtoResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {

        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, direction);
        return ResponseEntity.ok(this.stockMovementHistoryService.findAll(request));
    }
}
