package com.example.StoreManagement.controller;

import com.example.StoreManagement.dtos.dtoRequest.CategoryPutRequest;
import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.CategoryDtoPostRequest;
import com.example.StoreManagement.dtos.dtoResponse.CategoryDtoResponse;
import com.example.StoreManagement.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<PagingResult<CategoryDtoResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, direction);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.findAll(request));
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
            @PathVariable Long id, @RequestBody @Valid CategoryPutRequest putRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.categoryService.update(id, putRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
