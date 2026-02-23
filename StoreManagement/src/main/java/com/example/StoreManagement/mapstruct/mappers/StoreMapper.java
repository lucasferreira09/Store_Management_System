package com.example.StoreManagement.mapstruct.mappers;

import com.example.StoreManagement.model.dtoRequest.StoreDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.StoreDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.StoreDtoResponse;
import com.example.StoreManagement.model.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {


    @Mapping(source = "address.id", target = "addressID")
    StoreDtoResponse entityToDtoResponse(Store store);

    @Mapping(source = "addressID", target = "address.id")
    Store dtoPostRequestToEntity(StoreDtoPostRequest storeDtoPostRequest);

    @Mapping(source = "addressID", target = "address.id")
    Store dtoPutRequestToEntity(StoreDtoPutRequest storeDtoPutRequest);

    List<StoreDtoResponse> entitiesToDtoResponse(List<Store> stores);
}
