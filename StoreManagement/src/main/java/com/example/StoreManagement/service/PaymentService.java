package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.PaymentMapper;
import com.example.StoreManagement.model.dtoRequest.PaymentCompletedData;
import com.example.StoreManagement.model.dtoRequest.PaymentCreation;
import com.example.StoreManagement.model.dtoRequest.PaymentCreationRequest;
import com.example.StoreManagement.model.dtoRequest.CheckoutCreationRequest;
import com.example.StoreManagement.model.dtoResponse.*;
import com.example.StoreManagement.model.entity.Payment;
import com.example.StoreManagement.model.entity.Transaction;
import com.example.StoreManagement.model.entity.enums.OrderStatus;
import com.example.StoreManagement.model.entity.enums.PaymentStatus;
import com.example.StoreManagement.model.repository.PaymentRepository;
import com.example.StoreManagement.model.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentGatewayFactory gatewayFactory;
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final TransactionRepository transactionRepository;
    private final PaymentMapper paymentMapper;


    private void updatePayment(PaymentCreation paymentCreation) {
        Optional<Payment> payment = this.paymentRepository.findByCheckoutId(paymentCreation.checkoutId());
        if (payment.isEmpty())
            throw new EntityNotFoundException("Payment not found");


        payment.get().setPaymentProvider(paymentCreation.paymentProvider());
        payment.get().setCreatedAt(paymentCreation.createdAt());
        payment.get().setExpiresAt(paymentCreation.expiresAt());
        payment.get().setProviderSessionId(paymentCreation.providerSessionId());
        payment.get().setPaymentStatus(PaymentStatus.PENDING);
        this.paymentRepository.save(payment.get());
    }

    private void savePayment(PaymentCreation paymentCreation) {
        Optional<Payment> existingPayment = this.paymentRepository.findByCheckoutId(paymentCreation.checkoutId());
        if (existingPayment.isPresent())
            throw new EntityNotFoundException("Payment already exists");


        Payment payment = this.paymentMapper.paymentCreationToPayment(paymentCreation);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentMethodType(paymentCreation.paymentMethodType());
        this.paymentRepository.save(payment);
    }

    private PaymentChanged reusePayment(PaymentCreationRequest paymentCreationRequest) {
        Optional<Payment> existingPayment = this.paymentRepository.findByCheckoutId(paymentCreationRequest.checkoutId());
        if (existingPayment.isPresent() && existingPayment.get().getPaymentStatus() != PaymentStatus.PENDING)
            throw new IllegalStateException("Cannot modify payment for checkout %s. Payment already completed".formatted(paymentCreationRequest.checkoutId()));


        boolean canReusePayment = existingPayment.isPresent()
                && existingPayment.get().getPaymentMethodType() == paymentCreationRequest.paymentMethodType()
                && existingPayment.get().getPaymentStatus() == PaymentStatus.PENDING
                && existingPayment.get().getExpiresAt().isAfter(Instant.now());

        if (canReusePayment)
            return handleExistingPayment(existingPayment, paymentCreationRequest);
        else
            return null;
    }

    private PaymentCreationResponse handleNewPayment(PaymentCreationRequest paymentCreationRequest, ProviderCheckoutResponse checkoutResponse) {
        Optional<Payment> existingPayment = this.paymentRepository.findByCheckoutId(paymentCreationRequest.checkoutId());
        if (existingPayment.isPresent())
            this.reusePayment(paymentCreationRequest);

        this.savePayment(this.paymentMapper.providerCheckoutToPaymentCreation(checkoutResponse));
        return this.paymentMapper.providerCheckoutToPaymentCreationResponse(checkoutResponse);
    }

    private PaymentChanged handleExistingPayment(Optional<Payment> payment, PaymentCreationRequest paymentCreationRequest) {
        if (payment.get().getPaymentStatus() != PaymentStatus.PENDING)
            throw new IllegalStateException("Cannot modify payment for checkout %s: already %s"
                            .formatted(payment.get().getCheckoutId(), payment.get().getPaymentStatus()));


        boolean sameProvider = payment.get().getPaymentProvider() == paymentCreationRequest.paymentProvider();
        boolean samePaymentMethodType = payment.get().getPaymentMethodType() == paymentCreationRequest.paymentMethodType();
        boolean stillValid = payment.get().getExpiresAt() != null && payment.get().getExpiresAt().isAfter(Instant.now());

        if (sameProvider && samePaymentMethodType && stillValid) {
            return new PaymentChanged(payment, false); // reuse — caller redirects to the same session URL
        }

        expireExistingPayment(paymentCreationRequest.checkoutId());
        return new PaymentChanged(payment, true);
    }


    @Transactional
    public PaymentCreationResponse createPayment(PaymentCreationRequest paymentCreationRequest) {
        PaymentGateway gateway = gatewayFactory.get(paymentCreationRequest.paymentProvider());

        OrderCreationResponse orderCreationResponse = orderService.getCheckoutById(paymentCreationRequest.checkoutId());
        if (orderCreationResponse.status() != OrderStatus.AWAITING_PAYMENT)
            throw new IllegalArgumentException("Cannot create payment for checkout %s. These orders are already completed".formatted(paymentCreationRequest.checkoutId()));

        CheckoutCreationRequest checkoutCreationRequest = new CheckoutCreationRequest(
                paymentCreationRequest.checkoutId(),
                orderCreationResponse.amount(),
                "BRL",
                "description",
                paymentCreationRequest.paymentProvider(),
                paymentCreationRequest.paymentMethodType()
        );

        PaymentChanged paymentChanged = this.reusePayment(paymentCreationRequest);
        if (paymentChanged != null) {
            if (!paymentChanged.changed()) {
                return new PaymentCreationResponse(
                        paymentCreationRequest.checkoutId(),
                        paymentCreationRequest.paymentMethodType(),
                        paymentCreationRequest.paymentProvider(),
                        paymentChanged.payment().get().getProviderSessionId(),
                        paymentChanged.payment().get().getAmount(),
                        paymentChanged.payment().get().getCurrency(),
                        this.getCheckoutUrl(checkoutCreationRequest.checkoutId())
                );
            }

            ProviderCheckoutResponse checkoutResponse = gateway.processPayment(checkoutCreationRequest);
            if (checkoutResponse == null)
              throw new RuntimeException("Error processing payment");

            PaymentCreation paymentCreation = this.paymentMapper.providerCheckoutToPaymentCreation(checkoutResponse);
            PaymentCreationResponse creationResponse =  this.paymentMapper.providerCheckoutToPaymentCreationResponse(checkoutResponse);
            this.updatePayment(paymentCreation);

            return creationResponse;
        }
        else {

            ProviderCheckoutResponse checkoutResponse = gateway.processPayment(checkoutCreationRequest);
            if (checkoutResponse == null)
                throw new RuntimeException("Error processing payment");

            return handleNewPayment(paymentCreationRequest, checkoutResponse);
        }
    }

    public void processPaymentSucceeded(PaymentCompletedData data) {
        Optional<Payment> payment = this.paymentRepository.findByCheckoutId(data.checkoutId());
        if (payment.isEmpty())
            throw new EntityNotFoundException("Payment not found");

        payment.get().setPaymentStatus(PaymentStatus.APPROVED);
        payment.get().setPaidAt(data.createdAt());
        this.paymentRepository.save(payment.get());

        this.orderService.changeOrderStatus(data.checkoutId(), OrderStatus.PAID);
        this.saveTransaction(data, payment.get());
    }

    public void processPaymentFailed(PaymentCompletedData data) {
        Optional<Payment> payment = this.paymentRepository.findByCheckoutId(data.checkoutId());
        if (payment.isEmpty())
            throw new EntityNotFoundException("Payment not found");

        payment.get().setPaymentStatus(PaymentStatus.FAILED);
        this.paymentRepository.save(payment.get());
        this.saveTransaction(data, payment.get());
    }

    public void saveTransaction(PaymentCompletedData data, Payment payment) {
        Transaction transaction = this.paymentMapper.paymentCompletedToTransaction(data);
        transaction.setPaymentMessage(data.paymentMessage());
        transaction.setPayment(payment);
        transaction.setProviderSessionId(payment.getProviderSessionId());
        transaction.setPaymentStatus(payment.getPaymentStatus());
        transactionRepository.save(transaction);
    }

    public PaymentDtoReponse findByCheckoutId(UUID checkoutId) {
        Optional<Payment> payment = this.paymentRepository.findByCheckoutId(checkoutId);
        if (!payment.isPresent())
            throw new EntityNotFoundException("Payment not found");

        return this.paymentMapper.entityToPaymentDtoResponse(payment.get());
    }

    private Payment expireExistingPayment(UUID checkoutId) {
        Optional<Payment> payment = this.paymentRepository.findByCheckoutId(checkoutId);
        if (!payment.isPresent())
            throw new EntityNotFoundException("Payment not found");

        if (payment.get().getPaymentStatus() == PaymentStatus.APPROVED ||
                payment.get().getPaymentStatus() == PaymentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot modify payment for checkout %s. Payment already completed or expired".formatted(checkoutId));
        }


        PaymentGateway gateway = this.gatewayFactory.get(payment.get().getPaymentProvider());
        gateway.expireSession(payment.get().getProviderSessionId());
        return payment.get();
    }

    public void expirePayment(UUID checkoutId, String motive) {
        Payment payment = expireExistingPayment(checkoutId);
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
        this.orderService.cancelOrder(checkoutId, motive);
    }

    public String getCheckoutUrl(UUID checkoutId) {
        Optional<Payment> payment = this.paymentRepository.findByCheckoutId(checkoutId);
        if (!payment.isPresent())
            throw new EntityNotFoundException("Payment not found");

        if (payment.get().getPaymentStatus() == PaymentStatus.APPROVED || payment.get().getPaymentStatus() == PaymentStatus.CANCELLED)
            throw new IllegalStateException("Cannot pay for checkout %s. It's already completed or expired".formatted(checkoutId));


        PaymentGateway gateway = this.gatewayFactory.get(payment.get().getPaymentProvider());
        return gateway.getCheckoutUrl(payment.get().getProviderSessionId());
    }
}
