package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.PaginationRequest;
import com.example.StoreManagement.model.PagingResult;
import com.example.StoreManagement.model.dtoRequest.OrderDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.OrderCreationResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDetailsDtoResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDtoResponse;
import com.example.StoreManagement.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/customer/{customerId}/orders")
    public ResponseEntity<PagingResult<OrderDtoResponse>> getByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, direction);
        PagingResult<OrderDtoResponse> customerOrders = this.orderService.findByCustomerId(customerId, request);

        return ResponseEntity.ok(customerOrders);
    }


    @PostMapping()
    public ResponseEntity<OrderCreationResponse> create(@RequestBody @Valid OrderDtoPostRequest orderDtoPostRequest) throws IllegalAccessException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.orderService.create(orderDtoPostRequest));
    }

    @PostMapping("/cancel/checkoutId/{checkoutId}")
    public ResponseEntity<Void> cancel(@PathVariable UUID checkoutId) throws IllegalAccessException {
        this.orderService.cancelOrder(checkoutId, null);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/checkoutId/{checkoutId}")
    public ResponseEntity<List<OrderDtoResponse>> getCheckoutById(@PathVariable UUID checkoutId) {

        return ResponseEntity.ok(this.orderService.findByCheckoutId(checkoutId));
    }
}
