package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.dtos.dtoRequest.PaymentCompletedData;
import com.example.StoreManagement.dtos.dtoRequest.PaymentCreation;
import com.example.StoreManagement.dtos.dtoResponse.PaymentCreationResponse;
import com.example.StoreManagement.dtos.dtoResponse.PaymentDtoReponse;
import com.example.StoreManagement.dtos.dtoResponse.ProviderCheckoutResponse;
import com.example.StoreManagement.model.entity.Payment;
import com.example.StoreManagement.model.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDtoReponse entityToDtoResponse(Payment payment);

    Transaction paymentCompletedToTransaction(PaymentCompletedData paymentCompletedData);
    Payment paymentCreationToPayment(PaymentCreation paymentCreation);

    PaymentCreation providerCheckoutToPaymentCreation(ProviderCheckoutResponse string);
    PaymentCreationResponse providerCheckoutToPaymentCreationResponse(ProviderCheckoutResponse string);

    PaymentDtoReponse entityToPaymentDtoResponse(Payment payment);
}
