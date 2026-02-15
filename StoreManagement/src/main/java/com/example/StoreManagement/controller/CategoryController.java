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
    public ResponseEntity<List<CategoryDtoResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryDtoResponse> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.findById(id));
    }


    @GetMapping("/productId/{id}")
    public ResponseEntity<CategoryDtoResponse> getByProductId(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.findByProductId(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<CategoryDtoResponse>> getByName(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<CategoryDtoResponse> create(@RequestBody @Valid CategoryDtoPostRequest categoryDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.categoryService.create(categoryDtoPostRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDtoResponse> update(
            @PathVariable Long id, @RequestBody @Valid CategoryDtoPostRequest categoryDtoPostRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.update(id, categoryDtoPostRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
