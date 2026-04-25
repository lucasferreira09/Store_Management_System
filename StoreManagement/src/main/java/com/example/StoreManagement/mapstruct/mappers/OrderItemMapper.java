package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoRequest.OrderItemDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.OrderItemDtoResponse;
import com.example.StoreManagement.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.id", target = "productId")
    OrderItemDtoResponse entityToDtoResponse(OrderItem orderItem);

    OrderItem dtoPostRequestToEntity(OrderItemDtoPostRequest orderItemDtoPostRequest);

    List<OrderItemDtoResponse> entitiesToDtoResponse(List<OrderItem> orderItems);
}
