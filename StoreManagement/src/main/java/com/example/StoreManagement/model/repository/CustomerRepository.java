package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByActiveTrue();
    Optional<Customer> findByIdAndActiveTrue(Long id);
    Customer findByEmail(String email);
    List<Customer> findByNameAndActiveTrue(String name);
    Optional<Customer> findByCpfAndActiveTrue(String cpf);
    Customer findByCpf(String cpf);

}
