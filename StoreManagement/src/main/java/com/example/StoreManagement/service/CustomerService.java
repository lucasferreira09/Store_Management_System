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
    public CustomerDtoResponse findById(Long id) {
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));

        return this.customerMapper.entityToDtoResponse(customer);
    }
    public List<CustomerDtoResponse> findByName(String name) {
        List<Customer> customers = this.customerRepository.findByName(name);

        return this.customerMapper.entitiesToDtoResponse(customers);
    }
    public CustomerDtoResponse findByCpf(String cpf) {
        Customer customer = this.customerRepository.findByCpf(cpf);

        if (customer == null) {
            throw new EntityNotFoundException("Customer not found");
        }

        return this.customerMapper.entityToDtoResponse(customer);
    }
    public CustomerDtoResponse create(CustomerDtoPostRequest dtoPostRequest) {
        if (this.customerRepository.existsByCpf(dtoPostRequest.cpf())) {
            throw new EntityExistsException("This customer already exists");
        }

        if (this.customerRepository.existsByEmail(dtoPostRequest.email())) {
            throw new EntityExistsException("A customer already exists with this E-Mail");
        }

        Customer customer = this.customerMapper.dtoPostRequestToEntity(dtoPostRequest);
        this.customerRepository.save(customer);

        return this.customerMapper.entityToDtoResponse(customer);
    }

    public CustomerDtoResponse update(Long id, CustomerDtoPostRequest dtoPostRequest) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));

        if (!dtoPostRequest.cpf().equals(customer.getCpf())) {
            if (this.customerRepository.existsByCpf(dtoPostRequest.cpf()))
                throw new EntityExistsException("Another customer already has this CPF");
        }

        Customer updatedCustomer = this.customerMapper.dtoPostRequestToEntity(dtoPostRequest);
        updatedCustomer.setId(id);

        this.customerRepository.save(updatedCustomer);
        return this.customerMapper.entityToDtoResponse(updatedCustomer);
    }
    public void delete(Long id) {
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this id"));

        this.customerRepository.delete(customer);
    }
}
