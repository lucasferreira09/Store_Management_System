package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoRequest.ProductDtoPostRequest;
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
    public List<ProductDtoResponse> getAllProducts() {
        return this.productService.getAllProducts();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductDtoResponse> getProductById(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.getProductById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductDtoResponse>> getProductByName(String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.getProductByName(name));
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductDtoResponse> getProductByBarcode(String barcode) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.productService.getProductByBarcode(barcode));
    }

    @PostMapping()
    public ResponseEntity<ProductDtoResponse> addProduct(@RequestBody @Valid ProductDtoPostRequest productDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.productService.addProduct(productDtoPostRequest));
    }


}
