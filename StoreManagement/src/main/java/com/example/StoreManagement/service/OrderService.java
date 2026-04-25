package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.OrderMapper;
import com.example.StoreManagement.model.PaginationRequest;
import com.example.StoreManagement.model.PaginationUtils;
import com.example.StoreManagement.model.PagingResult;
import com.example.StoreManagement.model.dtoRequest.OrderDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.OrderItemDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.OrderDetailsDtoResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDtoResponse;
import com.example.StoreManagement.model.entity.*;
import com.example.StoreManagement.model.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private OrderItemRepository orderItemRepository;
    private OrderRepository orderRepository;
    private StoreRepository storeRepository;
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private InventoryRepository inventoryRepository;
    private StockMovementHistoryRespository stockMovementHistoryRespository;
    private OrderMapper orderMapper;

    public OrderService(
            OrderItemRepository orderItemRepository,
            OrderRepository orderRepository,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            InventoryRepository inventoryRepository,
            StockMovementHistoryRespository stockMovementHistoryRespository,
            OrderMapper orderMapper
    ) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
        this.stockMovementHistoryRespository = stockMovementHistoryRespository;
        this.orderMapper = orderMapper;
    }

    public PagingResult<OrderDtoResponse> findAll(PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);
        Page<Order> ordersPage = this.orderRepository.findAll(pageable);
        List<OrderDtoResponse> ordersDtoResponse = ordersPage.stream().map(orderMapper::entityToDtoResponse).toList();

        return new PagingResult<>(
                ordersDtoResponse,
                ordersPage.getTotalPages(),
                ordersPage.getTotalElements(),
                ordersPage.getSize(),
                ordersPage.getNumber(),
                ordersPage.isEmpty(),
                ordersPage.isLast()
        );
    }

    public OrderDetailsDtoResponse findById(Long id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with this ID"));

        return this.orderMapper.entityToDetailDtoResponse(order);
    }

    public PagingResult<OrderDtoResponse> findByCustomerId(Long id, PaginationRequest request) {
        Customer customer = this.customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));


        Pageable pageable = PaginationUtils.getPageable(request);
        Page<Order> customerOrdersPage = this.orderRepository.findByCustomerId(id, pageable);
        List<OrderDtoResponse> ordersDtoResponse = customerOrdersPage.stream().map(orderMapper::entityToDtoResponse).toList();

        return new PagingResult<>(
                ordersDtoResponse,
                customerOrdersPage.getTotalPages(),
                customerOrdersPage.getTotalElements(),
                customerOrdersPage.getSize(),
                customerOrdersPage.getNumber(),
                customerOrdersPage.isEmpty(),
                customerOrdersPage.isLast()
        );
    }

    public OrderDtoResponse create(OrderDtoPostRequest orderDtoPostRequest) {
        Order order = this.orderMapper.dtoPostRequestToEntity(orderDtoPostRequest);
        order.setTotalAmount(new BigDecimal(0));
        Customer customer = this.customerRepository.findById(orderDtoPostRequest.customerId()).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        Store store = this.storeRepository.findById(orderDtoPostRequest.storeId()).orElseThrow(() -> new EntityNotFoundException("Store not found"));
        order.setCustomer(customer);
        order.setStore(store);


        for (OrderItemDtoPostRequest request: orderDtoPostRequest.orderItems()) {
            Product product = this.productRepository.findById(request.productId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(request.quantity());
            BigDecimal salePrice = product.getSalePrice();
            BigDecimal quantity = new BigDecimal(request.quantity());
            BigDecimal totalPrice = salePrice.multiply(quantity);


            orderItem.setSalePrice(salePrice);
            orderItem.setTotalPrice(totalPrice);

            order.getOrderItems().add(orderItem);
            order.setTotalAmount(order.getTotalAmount().add(totalPrice));
        }

        order.setStatus("PAID");
        return this.orderMapper.entityToDtoResponse(this.orderRepository.save(order));
    }
}
