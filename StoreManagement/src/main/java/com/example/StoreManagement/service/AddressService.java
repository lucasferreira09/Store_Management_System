package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.AddressMapper;
import com.example.StoreManagement.mapstruct.mappers.CustomerMapper;
import com.example.StoreManagement.model.dtoRequest.AddressDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.AddressDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.AddressDtoResponse;
import com.example.StoreManagement.model.entity.Address;
import com.example.StoreManagement.model.entity.Category;
import com.example.StoreManagement.model.entity.Customer;
import com.example.StoreManagement.model.repository.AddressRepository;
import com.example.StoreManagement.model.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private AddressMapper addressMapper;
    private AddressRepository addressRepository;
    private CustomerMapper customerMapper;
    private CustomerRepository customerRepository;

    public AddressService(
           AddressMapper addressMapper,
           AddressRepository addressRepository,
           CustomerMapper customerMapper,
           CustomerRepository customerRepository
    ) {
        this.addressMapper = addressMapper;
        this.addressRepository = addressRepository;
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    public List<AddressDtoResponse> findAll() {
        List<Address> address = this.addressRepository.findAll();

        return this.addressMapper.entitiesToDtoResponse(address);
    }

    public AddressDtoResponse findById(Long id) {
        Address address = this.addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with this id!"));

        return this.addressMapper.entityToDtoResponse(address);

    }
    public List<AddressDtoResponse> findByCustomerId(Long id) {
        List<Address> addresses = this.addressRepository.findByCustomerId(id);

        return this.addressMapper.entitiesToDtoResponse(addresses);
    }

    public AddressDtoResponse create(AddressDtoPostRequest addressDtoPostRequest) {
        Customer customer = this.customerRepository.findById(addressDtoPostRequest.customerID())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));

        Address address = this.addressMapper.dtoPostRequestToEntity(addressDtoPostRequest);
        address.setCustomer(customer);
        this.addressRepository.save(address);

        return this.addressMapper.entityToDtoResponse(address);
    }

    public AddressDtoResponse update(Long id, AddressDtoPutRequest addressDtoPutRequest) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with this id"));

        Address updatedAddress = this.addressMapper.dtoPutRequestToEntity(addressDtoPutRequest);
        updatedAddress.setId(address.getId());
        updatedAddress.setCustomer(address.getCustomer());
        this.addressRepository.save(updatedAddress);

        return this.addressMapper.entityToDtoResponse(updatedAddress);
    }

    public void delete(Long id) {
        Address address = this.addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with this id"));

        this.addressRepository.delete(address);
    }
}
