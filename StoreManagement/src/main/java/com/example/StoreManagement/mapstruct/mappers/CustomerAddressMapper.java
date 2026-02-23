package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoRequest.CustomerAddressDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CustomerAddressDetailsResponse;
import com.example.StoreManagement.model.dtoResponse.CustomerAddressResponse;
import com.example.StoreManagement.model.entity.CustomerAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CustomerMapper.class)
public interface CustomerAddressMapper {

    CustomerAddress dtoPostToEntity(CustomerAddressDtoPostRequest customerAddressDtoPostRequest);

    @Mapping(source = "customer.id", target = "customerID")
    @Mapping(source = "address.id", target = "addressID")
    CustomerAddressResponse entityToDtoResponse(CustomerAddress customerAddress);

    @Mapping(source = "customer.name", target = "name")
    CustomerAddressDetailsResponse entityToDetailsResponse(CustomerAddress customerAddress);
    List<CustomerAddressResponse> entitiesToDtoResponse(List<CustomerAddress> customerAddresses);
    
    @Mapping(source = "customer.name", target = "name")
    List<CustomerAddressDetailsResponse> entitiesToDetailsResponse(List<CustomerAddress> customerAddresses);
}
