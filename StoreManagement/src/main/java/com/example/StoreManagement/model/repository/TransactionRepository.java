package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
