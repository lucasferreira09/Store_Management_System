package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.OrderItemMapper;
import com.example.StoreManagement.mapstruct.mappers.OrderMapper;
import com.example.StoreManagement.model.*;
import com.example.StoreManagement.model.dtoRequest.*;
import com.example.StoreManagement.model.dtoResponse.OrderCreationResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDetailsDtoResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDtoResponse;
import com.example.StoreManagement.model.entity.*;
import com.example.StoreManagement.model.entity.enums.OrderStatus;
import com.example.StoreManagement.model.entity.enums.StockMovementReason;
import com.example.StoreManagement.model.entity.enums.StockMovementType;
import com.example.StoreManagement.model.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final StockMovementHistoryRespository stockMovementHistoryRespository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final StripePaymentGateway stripePaymentGateway;


    public PagingResult<OrderDtoResponse> findAll(PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);
        Page<Order> ordersPage = orderRepository.findAll(pageable);
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
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with this ID"));

        return this.orderMapper.entityToDetailDtoResponse(order);
    }

    public OrderCreationResponse getCheckoutById(UUID id) {
        List<Order> orders = orderRepository.findByCheckoutId(id);
        if (orders == null || orders.isEmpty()) {
            throw new EntityNotFoundException("Orders not found for this checkoutId");
        }


        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Long> orderIds = new ArrayList<>();
        for  (Order order : orders) {
            totalAmount = totalAmount.add(order.getTotalAmount());
            orderIds.add(order.getId());
        }

        OrderCreationResponse orderCreationResponse = new OrderCreationResponse(
                orders.getFirst().getCustomer().getId(),
                id,
                totalAmount,
                orders.getFirst().getStatus()
        );

        return orderCreationResponse;
    }

    public List<OrderDtoResponse> findByCheckoutId(UUID checkoutId) {
        List<Order> orders = orderRepository.findByCheckoutId(checkoutId);

        return orderMapper.entitiesToDtoResponse(orders);
    }

    public PagingResult<OrderDtoResponse> findByCustomerId(Long id, PaginationRequest request) {
        Customer customer = customerRepository.findByIdAndActiveTrue(id)
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


    @Transactional
    public OrderCreationResponse create(OrderDtoPostRequest orderRequest) throws IllegalAccessException {
        if (orderRequest.customerId() == null)
            throw new IllegalAccessException("CustomerId must not be null");

        Customer customer = customerRepository.findById(orderRequest.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));


        if (orderRequest.orderItems() == null || orderRequest.orderItems().isEmpty())
            throw new IllegalAccessException("Order must have at least one item");

        this.validateDuplicatedItems(orderRequest.orderItems());

        List<Long> productsIds = orderRequest.orderItems().stream().map(OrderItemDtoPostRequest::productId).distinct().toList();
        List<Long> storeIds = orderRequest.orderItems().stream().map(OrderItemDtoPostRequest::storeId).distinct().toList();


        Map<Long, Product> productMap = productRepository.findAllById(productsIds).stream().collect(
                Collectors.toMap(
                        Product::getId,
                        product -> product,
                        (existingValue, newValue) -> existingValue
                ));


        // Fetch ALL inventories in ONE query | Load inventories in bulk
        List<Inventory> inventories = inventoryRepository.findByStoreIdInAndProductIdIn(storeIds, productsIds);

        // Pick ONLY what we need
        Map<InventoryKey, Inventory> inventoryMap = inventories.stream().collect(Collectors.toMap(
                inv -> new InventoryKey(inv.getStore().getId(), inv.getProduct().getId()),
                inv -> inv
        ));


        // Group items by store
        Map<Long, List<OrderItemDtoPostRequest>> itemsByStore = new HashMap<>();
        for (OrderItemDtoPostRequest item : orderRequest.orderItems()) {
            itemsByStore.computeIfAbsent(
                    item.storeId(),
                    k -> new ArrayList<>()
            ).add(item);
        }


        List<Order> createdOrders = new ArrayList<>();
        BigDecimal totalOrderAmount = BigDecimal.ZERO;
        UUID checkoutId = UUID.randomUUID();

        for (Map.Entry<Long, List<OrderItemDtoPostRequest>> entry : itemsByStore.entrySet()) {

            Long storeId = entry.getKey();
            List<OrderItemDtoPostRequest> itemsRequested = entry.getValue();

            Store store = storeRepository.findByIdAndActiveTrue(storeId)
                    .orElseThrow(() -> new EntityNotFoundException("Store not found with this ID"));


            // Mapper only address fields
            Order order = this.orderMapper.dtoPostRequestToEntity(orderRequest);
            order.setCustomer(customer);
            order.setStore(store);
            order.setStatus(OrderStatus.AWAITING_PAYMENT);

            BigDecimal orderAmount = BigDecimal.ZERO;
            List<OrderItem> orderItemList = new ArrayList<>();
            for (OrderItemDtoPostRequest item : itemsRequested) {
                order.setCheckoutId(checkoutId);

                Inventory inventory = inventoryMap.get(new InventoryKey(item.storeId(), item.productId()));
                Product product = productMap.get(item.productId());

                validateOrderItem(item, inventory, product);
                OrderItemCreated orderItemCreated = this.processOrderItem(new OrderItemContext(item, inventory, product, store, order));

                orderItemList.add(orderItemCreated.orderItem());
                orderAmount = orderAmount.add(orderItemCreated.totalPrice());
            }

            order.setOrderItems(orderItemList);
            order.setTotalAmount(orderAmount);
            order.setCreated_at(Instant.now());
            createdOrders.add(order);
            totalOrderAmount = totalOrderAmount.add(orderAmount);
        }

        this.orderRepository.saveAll(createdOrders);
        return new OrderCreationResponse(
                customer.getId(),
                checkoutId,
                totalOrderAmount,
                OrderStatus.AWAITING_PAYMENT
        );
    }

    private List<Long> filterOrderIds(List<Order> orders) {

        List<Long> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());
        return orderIds;
    }

    private void validateDuplicatedItems(List<OrderItemDtoPostRequest> orderItems) throws IllegalArgumentException {
        Set<InventoryKey> itemsKeys = new HashSet<>();

        for (OrderItemDtoPostRequest item : orderItems) {

            InventoryKey inventoryKey = new InventoryKey(item.storeId(), item.productId());

            if (!itemsKeys.add(inventoryKey))
                throw new IllegalArgumentException("Duplicated product in same Store. " +
                        "StoreID=" + item.storeId() + " | ProductID=" + item.productId());
        }
    }

    private void validateOrderItem(OrderItemDtoPostRequest item, Inventory inventory, Product product) {
        if (inventory == null || !inventory.isActive())
            throw new EntityNotFoundException("Inventory not found for StoreID: " + item.storeId() + " | ProductID: " + item.productId());


        if (item.quantity() <= 0)
            throw new IllegalArgumentException("Quantity must be greater than zero");

        if (item.quantity() > inventory.getQuantity())
            throw new IllegalArgumentException("Insufficient stock for ProductID: " + item.productId());


        if (product == null || !product.isActive())
            throw new EntityNotFoundException("Product not found or inactive");
    }

    private OrderItemCreated processOrderItem(OrderItemContext orderItemContext) {
        BigDecimal salePrice = orderItemContext.product().getSalePrice();
        BigDecimal quantity = BigDecimal.valueOf(orderItemContext.item().quantity());
        BigDecimal totalPrice = salePrice.multiply(quantity);

        processStockMovement(new StockMovementRequest(
                orderItemContext.inventory(),
                StockMovementType.OUT,
                StockMovementReason.SALE,
                orderItemContext.item().quantity(),
                orderItemContext.order().getCheckoutId().toString(),
                Instant.now(),
                null
        ));

        OrderItem orderItem = new OrderItem();
        orderItem.setSalePrice(salePrice);
        orderItem.setProduct(orderItemContext.product());
        orderItem.setStore(orderItemContext.store());
        orderItem.setQuantity(orderItemContext.item().quantity());
        orderItem.setOrder(orderItemContext.order());

        OrderItemCreated orderItemCreated = new OrderItemCreated(orderItem, totalPrice);
        return orderItemCreated;
    }

    public void changeOrderStatus(UUID orderCheckoutId, OrderStatus orderStatus) {
        List<Order> orders = this.orderRepository.findByCheckoutId(orderCheckoutId);

        orders.forEach(order -> {
                    order.setStatus(orderStatus);
                }
        );
        orderRepository.saveAll(orders);
    }

    public void processStockMovement(StockMovementRequest stockMovementRequest) {
        inventoryService.processMovement(stockMovementRequest);
    }


    @Transactional
    public void cancelOrder(UUID orderCheckoutId, String cancelMessage) {
        List<Order> orders = this.orderRepository.findByCheckoutId(orderCheckoutId);
        if (orders.isEmpty())
            throw new EntityNotFoundException("Order not found for checkout ID: " + orderCheckoutId);

        validateCancellation(orders);

        List<Long> orderIds = orders.stream().map(Order::getId).toList();
        List<OrderItem> orderItems = this.orderItemRepository.findAllByOrderIdIn(orderIds);

        restoreInventoryFromCancellation(orderItems, cancelMessage);

        orders.forEach(order ->
                order.setStatus(OrderStatus.CANCELLED)
        );
        orderRepository.saveAll(orders);
    }

    private void validateCancellation(List<Order> orders) {
        if (orders.stream().anyMatch(order ->
                order.getStatus() == (OrderStatus.CANCELLED) ||
                        order.getStatus() == OrderStatus.COMPLETED
        )) {
            throw new IllegalStateException("Cannot cancel Order when it's already cancelled or completed");
        }
    }

    private void restoreInventoryFromCancellation(List<OrderItem> orderItems, String cancelMessage) {
        List<Long> productIds = orderItems.stream().map(orderItem -> orderItem.getProduct().getId()).distinct().toList();
        List<Long> storeIds = orderItems.stream().map(orderItem -> orderItem.getStore().getId()).distinct().toList();

        List<Inventory> inventories = this.inventoryRepository.findByStoreIdInAndProductIdIn(storeIds, productIds);

        Map<InventoryKey, Inventory> inventoryMap = inventories.stream().collect(Collectors.toMap(
                inventory -> new InventoryKey(inventory.getStore().getId(), inventory.getProduct().getId()),
                Function.identity()
        ));

        for (OrderItem orderItem : orderItems) {
            Long storeId = orderItem.getStore().getId();
            Long productId = orderItem.getProduct().getId();

            Inventory inventory = inventoryMap.get(new InventoryKey(storeId, productId));
            if (inventory == null) {
                throw new EntityNotFoundException("Inventory not found for StoreID: " + storeId + " | ProductID: " + productId);
            }

            processStockMovement(new StockMovementRequest(
                    inventory,
                    StockMovementType.IN,
                    StockMovementReason.ORDER_CANCELLED,
                    orderItem.getQuantity(),
                    orderItem.getOrder().getId().toString(),
                    Instant.now(),
                    cancelMessage
            ));
        }

    }

}

