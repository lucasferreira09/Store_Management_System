package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.PaginationRequest;
import com.example.StoreManagement.model.PagingResult;
import com.example.StoreManagement.model.dtoRequest.OrderDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.OrderCreationDtoResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDetailsDtoResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDtoResponse;
import com.example.StoreManagement.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public ResponseEntity<PagingResult<OrderDtoResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        PaginationRequest paginationRequest = new PaginationRequest(pageNumber, size, sortField, direction);
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
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, direction);
        PagingResult<OrderDtoResponse> customerOrders = this.orderService.findByCustomerId(id, request);

        return ResponseEntity.ok(customerOrders);
    }


    @PostMapping()
    public ResponseEntity<OrderCreationDtoResponse> create(@RequestBody @Valid OrderDtoPostRequest orderDtoPostRequest) throws IllegalAccessException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.orderService.create(orderDtoPostRequest));
    }
}
