package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.AddressMapper;
import com.example.StoreManagement.mapstruct.mappers.CustomerMapper;
import com.example.StoreManagement.model.dtoRequest.CustomerDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CustomerDtoDetailResponse;
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
        List<Customer> customer = this.customerRepository.findByActiveTrue();

        return this.customerMapper.entitiesToDtoResponse(customer);
    }

    public CustomerDtoResponse findById(Long id) {
        Customer customer = this.customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));

        return this.customerMapper.entityToDtoResponse(customer);
    }

    public List<CustomerDtoResponse> findByName(String name) {
        List<Customer> customers = this.customerRepository.findByNameAndActiveTrue(name);

        return this.customerMapper.entitiesToDtoResponse(customers);
    }

    public CustomerDtoResponse findByCpf(String cpf) {
        Customer customer = this.customerRepository.findByCpfAndActiveTrue(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return this.customerMapper.entityToDtoResponse(customer);
    }

    public CustomerDtoDetailResponse create(CustomerDtoPostRequest dtoPostRequest) {
        Customer existingByCpf = this.customerRepository.findByCpf(dtoPostRequest.cpf());

        if (existingByCpf != null) {
            if (existingByCpf.isActive())
                throw new EntityExistsException("Customer already exists");

            this.checkEmailAvailability(existingByCpf, dtoPostRequest.email());
            return this.customerMapper.entityToDtoDetailResponse(this.reactivateCustomer(existingByCpf, dtoPostRequest));
        }

        Customer newCustomer = this.customerMapper.dtoPostRequestToEntity(dtoPostRequest);

        this.checkEmailAvailability(newCustomer, dtoPostRequest.email());
        this.customerRepository.save(newCustomer);
        return this.customerMapper.entityToDtoDetailResponse(newCustomer);
    }

    public CustomerDtoDetailResponse update(Long id, CustomerDtoPostRequest dtoPostRequest) {
        Customer customer = customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));

        Customer existingByCpf = this.customerRepository.findByCpf(dtoPostRequest.cpf());
        if (existingByCpf != null && !existingByCpf.getId().equals(customer.getId()))
           throw new EntityExistsException("This CPF already belongs to another Customer");


        this.checkEmailAvailability(customer, dtoPostRequest.email());
        Customer updatedCustomer = this.customerMapper.dtoPostRequestToEntity(dtoPostRequest);
        updatedCustomer.setId(customer.getId());
        this.customerRepository.save(updatedCustomer);
        return this.customerMapper.entityToDtoDetailResponse(updatedCustomer);
    }

    public void delete(Long id) {
        Customer customer = this.customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));

        this.customerRepository.delete(customer);
    }

    private Customer reactivateCustomer(Customer customer, CustomerDtoPostRequest dtoPostRequest) {
        customer.setActive(true);
        customer.setName(dtoPostRequest.name());
        customer.setCpf(dtoPostRequest.cpf());
        customer.setPhoneNumber(dtoPostRequest.phoneNumber());
        customer.setEmail(dtoPostRequest.email());
        return this.customerRepository.save(customer);
    }

    private void checkEmailAvailability(Customer customer, String email) {
        Customer existingByEmail = this.customerRepository.findByEmail(email);


        if (existingByEmail != null &&
                (customer != null || !existingByEmail.getId().equals(customer.getId()))
        )
            throw new EntityExistsException("Email already in use");

    }
}
