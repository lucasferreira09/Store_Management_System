package com.example.StoreManagement.dtos.dtoResponse;

import com.example.StoreManagement.model.entity.Payment;

import java.util.Optional;

public record PaymentChanged(
        Optional<Payment> payment,
        boolean changed
) {}
