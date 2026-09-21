package com.example.StoreManagement.controller;

import com.example.StoreManagement.dtos.dtoRequest.ProductDtoPutRequest;
import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.ProductDtoPostRequest;
import com.example.StoreManagement.dtos.dtoResponse.ProductDtoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.StoreManagement.service.ProductService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    @GetMapping()
    public ResponseEntity<PagingResult<ProductDtoResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {

        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, direction);
        return ResponseEntity.ok(this.productService.findAll(request));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductDtoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(this.productService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductDtoResponse>> getByName(@PathVariable String name) {
        return ResponseEntity.ok(this.productService.findByName(name));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDtoResponse>> getByCategoryId(@PathVariable Long id) {
        return ResponseEntity.ok(this.productService.findByCategoryId(id));
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductDtoResponse> getByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(this.productService.findByBarcode(barcode));
    }

    @PostMapping()
    public ResponseEntity<ProductDtoResponse> create(@RequestBody @Valid ProductDtoPostRequest productDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.productService.create(productDtoPostRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDtoResponse> update(
            @PathVariable Long id, @RequestBody @Valid ProductDtoPutRequest putRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.update(id, putRequest));
    }

    @PutMapping("/productId/{productId}/categoryId/{categoryId}")
    public ResponseEntity<ProductDtoResponse> update(
            @PathVariable Long productId, @PathVariable Long categoryId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.updateCategory(productId, categoryId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
