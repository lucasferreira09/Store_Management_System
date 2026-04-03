package com.example.StoreManagement.model.entity;

import com.example.StoreManagement.model.entity.enums.StockMovementType;
import com.example.StoreManagement.model.entity.enums.StockMovementReason;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.SqlTypedJdbcType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "stock_movement_history")
public class StockMovementHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", columnDefinition = "stock_movement_type", nullable = false)
    private StockMovementType type;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "reason", columnDefinition = "stock_movement_reason", nullable = false)
    private StockMovementReason reason;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "sale_id", nullable = true)
    private Integer saleId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "inventory_id", referencedColumnName = "id", nullable = false)
    private Inventory inventory;



    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
