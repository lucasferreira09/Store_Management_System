package com.example.StoreManagement.repository;

import com.example.StoreManagement.model.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByActiveTrue();
    Page<Customer> findByActiveTrue(Pageable  pageable);

    Optional<Customer> findByIdAndActiveTrue(Long id);

    Customer findByEmail(String email);

    List<Customer> findByNameAndActiveTrue(String name);

    Page<Customer> findByNameAndActiveTrue(String name, Pageable pageable);

    Optional<Customer> findByCpfAndActiveTrue(String cpf);

    Customer findByCpf(String cpf);
}
