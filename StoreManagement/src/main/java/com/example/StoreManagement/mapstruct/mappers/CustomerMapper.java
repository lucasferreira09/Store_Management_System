package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoRequest.CustomerDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CustomerDtoDetailResponse;
import com.example.StoreManagement.model.dtoResponse.CustomerDtoResponse;
import com.example.StoreManagement.model.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDtoResponse entityToDtoResponse(Customer customer);
    CustomerDtoDetailResponse entityToDtoDetailResponse(Customer customer);

    Customer dtoPostRequestToEntity(CustomerDtoPostRequest customerDtoPostRequest);

    List<CustomerDtoResponse> entitiesToDtoResponse(List<Customer> customers);
}
