package com.example.StoreManagement.model.entity;


import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;
import com.example.StoreManagement.model.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency", length = 5, nullable = false)
    private String currency;

    @Column(name = "checkoutId", updatable = false, nullable = false)
    private UUID checkoutId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "paymentStatus", columnDefinition = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "paymentProvider", columnDefinition = "payment_provider", nullable = false)
    private PaymentProvider paymentProvider;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "paymentMethodType", columnDefinition = "payment_method_type", nullable = false)
    private PaymentMethodType paymentMethodType;

    @Column(name = "providerSessionId", nullable = false)
    private String providerSessionId;

    @Column(name = "createdAt", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private Instant createdAt;

    @Column(name = "expiresAt", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private Instant expiresAt;

    @Column(name = "paidAt", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant paidAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = Instant.now();
    }

}
