package com.example.StoreManagement.model.entity;


import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;
import com.example.StoreManagement.model.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import javax.print.attribute.EnumSyntax;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "currency")
    private String currency;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "paymentStatus", columnDefinition = "payment_status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "paymentProvider", columnDefinition = "payment_provider", nullable = false)
    private PaymentProvider paymentProvider;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "paymentMethodType", columnDefinition = "payment_method_type", nullable = false)
    private PaymentMethodType paymentMethodType;

    @Column(name = "cardBrand")
    private String cardBrand;

    @Column(name = "lastCardNumbers")
    private String lastCardNumbers;

    @Column(name = "providerPaymentId")
    private String providerPaymentId;

    @Column(name = "providerSessionId")
    private String providerSessionId;

    @Column(name = "providerChargeId")
    private String providerChargeId;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "payment")
    private List<Order> orders;


}
