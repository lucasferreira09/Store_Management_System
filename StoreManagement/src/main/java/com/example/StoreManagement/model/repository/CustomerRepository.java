package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Boolean existsByCpf(String cpf);
    Boolean existsByEmail(String email);
    List<Customer> findByName(String name);
    Customer findByCpf(String cpf);

}
