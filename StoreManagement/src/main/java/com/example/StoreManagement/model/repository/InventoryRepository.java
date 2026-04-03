package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByIdAndActiveTrue(Long id);
    List<Inventory> findAllByActiveTrue();
    Inventory findByStoreIdAndProductId(Long storeId, Long productId);

    @Query("""
        SELECT i from Inventory i WHERE i.store.id = :id
        AND i.active = true
        AND i.store.active = true
        AND i.product.active = true
    """)
    List<Inventory> findValidInventoriesByStoreId(Long id);

}
