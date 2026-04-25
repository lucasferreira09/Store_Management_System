package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.PaginationRequest;
import com.example.StoreManagement.model.PaginationUtils;
import com.example.StoreManagement.model.PagingResult;
import com.example.StoreManagement.model.dtoRequest.OrderDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.OrderDetailsDtoResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDtoResponse;
import com.example.StoreManagement.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<PagingResult<OrderDtoResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(required = false) Sort.Direction direction
    ) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size, sortField, direction);
        PagingResult<OrderDtoResponse> orders = this.orderService.findAll(paginationRequest);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<OrderDetailsDtoResponse> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.orderService.findById(id));
    }

    @GetMapping("/customer/{id}/orders")
    public ResponseEntity<PagingResult<OrderDtoResponse>> getByCustomerId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        PaginationRequest request = new PaginationRequest(page, size, sortField, direction);
        PagingResult<OrderDtoResponse> customerOrders = this.orderService.findByCustomerId(id, request);

        return ResponseEntity.ok(customerOrders);
    }

    @PostMapping()
    public ResponseEntity<OrderDtoResponse> create(@RequestBody @Valid OrderDtoPostRequest orderDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.orderService.create(orderDtoPostRequest));
    }
}
