package com.example.StoreManagement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE product set ACTIVE = false WHERE id = ?")
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
    private BigDecimal salePrice;

    @Column(name = "cost_price", nullable = false)
    private BigDecimal costPrice;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;
}
