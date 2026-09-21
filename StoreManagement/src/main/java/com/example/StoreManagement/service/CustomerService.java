package com.example.StoreManagement.service;

import com.example.StoreManagement.dtos.dtoRequest.CustomerDtoPutRequest;
import com.example.StoreManagement.mapstruct.mappers.AddressMapper;
import com.example.StoreManagement.mapstruct.mappers.CustomerMapper;
import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PaginationUtils;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.CustomerDtoPostRequest;
import com.example.StoreManagement.dtos.dtoResponse.CustomerDtoDetailResponse;
import com.example.StoreManagement.dtos.dtoResponse.CustomerDtoResponse;
import com.example.StoreManagement.model.entity.Customer;
import com.example.StoreManagement.repository.AddressRepository;
import com.example.StoreManagement.repository.CustomerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;


    public PagingResult<CustomerDtoResponse> findAll(PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);

        Page<Customer> customersPage = this.customerRepository.findByActiveTrue(pageable);
        List<CustomerDtoResponse> customerDtoList = customersPage.stream().map(customerMapper::entityToDtoResponse).toList();

        return new PagingResult<>(
                customerDtoList,
                customersPage.getTotalPages(),
                customersPage.getTotalElements(),
                customersPage.getSize(),
                customersPage.getSize(),
                customersPage.isEmpty(),
                customersPage.isLast()
        );
    }

    public CustomerDtoResponse findById(Long id) {
        Customer customer = this.customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));

        return this.customerMapper.entityToDtoResponse(customer);
    }

    public PagingResult<CustomerDtoResponse> findByName(String name, PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);

        Page<Customer> customersPage = this.customerRepository.findByNameAndActiveTrue(name, pageable);
        List<CustomerDtoResponse> customerDtoList = customersPage.stream().map(customerMapper::entityToDtoResponse).toList();

        return new PagingResult<>(
                customerDtoList,
                customersPage.getTotalPages(),
                customersPage.getTotalElements(),
                customersPage.getSize(),
                customersPage.getSize(),
                customersPage.isEmpty(),
                customersPage.isLast()
        );
    }

    public CustomerDtoResponse findByCpf(String cpf) {
        Customer customer = this.customerRepository.findByCpfAndActiveTrue(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return this.customerMapper.entityToDtoResponse(customer);
    }


    @Transactional
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

    @Transactional
    public CustomerDtoDetailResponse update(Long id, CustomerDtoPutRequest putRequest) {
        Customer customer = customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID"));

        Customer existingByCpf = this.customerRepository.findByCpf(putRequest.cpf());
        if (existingByCpf != null && !existingByCpf.getId().equals(customer.getId()))
           throw new EntityExistsException("This CPF already belongs to another Customer");

        this.checkEmailAvailability(customer, putRequest.email());

        customerMapper.updateEntity(putRequest, customer);
        return this.customerMapper.entityToDtoDetailResponse(customer);
    }

    @Transactional
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
