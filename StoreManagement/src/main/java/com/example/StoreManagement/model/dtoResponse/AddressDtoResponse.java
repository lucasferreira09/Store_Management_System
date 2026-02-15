package com.example.StoreManagement.model.dtoResponse;

import com.example.StoreManagement.model.entity.Customer;

public record AddressDtoResponse(
        String street,
        String neighbourhood,
        String complement,
        String number,
        String city,
        String state,
        String zip,
        Long customerID

) {}
