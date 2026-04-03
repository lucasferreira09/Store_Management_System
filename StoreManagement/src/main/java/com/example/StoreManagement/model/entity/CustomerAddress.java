package com.example.StoreManagement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_address")
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "addressId", referencedColumnName = "id", nullable = false)
    private Address address;
}
