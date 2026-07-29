package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoRequest.CategoryDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CategoryDtoResponse;
import com.example.StoreManagement.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    public final CategoryService categoryService;


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
