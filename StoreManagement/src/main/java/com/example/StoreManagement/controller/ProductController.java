package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoRequest.ProductDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.ProductDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.ProductDtoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.StoreManagement.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public List<ProductDtoResponse> getAll() {
        return this.productService.findAll();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductDtoResponse> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductDtoResponse>> getByName(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.findByName(name));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDtoResponse>> getByCategoryId(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.findByCategoryId(id));
    }
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductDtoResponse> getByBarcode(@PathVariable String barcode) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.findByBarcode(barcode));
    }

    @PostMapping()
    public ResponseEntity<ProductDtoResponse> create(@RequestBody @Valid ProductDtoPostRequest productDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.productService.create(productDtoPostRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDtoResponse> update(
            @PathVariable Long id, @RequestBody @Valid ProductDtoPostRequest dtoPostRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.update(id, dtoPostRequest));
    }

    @PutMapping("/productId/{productId}/categoryId/{categoryId}")
    public ResponseEntity<ProductDtoResponse> update(
            @PathVariable Long productId, @PathVariable Long categoryId
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.update(productId, categoryId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
