package com.example.StoreManagement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    @Column(name = "photo")
    private String photo;

    @Column(name = "sale_price", nullable = false)
    private BigDecimal sale_price;

    @Column(name = "cost_price", nullable = false)
    private BigDecimal cost_price;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

}
