package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findByActiveTrue(Pageable pageable);
    List<Store> findByActiveTrue();
    Optional<Store> findByIdAndActiveTrue(Long id);
    Store findByCnpj(String cnpj);
    Store findByCnpjAndActiveTrue(String cnpj);
    Store findByEmail(String email);
    List<Store> findByAddressId(Long id);
    Boolean existsByAddressId(Long id);
    List<Store> findByNameAndActiveTrue(String name);
}
