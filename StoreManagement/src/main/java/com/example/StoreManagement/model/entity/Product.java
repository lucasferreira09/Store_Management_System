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
    private Long productID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    @Column(name = "photo")
    private String photo;

    @Column(name = "salePrice", nullable = false)
    private BigDecimal salePrice;

    @Column(name = "costPrice", nullable = false)
    private BigDecimal costPrice;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "categoryID")
    private Category category;

}
