package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoRequest.AddressDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.AddressDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.AddressDtoResponse;
import com.example.StoreManagement.model.entity.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    // @Mapping(source = "customer.id", target = "customerID")
    AddressDtoResponse entityToDtoResponse(Address address);

    // @Mapping(source = "customerID", target = "customer.id")
    Address dtoPostRequestToEntity(AddressDtoPostRequest addressDtoPostRequest);

    Address dtoPutRequestToEntity(AddressDtoPutRequest addressDtoPutRequest);

    List<AddressDtoResponse> entitiesToDtoResponse(List<Address> address);

}
