package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.AddressMapper;
import com.example.StoreManagement.model.dtoRequest.AddressDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.AddressDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.AddressDtoResponse;
import com.example.StoreManagement.model.entity.Address;
import com.example.StoreManagement.model.repository.AddressRepository;
import com.example.StoreManagement.model.repository.CustomerAddressRepository;
import com.example.StoreManagement.model.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final StoreRepository storeRepository;


    public List<AddressDtoResponse> findAll() {
        List<Address> address = this.addressRepository.findAll();

        return this.addressMapper.entitiesToDtoResponse(address);
    }

    public AddressDtoResponse findById(Long id) {
        Address address = this.addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with this id!"));

        return this.addressMapper.entityToDtoResponse(address);
    }

    public AddressDtoResponse create(AddressDtoPostRequest addressDtoPostRequest) {
        Address address = this.addressMapper.dtoPostRequestToEntity(addressDtoPostRequest);
        this.addressRepository.save(address);

        return this.addressMapper.entityToDtoResponse(address);
    }

    public AddressDtoResponse update(Long id, AddressDtoPutRequest dtoPutRequest) {
        Address address = this.addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with this id!"));

        Address updatedAddress = this.addressMapper.dtoPutRequestToEntity(dtoPutRequest);
        updatedAddress.setId(address.getId());
        this.addressRepository.save(updatedAddress);

        return this.addressMapper.entityToDtoResponse(updatedAddress);
    }

    public void delete(Long id) {
        Address address = this.addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with this id"));

        if (this.storeRepository.existsByAddressId(id) || this.customerAddressRepository.existsByAddressId(id))
            throw new IllegalStateException("Address already in use");

        this.addressRepository.delete(address);
    }
}
