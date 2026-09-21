package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.dtos.dtoRequest.StoreDtoPostRequest;
import com.example.StoreManagement.dtos.dtoRequest.StoreDtoPutRequest;
import com.example.StoreManagement.dtos.dtoResponse.StoreDtoDetailResponse;
import com.example.StoreManagement.dtos.dtoResponse.StoreDtoResponse;
import com.example.StoreManagement.model.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {


    @Mapping(source = "address.id", target = "addressId")
    StoreDtoResponse entityToDtoResponse(Store store);

    @Mapping(source = "address.id", target = "addressId")
    StoreDtoDetailResponse entityToDtoDetailResponse(Store store);

    @Mapping(source = "addressId", target = "address.id")
    Store dtoPostRequestToEntity(StoreDtoPostRequest storeDtoPostRequest);

    @Mapping(source = "addressId", target = "address.id")
    Store dtoPutRequestToEntity(StoreDtoPutRequest storeDtoPutRequest);

    List<StoreDtoResponse> entitiesToDtoResponse(List<Store> stores);

    void updateEntity(StoreDtoPutRequest putRequest, @MappingTarget Store store);
}
