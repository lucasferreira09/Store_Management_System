package com.example.StoreManagement.service;

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
import jakarta.persistence.EntityNotFoundException;
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


    public PagingResult<CustomerAddressResponse> findAll(PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);
        Page<CustomerAddress> customerAddressesPage = this.customerAddressRepository.findAll(pageable);
        List<CustomerAddressResponse> customerAddressesDto = customerAddressesPage.stream().map(customerAddressMapper::entityToDtoResponse).toList();

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
        List<CustomerAddress> customerAddress = this.customerAddressRepository.findByCustomerId(id);

        if (customerAddress == null) {
            throw new EntityNotFoundException("Customer not found with this ID");
        }

        return customerAddressMapper.entitiesToDetailsResponse(customerAddress);
    }

    public List<CustomerAddressDetailsResponse> findByAddressId(Long id) {
        List<CustomerAddress> customerAddress = this.customerAddressRepository.findByAddressId(id);

        if (customerAddress == null) {
            throw new EntityNotFoundException("Address not found with this ID");
        }

        return customerAddressMapper.entitiesToDetailsResponse(customerAddress);
    }

    @Transactional
    public CustomerAddressResponse create(CustomerAddressDtoPostRequest dtoPostRequest) {
        Customer customer = this.customerRepository.findById(dtoPostRequest.customerID())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with this ID!"));

        Address address = this.addressRepository.findById(dtoPostRequest.addressID())
                .orElseThrow(() -> new EntityNotFoundException("Address not found with this ID!"));

        CustomerAddress customerAddress = this.customerAddressRepository.findByCustomerIdAndAddressId(dtoPostRequest.customerID(), dtoPostRequest.addressID());
        if (customerAddress == null) {
            customerAddress = new CustomerAddress();
            customerAddress.setCustomer(customer);
            customerAddress.setAddress(address);
        }

        this.customerAddressRepository.save(customerAddress);
        return this.customerAddressMapper.entityToDtoResponse(customerAddress);
    }

    @Transactional
    public CustomerAddressResponse update(Long customerId, Long addressId, CustomerAddressPutRequest putRequest) {
       Address address = this.addressRepository.findById(putRequest.addressID())
                .orElseThrow(() -> new EntityNotFoundException("Address not found with this ID"));


        CustomerAddress existCustomerAddress = this.customerAddressRepository.findByCustomerIdAndAddressId(customerId, addressId);
        if (existCustomerAddress == null)
            throw new EntityNotFoundException("CustomerAddress not found with this ID");

        customerAddressMapper.updateEntity(putRequest, existCustomerAddress);
        this.customerAddressRepository.save(existCustomerAddress);

        return this.customerAddressMapper.entityToDtoResponse(existCustomerAddress);
    }

    @Transactional
    public void delete(CustomerAddressDtoPostRequest dtoPostRequest) {

        CustomerAddress customerAddress = this.customerAddressRepository.findByCustomerIdAndAddressId(
                dtoPostRequest.customerID(),
                dtoPostRequest.addressID()
        );

        if (customerAddress == null)
            throw new EntityNotFoundException("CustomerAddress not found with this ID");


        this.customerAddressRepository.delete(customerAddress);
    }
}
