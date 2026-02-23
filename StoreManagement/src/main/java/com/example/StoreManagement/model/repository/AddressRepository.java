package com.example.StoreManagement.model.repository;

import com.example.StoreManagement.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    //List<Address> findByCustomerId(Long id);

}
