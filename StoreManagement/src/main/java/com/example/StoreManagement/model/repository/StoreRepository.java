package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByName(String name);
    Store findByCnpj(String cnpj);
    List<Store> findByAddressId(Long id);


    Boolean existsByCnpj(String cnpj);

}
