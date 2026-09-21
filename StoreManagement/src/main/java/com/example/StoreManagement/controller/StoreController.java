package com.example.StoreManagement.controller;

import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.StoreDtoPostRequest;
import com.example.StoreManagement.dtos.dtoRequest.StoreDtoPutRequest;
import com.example.StoreManagement.dtos.dtoResponse.StoreDtoDetailResponse;
import com.example.StoreManagement.dtos.dtoResponse.StoreDtoResponse;
import com.example.StoreManagement.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping()
    public ResponseEntity<PagingResult<StoreDtoResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {
        PaginationRequest paginationRequest = new PaginationRequest(pageNumber, size, sortField, direction);
        PagingResult<StoreDtoResponse> stores = this.storeService.findAll(paginationRequest);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<StoreDtoResponse> getById(@PathVariable Long id){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.storeService.findById(id));
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<List<StoreDtoResponse>> getByName(@PathVariable String name){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.storeService.findByName(name));
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<StoreDtoResponse> getByCnpj(@PathVariable String cnpj){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.storeService.findByCnpj(cnpj));
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<List<StoreDtoResponse>> getByAddressId(@PathVariable Long id){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.storeService.findByAddressId(id));
    }

    @PostMapping()
    public ResponseEntity<StoreDtoDetailResponse> create(@RequestBody @Valid StoreDtoPostRequest storeDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.storeService.create(storeDtoPostRequest));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<StoreDtoDetailResponse> update(@PathVariable Long id, @RequestBody @Valid StoreDtoPutRequest dtoPutRequest) {

        return ResponseEntity.
                status(HttpStatus.OK)
                .body(this.storeService.update(id, dtoPutRequest));
    }

    @PutMapping("/storeId/{storeId}/cnpj")
    public ResponseEntity<StoreDtoDetailResponse> update(@PathVariable Long storeId, @RequestBody @Valid StoreDtoPutRequest.Cnpj putRequest) {

        return ResponseEntity.
                status(HttpStatus.OK)
                .body(this.storeService.updateCnpj(storeId, putRequest));
    }

    @PutMapping("/storeId/{storeId}/address")
    public ResponseEntity<StoreDtoResponse> update(@PathVariable Long storeId,  @RequestBody @Valid StoreDtoPutRequest.Adress putRequest) {

        return ResponseEntity.
                status(HttpStatus.OK)
                .body(this.storeService.updateAddress(storeId, putRequest));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        this.storeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
