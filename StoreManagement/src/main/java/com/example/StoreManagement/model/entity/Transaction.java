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

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "paymentProvider", columnDefinition = "payment_provider", nullable = false)
    private PaymentProvider paymentProvider;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "paymentMethodType", columnDefinition = "payment_method_type", nullable = false)
    private PaymentMethodType paymentMethodType;

    @Column(name = "providerPaymentId")
    private String providerPaymentId;

    @Column(name = "providerSessionId", nullable = false)
    private String providerSessionId;

    @Column(name = "providerChargeId")
    private String providerChargeId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "paymentStatus", columnDefinition = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "cardBrand")
    private String cardBrand;

    @Column(name = "last4")
    private String last4;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "createdAt", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private Instant createdAt;

    @Column(name = "paymentMessage")
    private String paymentMessage;

    @ManyToOne
    @JoinColumn(name = "paymentId", referencedColumnName = "id")
    private Payment payment;
}
