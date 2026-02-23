package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.CustomerAddressMapper;
import com.example.StoreManagement.model.dtoRequest.CustomerAddressDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.CustomerAddressPutRequest;
import com.example.StoreManagement.model.dtoResponse.CustomerAddressDetailsResponse;
import com.example.StoreManagement.model.dtoResponse.CustomerAddressResponse;
import com.example.StoreManagement.model.entity.Address;
import com.example.StoreManagement.model.entity.Customer;
import com.example.StoreManagement.model.entity.CustomerAddress;
import com.example.StoreManagement.model.repository.AddressRepository;
import com.example.StoreManagement.model.repository.CustomerAddressRepository;
import com.example.StoreManagement.model.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerAddressService {

    private CustomerAddressRepository customerAddressRepository;
    private CustomerAddressMapper customerAddressMapper;
    private CustomerRepository customerRepository;
    private AddressRepository addressRepository;

    public CustomerAddressService(
            CustomerAddressRepository customerAddressRepository,
            CustomerAddressMapper customerAddressMapper,
            CustomerRepository customerRepository,
            AddressRepository addressRepository
    ) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerAddressMapper = customerAddressMapper;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    public List<CustomerAddressResponse> findAll() {
        List<CustomerAddress> customerAddresses = this.customerAddressRepository.findAll();

        return this.customerAddressMapper.entitiesToDtoResponse(customerAddresses);
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

    public CustomerAddressResponse update(Long customerId, Long addressId, CustomerAddressPutRequest dtoPutRequest) {
       Address address = this.addressRepository.findById(dtoPutRequest.addressID())
                .orElseThrow(() -> new EntityNotFoundException("Address not found with this ID"));


        CustomerAddress putCustomerAddress = this.customerAddressRepository.findByCustomerIdAndAddressId(customerId, addressId);
        if (putCustomerAddress == null) {
            throw new EntityNotFoundException("CustomerAddress not found with this ID");
        }

        putCustomerAddress.setAddress(address);
        this.customerAddressRepository.save(putCustomerAddress);

        return this.customerAddressMapper.entityToDtoResponse(putCustomerAddress);
    }

    public void delete(CustomerAddressDtoPostRequest dtoPostRequest) {

        CustomerAddress customerAddress = this.customerAddressRepository.findByCustomerIdAndAddressId(
                dtoPostRequest.customerID(), dtoPostRequest.addressID());

        if (customerAddress == null) {
            throw new EntityNotFoundException("Customer and address not found with this ID");
        }

        this.customerAddressRepository.delete(customerAddress);
    }
}
