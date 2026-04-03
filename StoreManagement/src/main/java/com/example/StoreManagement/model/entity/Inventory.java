package com.example.StoreManagement.model.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(
        name = "inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniqueStoreAndProduct",
                        columnNames = {"store_id", "product_id"}
                )
        })
@SQLDelete(sql = "UPDATE inventory set active = false WHERE id = ?")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "storeId", referencedColumnName = "id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "active", nullable = false)
    private boolean active = true;
}