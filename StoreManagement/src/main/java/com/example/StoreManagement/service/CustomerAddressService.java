package com.example.StoreManagement.service;

import com.example.StoreManagement.Exception.ResourceNotFoundException;
import com.example.StoreManagement.mapstruct.mappers.CustomerAddressMapper;
import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PaginationUtils;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.CustomerAddressDtoPostRequest;
import com.example.StoreManagement.dtos.dtoRequest.CustomerAddressPutRequest;
import com.example.StoreManagement.dtos.dtoResponse.CustomerAddressDetailsResponse;
import com.example.StoreManagement.dtos.dtoResponse.CustomerAddressResponse;
import com.example.StoreManagement.model.entity.Address;
import com.example.StoreManagement.model.entity.Customer;
import com.example.StoreManagement.model.entity.CustomerAddress;
import com.example.StoreManagement.repository.AddressRepository;
import com.example.StoreManagement.repository.CustomerAddressRepository;
import com.example.StoreManagement.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerAddressService {

    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerAddressMapper customerAddressMapper;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private static final String entityName = "CustomerAddress";


    public PagingResult<CustomerAddressResponse> findAll(PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);
        Page<CustomerAddress> customerAddressesPage = customerAddressRepository.findAll(pageable);
        List<CustomerAddressResponse> customerAddressesDto = customerAddressesPage
                .stream()
                .map(customerAddressMapper::entityToDtoResponse).toList();

        return new PagingResult<>(
                customerAddressesDto,
                customerAddressesPage.getTotalPages(),
                customerAddressesPage.getTotalElements(),
                customerAddressesPage.getSize(),
                customerAddressesPage.getSize(),
                customerAddressesPage.isEmpty(),
                customerAddressesPage.isLast()
        );
    }

    public List<CustomerAddressDetailsResponse> findByCustomerId(Long id) {
        List<CustomerAddress> customerAddress = customerAddressRepository.findByCustomerId(id);

        if (customerAddress == null) {
            throw new ResourceNotFoundException(entityName, "ID:" + id.toString());
        }

        return customerAddressMapper.entitiesToDetailsResponse(customerAddress);
    }

    public List<CustomerAddressDetailsResponse> findByAddressId(Long id) {
        List<CustomerAddress> customerAddress = customerAddressRepository.findByAddressId(id);

        if (customerAddress == null) {
            throw new ResourceNotFoundException(entityName, "ID:" + id.toString());
        }

        return customerAddressMapper.entitiesToDetailsResponse(customerAddress);
    }

    @Transactional
    public CustomerAddressResponse create(CustomerAddressDtoPostRequest dtoPostRequest) {
        Customer customer = customerRepository.findById(dtoPostRequest.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "ID:" + dtoPostRequest.customerId().toString()));

        Address address = addressRepository.findById(dtoPostRequest.addressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "ID:" + dtoPostRequest.addressId().toString()));

        CustomerAddress customerAddress =
                customerAddressRepository.findByCustomerIdAndAddressId(dtoPostRequest.customerId(), dtoPostRequest.addressId());

        if (customerAddress == null) {
            customerAddress = new CustomerAddress();
            customerAddress.setCustomer(customer);
            customerAddress.setAddress(address);
        }

        customerAddressRepository.save(customerAddress);
        return customerAddressMapper.entityToDtoResponse(customerAddress);
    }

    @Transactional
    public CustomerAddressResponse update(Long customerId, Long addressId, CustomerAddressPutRequest putRequest) {
       Address address = addressRepository.findById(putRequest.addressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "ID:" + putRequest.addressId().toString()));

        CustomerAddress existCustomerAddress = this.customerAddressRepository.findByCustomerIdAndAddressId(customerId, addressId);
        if (existCustomerAddress == null)
            throw new ResourceNotFoundException(entityName, "CustomerID: %s AddressID: %s".formatted(customerId, addressId));

        customerAddressMapper.updateEntity(putRequest, existCustomerAddress);
        this.customerAddressRepository.save(existCustomerAddress);

        return this.customerAddressMapper.entityToDtoResponse(existCustomerAddress);
    }

    @Transactional
    public void delete(CustomerAddressDtoPostRequest dtoPostRequest) {

        CustomerAddress customerAddress = this.customerAddressRepository.findByCustomerIdAndAddressId(
                dtoPostRequest.customerId(),
                dtoPostRequest.addressId()
        );

        if (customerAddress == null)
            throw new ResourceNotFoundException(
                    entityName,
                    "CustomerID: %s AddressID: %s".formatted(dtoPostRequest.customerId(), dtoPostRequest.addressId())
            );


        this.customerAddressRepository.delete(customerAddress);
    }
}
