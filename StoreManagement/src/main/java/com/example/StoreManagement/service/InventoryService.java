package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.InventoryMapper;
import com.example.StoreManagement.model.dtoRequest.InventoryDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.InventoryMovementRequest;
import com.example.StoreManagement.model.dtoResponse.InventoryDtoResponse;
import com.example.StoreManagement.model.entity.Inventory;
import com.example.StoreManagement.model.entity.Product;
import com.example.StoreManagement.model.entity.StockMovementHistory;
import com.example.StoreManagement.model.entity.Store;
import com.example.StoreManagement.model.repository.InventoryRepository;
import com.example.StoreManagement.model.repository.ProductRepository;
import com.example.StoreManagement.model.repository.StockMovementHistoryRespository;
import com.example.StoreManagement.model.repository.StoreRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private InventoryRepository inventoryRepository;
    private StockMovementHistoryRespository stockMovementHistoryRespository;
    private InventoryMapper inventoryMapper;
    private StockMovementHistoryService stockMovementHistoryService;
    private ProductRepository productRepository;
    private StoreRepository storeRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            InventoryMapper inventoryMapper,
            StockMovementHistoryRespository stockMovementHistoryRespository,
            StockMovementHistoryService stockMovementHistoryService,
            ProductRepository productRepository,
            StoreRepository storeRepository
    ) {

        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.stockMovementHistoryRespository = stockMovementHistoryRespository;
        this.stockMovementHistoryService = stockMovementHistoryService;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    public List<InventoryDtoResponse> findAll() {
        List<Inventory> inventories = this.inventoryRepository.findAllByActiveTrue();

        return this.inventoryMapper.entitiesToDtoResponse(inventories);
    }

    public InventoryDtoResponse create(InventoryDtoPostRequest dtoPostRequest) {
        if (dtoPostRequest.quantity() < 0)
            throw new RuntimeException("Quantity must be greater or equal than zero");

        Product product = this.productRepository.findByIdAndActiveTrueAndCategoryActiveTrue(dtoPostRequest.productID())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this ID"));

        Store store = this.storeRepository.findByIdAndActiveTrue(dtoPostRequest.storeID())
                .orElseThrow(() -> new EntityNotFoundException("Store not found with this ID"));


        Inventory existingInventory = this.inventoryRepository.findByStoreIdAndProductId(dtoPostRequest.storeID(), dtoPostRequest.productID());
        if (existingInventory != null) {
            if (existingInventory.isActive())
                throw new EntityExistsException("Inventory already exists");

            return this.inventoryMapper.entityToDtoResponse(
                    this.reactivateInventory(existingInventory, dtoPostRequest.quantity()));
        }

        Inventory inventory = this.inventoryMapper.dtoPostRequestToEntity(dtoPostRequest);
        this.inventoryRepository.save(inventory);
        return this.inventoryMapper.entityToDtoResponse(inventory);
    }


    public List<InventoryDtoResponse> findByStoreId(Long id) {
        List<Inventory> inventories = this.inventoryRepository.findValidInventoriesByStoreId(id);

        return this.inventoryMapper.entitiesToDtoResponse(inventories);
    }


    @Transactional
    public void processMovement(InventoryMovementRequest movementRequest) {

        Inventory inventory = this.inventoryRepository.findByIdAndActiveTrue(movementRequest.inventoryID())
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found"));

        if (movementRequest.quantity() < 0)
            throw new RuntimeException("Quantity must be greater or equal than zero");

        switch (movementRequest.type()) {
            case IN -> this.increaseStock(inventory, movementRequest);
            case OUT -> this.decreaseStock(inventory, movementRequest);
            case ADJUSTMENT -> this.adjustStock(inventory, movementRequest);
            default -> throw new RuntimeException("Movement type doesn't exist.");
        }
    }

    private void increaseStock(Inventory inventory, InventoryMovementRequest movementRequest) {

        inventory.setQuantity(inventory.getQuantity() + movementRequest.quantity());
        this.inventoryRepository.save(inventory);
        this.saveStockMovement(inventory, movementRequest);
    }


    private void decreaseStock(Inventory inventory, InventoryMovementRequest movementRequest) {
        if (movementRequest.quantity() > inventory.getQuantity())
            throw new RuntimeException("Insufficient stock");

        inventory.setQuantity(inventory.getQuantity() - movementRequest.quantity());
        this.inventoryRepository.save(inventory);
        this.saveStockMovement(inventory, movementRequest);
    }


    private void adjustStock(Inventory inventory, InventoryMovementRequest movementRequest) {

        inventory.setQuantity(movementRequest.quantity());
        this.inventoryRepository.save(inventory);
        this.saveStockMovement(inventory, movementRequest);
    }


    private void saveStockMovement(
            Inventory inventory,
            InventoryMovementRequest movementRequest
    ) {
        StockMovementHistory stockMovementHistory = new StockMovementHistory();
        stockMovementHistory.setInventory(inventory);
        stockMovementHistory.setType(movementRequest.type());
        stockMovementHistory.setReason(movementRequest.reason());
        stockMovementHistory.setQuantity(movementRequest.quantity());
        stockMovementHistory.setSaleId(movementRequest.saleId());
        this.stockMovementHistoryRespository.save(stockMovementHistory);
    }


    private Inventory reactivateInventory(Inventory inventory, int quantity) {
        inventory.setActive(true);
        inventory.setQuantity(quantity);
        return this.inventoryRepository.save(inventory);
    }
}
