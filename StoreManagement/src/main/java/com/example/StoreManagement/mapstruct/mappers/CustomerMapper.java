package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.dtos.dtoRequest.CustomerDtoPostRequest;
import com.example.StoreManagement.dtos.dtoRequest.CustomerDtoPutRequest;
import com.example.StoreManagement.dtos.dtoResponse.CustomerDtoDetailResponse;
import com.example.StoreManagement.dtos.dtoResponse.CustomerDtoResponse;
import com.example.StoreManagement.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDtoResponse entityToDtoResponse(Customer customer);
    CustomerDtoDetailResponse entityToDtoDetailResponse(Customer customer);

    Customer dtoPostRequestToEntity(CustomerDtoPostRequest customerDtoPostRequest);

    List<CustomerDtoResponse> entitiesToDtoResponse(List<Customer> customers);

    void updateEntity(CustomerDtoPutRequest putRequest, @MappingTarget Customer customer);
}
