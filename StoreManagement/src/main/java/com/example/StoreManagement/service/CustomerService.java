package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.AddressMapper;
import com.example.StoreManagement.mapstruct.mappers.CustomerMapper;
import com.example.StoreManagement.model.dtoRequest.CustomerDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CustomerDtoResponse;
import com.example.StoreManagement.model.entity.Customer;
import com.example.StoreManagement.model.repository.AddressRepository;
import com.example.StoreManagement.model.repository.CustomerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private AddressMapper addressMapper;
    private AddressRepository addressRepository;
    private CustomerMapper customerMapper;
    private CustomerRepository customerRepository;

    public CustomerService(
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

    public List<CustomerDtoResponse> findAll() {
        List<Customer> customer = this.customerRepository.findAll();

        return this.customerMapper.entitiesToDtoResponse(customer);
    }

    public List<CustomerDtoResponse> findByName(String name) {
        List<Customer> customers = this.customerRepository.findByName(name);

        return this.customerMapper.entitiesToDtoResponse(customers);
    }

    public CustomerDtoResponse create(CustomerDtoPostRequest customerDtoPostRequest) {
        if (this.customerRepository.existsByCpf(customerDtoPostRequest.cpf())) {
            throw new EntityExistsException("This customer already exists");
        }

        if (this.customerRepository.existsByEmail(customerDtoPostRequest.email())) {
            throw new EntityExistsException("A customer already exists with this E-Mail");
        }

        Customer customer = this.customerMapper.dtoPostRequestToEntity(customerDtoPostRequest);
        this.customerRepository.save(customer);

        return this.customerMapper.entityToDtoResponse(customer);
    }

    public CustomerDtoResponse update(Long id, CustomerDtoPostRequest customerDtoPostRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this id"));


        Customer updatedCustomer = this.customerMapper.dtoPostRequestToEntity(customerDtoPostRequest);
        updatedCustomer.setId(customer.getId());

        this.customerRepository.save(updatedCustomer);
        return this.customerMapper.entityToDtoResponse(updatedCustomer);
    }

    public void delete(Long id) {
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this id"));

        this.customerRepository.delete(customer);
    }
}
