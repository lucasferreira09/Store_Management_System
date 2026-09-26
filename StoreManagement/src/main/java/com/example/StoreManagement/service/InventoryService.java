package com.example.StoreManagement.service;

import com.example.StoreManagement.Exception.ResourceAlreadyInUseException;
import com.example.StoreManagement.Exception.ResourceNotFoundException;
import com.example.StoreManagement.mapstruct.mappers.InventoryMapper;
import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PaginationUtils;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.InventoryDtoPostRequest;
import com.example.StoreManagement.dtos.dtoRequest.StockMovementRequest;
import com.example.StoreManagement.dtos.dtoResponse.InventoryDtoResponse;
import com.example.StoreManagement.model.entity.Inventory;
import com.example.StoreManagement.model.entity.Product;
import com.example.StoreManagement.model.entity.StockMovementHistory;
import com.example.StoreManagement.model.entity.Store;
import com.example.StoreManagement.repository.InventoryRepository;
import com.example.StoreManagement.repository.ProductRepository;
import com.example.StoreManagement.repository.StockMovementHistoryRespository;
import com.example.StoreManagement.repository.StoreRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final StockMovementHistoryRespository stockMovementHistoryRespository;
    private final InventoryMapper inventoryMapper;
    private final StockMovementHistoryService stockMovementHistoryService;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private static final String entityName = "Inventory";


    public PagingResult<InventoryDtoResponse> findAll(PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);

        Page<Inventory> inventoriesPage = this.inventoryRepository.findAllByActiveTrue(pageable);
        List<InventoryDtoResponse> inventoriesDtoList = inventoriesPage.stream().map(inventoryMapper::entityToDtoResponse).toList();

        return new PagingResult<>(
                inventoriesDtoList,
                inventoriesPage.getTotalPages(),
                inventoriesPage.getTotalElements(),
                inventoriesPage.getSize(),
                inventoriesPage.getSize(),
                inventoriesPage.isEmpty(),
                inventoriesPage.isLast()
        );
    }

    public InventoryDtoResponse create(InventoryDtoPostRequest dtoPostRequest) {
        if (dtoPostRequest.quantity() < 0)
            throw new RuntimeException("Quantity must be greater or equal than zero");

        Product product = this.productRepository.findByIdAndActiveTrueAndCategoryActiveTrue(dtoPostRequest.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID:" + dtoPostRequest.productId().toString()));

        Store store = this.storeRepository.findByIdAndActiveTrue(dtoPostRequest.storeId())
                .orElseThrow(() -> new ResourceNotFoundException("Store", "ID:" + dtoPostRequest.storeId().toString()));


        Inventory existingInventory = this.inventoryRepository.findByStoreIdAndProductId(dtoPostRequest.storeId(), dtoPostRequest.productId());
        if (existingInventory != null) {
            if (existingInventory.isActive())
                throw new ResourceAlreadyInUseException(entityName, "ID:" + existingInventory.getId().toString());

            return this.inventoryMapper.entityToDtoResponse(
                    this.reactivateInventory(existingInventory, dtoPostRequest.quantity()));
        }

        Inventory inventory = this.inventoryMapper.dtoPostRequestToEntity(dtoPostRequest);
        this.inventoryRepository.save(inventory);
        return this.inventoryMapper.entityToDtoResponse(inventory);
    }


    public PagingResult<InventoryDtoResponse> findByStoreId(Long id, PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);
        Page<Inventory> inventoriesPage = this.inventoryRepository.findValidInventoriesByStoreId(id, pageable);

        List<InventoryDtoResponse> inventoriesDtoList = inventoriesPage
                .stream()
                .map(inventoryMapper::entityToDtoResponse).toList();

        return new PagingResult<>(
                inventoriesDtoList,
                inventoriesPage.getTotalPages(),
                inventoriesPage.getTotalElements(),
                inventoriesPage.getSize(),
                inventoriesPage.getSize(),
                inventoriesPage.isEmpty(),
                inventoriesPage.isLast()
        );
    }

    public void processMovement(StockMovementRequest.DtoPostRequest dtoPostRequest) {
        Inventory inventory = this.inventoryRepository.findById(dtoPostRequest.inventoryId())
                .orElseThrow(() -> new ResourceAlreadyInUseException(entityName, "ID:" + dtoPostRequest.inventoryId().toString()));

        processMovement(new StockMovementRequest(
                inventory,
                dtoPostRequest.type(),
                dtoPostRequest.reason(),
                dtoPostRequest.quantity(),
                dtoPostRequest.orderId(),
                Instant.now(),
                dtoPostRequest.description()
        ));
    }

    @Transactional
    public void processMovement(StockMovementRequest movementRequest) {

        Inventory inventory = movementRequest.inventory();
        if (inventory == null)
            throw new ResourceNotFoundException(entityName, "");

        if (movementRequest.quantity() < 0)
            throw new RuntimeException("Quantity must be greater or equal than zero");

        switch (movementRequest.type()) {
            case IN -> this.increaseStock(inventory, movementRequest);
            case OUT -> this.decreaseStock(inventory, movementRequest);
            case ADJUSTMENT -> this.adjustStock(inventory, movementRequest);
            default -> throw new RuntimeException("Movement type doesn't exist.");
        }
    }

    private void increaseStock(Inventory inventory, StockMovementRequest movementRequest) {

        inventory.setQuantity(inventory.getQuantity() + movementRequest.quantity());
        this.inventoryRepository.save(inventory);
        this.saveStockMovement(inventory, movementRequest);
    }


    private void decreaseStock(Inventory inventory, StockMovementRequest movementRequest) {
        if (movementRequest.quantity() > inventory.getQuantity())
            throw new RuntimeException("Insufficient stock");

        inventory.setQuantity(inventory.getQuantity() - movementRequest.quantity());
        this.inventoryRepository.save(inventory);
        this.saveStockMovement(inventory, movementRequest);
    }


    private void adjustStock(Inventory inventory, StockMovementRequest movementRequest)  {

        inventory.setQuantity(movementRequest.quantity());
        this.inventoryRepository.save(inventory);
        this.saveStockMovement(inventory, movementRequest);
    }


    private void saveStockMovement(Inventory inventory, StockMovementRequest movementRequest) {
        StockMovementHistory stockMovementHistory = new StockMovementHistory();
        stockMovementHistory.setInventory(inventory);
        stockMovementHistory.setType(movementRequest.type());
        stockMovementHistory.setReason(movementRequest.reason());
        stockMovementHistory.setQuantity(movementRequest.quantity());
        stockMovementHistory.setOrderId(movementRequest.orderId().toString());
        stockMovementHistory.setCreatedAt(movementRequest.created());
        stockMovementHistory.setDescription(movementRequest.description());
        this.stockMovementHistoryRespository.save(stockMovementHistory);
    }


    private Inventory reactivateInventory(Inventory inventory, int quantity) {
        inventory.setActive(true);
        inventory.setQuantity(quantity);
        return this.inventoryRepository.save(inventory);
    }
}
