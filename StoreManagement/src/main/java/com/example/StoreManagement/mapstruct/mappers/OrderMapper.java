package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoRequest.OrderDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.OrderDetailsDtoResponse;
import com.example.StoreManagement.model.dtoResponse.OrderDtoResponse;
import com.example.StoreManagement.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    OrderDtoResponse entityToDtoResponse(Order order);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "store.id", target = "storeId")
    OrderDetailsDtoResponse entityToDetailDtoResponse(Order order);

    @Mapping(target = "orderItems", ignore = true)
    Order dtoPostRequestToEntity(OrderDtoPostRequest orderDtoPostRequest);

    List<OrderDtoResponse> entitiesToDtoResponse(List<Order> orders);

    List<OrderDetailsDtoResponse> entitiesToDetailsDtoResponse(List<Order> orders);
}