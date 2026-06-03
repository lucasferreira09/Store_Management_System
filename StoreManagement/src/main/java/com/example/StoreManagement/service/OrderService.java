package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.OrderItemMapper;
import com.example.StoreManagement.mapstruct.mappers.OrderMapper;
import com.example.StoreManagement.model.*;
import com.example.StoreManagement.model.dtoRequest.*;
import com.example.StoreManagement.model.dtoResponse.OrderDetailsDtoResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDtoResponse;
import com.example.StoreManagement.model.entity.*;
import com.example.StoreManagement.model.entity.enums.StockMovementReason;
import com.example.StoreManagement.model.entity.enums.StockMovementType;
import com.example.StoreManagement.model.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private OrderItemRepository orderItemRepository;
    private OrderRepository orderRepository;
    private StoreRepository storeRepository;
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private InventoryRepository inventoryRepository;
    private InventoryService inventoryService;
    private StockMovementHistoryRespository stockMovementHistoryRespository;
    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;

    public OrderService(
            OrderItemRepository orderItemRepository,
            OrderRepository orderRepository,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            InventoryRepository inventoryRepository,
            InventoryService inventoryService,
            StockMovementHistoryRespository stockMovementHistoryRespository,
            OrderMapper orderMapper,
            OrderItemMapper orderItemMapper
    ) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
        this.stockMovementHistoryRespository = stockMovementHistoryRespository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
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


    @Transactional
    public void create(OrderDtoPostRequest orderRequest) throws IllegalAccessException {
        Customer customer = this.customerRepository.findById(orderRequest.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));

        if (orderRequest.orderItems() == null || orderRequest.orderItems().isEmpty())
            throw new IllegalAccessException("Order must have at least one item");

        //this.validateDuplicatedItems(orderRequest.orderItems());

        List<Long> productsIds = orderRequest.orderItems().stream().map(OrderItemDtoPostRequest::productId).distinct().toList();
        List<Long> storeIds = orderRequest.orderItems().stream().map(OrderItemDtoPostRequest::storeId).distinct().toList();

        Map<Long, Product> productMap = this.productRepository.findAllById(productsIds).stream().collect(
                Collectors.toMap(
                        Product::getId,product -> product, (existingValue, newValue) -> existingValue
                ));


        // Fetch ALL inventories in ONE query | Load inventories in bulk
        List<Inventory> inventories = this.inventoryRepository.findByStoreIdInAndProductIdIn(storeIds, productsIds);

        // Pick ONLY what we need
        Map<InventoryKey, Inventory> inventoryMap = inventories.stream().collect(Collectors.toMap(
                inv -> new InventoryKey(inv.getStore().getId(), inv.getProduct().getId()),
                inv -> inv
        ));


        // Group items by store
        Map<Long, List<OrderItemDtoPostRequest>> itemsByStore = new HashMap<>();
        for (OrderItemDtoPostRequest item : orderRequest.orderItems()) {

            itemsByStore.computeIfAbsent(item.storeId(), k -> new ArrayList<>())
                    .add(item);
        }

        // create orders by store
        List<Order> createdOrders = new ArrayList<>();
        for (Map.Entry<Long, List<OrderItemDtoPostRequest>> entry : itemsByStore.entrySet()) {

            Long storeId = entry.getKey();
            List<OrderItemDtoPostRequest> itemsRequested = entry.getValue();

            Store store = this.storeRepository.findByIdAndActiveTrue(storeId)
                    .orElseThrow(() -> new EntityNotFoundException("Store not found with this ID"));


            // Mapper only address fields
            Order order = this.orderMapper.dtoPostRequestToEntity(orderRequest);
            order.setCustomer(customer);
            order.setStore(store);
            order.setStatus("DONE");

            BigDecimal totalAmount = BigDecimal.ZERO;
            List<OrderItem> orderItemList = new ArrayList<>();
            for (OrderItemDtoPostRequest item : itemsRequested) {

                Inventory inventory = inventoryMap.get(new InventoryKey(item.storeId(), item.productId()));
                Product product = productMap.get(item.productId());

                this.validateOrderItem(item, inventory, product);

                OrderItemContext orderItemContext = new OrderItemContext(item, inventory, product, store, order);
                OrderItemCreated orderItemCreated = this.processOrderItem(orderItemContext);

                orderItemList.add(orderItemCreated.orderItem());
                totalAmount = totalAmount.add(orderItemCreated.totalPrice());
            }

            order.setOrderItems(orderItemList);
            order.setTotalAmount(totalAmount);
            createdOrders.add(order);
        }

        this.orderRepository.saveAll(createdOrders);
    }

    private void createOrderByStore(Map<Long, List<OrderItemDtoPostRequest>> itemsByStore) {

    }

    private void validateDuplicatedItems(List<OrderItemDtoPostRequest> orderItems) throws IllegalAccessException {
        Set<InventoryKey> itemsKeys = new HashSet<>();

        for (OrderItemDtoPostRequest item : orderItems) {

            InventoryKey inventoryKey = new InventoryKey(item.storeId(), item.productId());

            if (!itemsKeys.add(inventoryKey))
                throw new IllegalAccessException("Duplicated product in same Store. " +
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


        InventoryMovementRequest inventoryMovementRequest = new InventoryMovementRequest(orderItemContext.inventory().getId(),
                StockMovementType.OUT,
                StockMovementReason.SALE,
                orderItemContext.item().quantity(),
                null
        );

        this.inventoryService.processMovement(inventoryMovementRequest);

        OrderItem orderItem = new OrderItem();
        orderItem.setSalePrice(salePrice);
        orderItem.setProduct(orderItemContext.product());
        orderItem.setStore(orderItemContext.store());
        orderItem.setQuantity(orderItemContext.item().quantity());
        orderItem.setOrder(orderItemContext.order());

        OrderItemCreated orderItemCreated = new OrderItemCreated(orderItem, totalPrice);
        return orderItemCreated;

    }

}

