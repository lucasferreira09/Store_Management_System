package com.example.StoreManagement.model.dtoResponse;

import com.example.StoreManagement.model.entity.Payment;

import java.util.Optional;

public record PaymentChanged(
        Optional<Payment> payment,
        boolean changed
) {}
