package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoRequest.CategoryDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CategoryDtoResponse;
import com.example.StoreManagement.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    public CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDtoResponse>> getAllCategories() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.getAllCategories());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryDtoResponse> getCategoryById(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.getCategoryById(id));
    }


    @GetMapping("/productID/{productID}")
    public ResponseEntity<CategoryDtoResponse> getCategoryByProductID(Long productID) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.getCategoryByProductID(productID));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<CategoryDtoResponse>> getCategoryByName(String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.getCategoryByName(name));
    }

    @PostMapping
    public ResponseEntity<CategoryDtoResponse> addCategory(@RequestBody @Valid CategoryDtoPostRequest categoryDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.categoryService.addCategory(categoryDtoPostRequest));
    }
}
