package com.example.StoreManagement.service;

import com.example.StoreManagement.model.dtoRequest.PaymentCreationRequest;
import com.example.StoreManagement.model.entity.Order;
import com.example.StoreManagement.model.entity.Payment;
import com.example.StoreManagement.model.entity.enums.OrderStatus;
import com.example.StoreManagement.model.entity.enums.PaymentStatus;
import com.example.StoreManagement.model.repository.OrderRepository;
import com.example.StoreManagement.model.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private PaymentCreationRequest paymentCreationRequest;


    private Payment doPayment(PaymentCreationRequest paymentCreationRequest, List<Order> orders) {

        Payment payment = new Payment();

        if ((paymentCreationRequest.createdAt() != null)) {
            payment.setCreatedAt(paymentCreationRequest.createdAt());
        } else {
            payment.setCreatedAt(LocalDateTime.now());
        }

        payment.setPaymentProvider(paymentCreationRequest.paymentProvider());
        payment.setPaymentMethodType(paymentCreationRequest.paymentMethodType());
        payment.setProviderPaymentId(paymentCreationRequest.providerPaymentId());
        payment.setProviderSessionId(paymentCreationRequest.sessionProviderId());
        payment.setProviderChargeId(paymentCreationRequest.providerChargeId());
        payment.setAmount(paymentCreationRequest.amount());
        payment.setCurrency(paymentCreationRequest.currency());
        payment.setCardBrand(paymentCreationRequest.cardBrand());
        payment.setLastCardNumbers(paymentCreationRequest.lastCardNumbers());
        payment.setOrders(orders);
        payment.setPaymentStatus(PaymentStatus.APPROVED);

        return this.paymentRepository.save(payment);
    }

    @Transactional
    public void createPayment(PaymentCreationRequest paymentCreationRequest) {
        this.paymentCreationRequest = paymentCreationRequest;

        List<String> orderIds = paymentCreationRequest.orderIds();
        List<Long> longOrderIds = orderIds.stream().map(Long::valueOf).toList();

        List<Order> orders = orderRepository.findAllById(longOrderIds);

        Payment payment = this.doPayment(paymentCreationRequest, orders);

        for (Order order : orders) {
            order.setPayment(payment);
            order.setStatus(OrderStatus.PAID);
            this.orderRepository.save(order);
        }
    }
}
