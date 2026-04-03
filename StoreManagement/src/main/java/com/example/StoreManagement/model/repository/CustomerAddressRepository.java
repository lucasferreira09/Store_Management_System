package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    List<CustomerAddress> findByCustomerId(Long id);

    List<CustomerAddress> findByAddressId(Long id);

    Boolean existsByAddressId(Long id);

    CustomerAddress findByCustomerIdAndAddressId(Long customerId, Long AddressId);

}
