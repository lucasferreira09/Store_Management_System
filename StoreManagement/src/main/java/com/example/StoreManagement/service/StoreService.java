package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.StoreMapper;
import com.example.StoreManagement.model.dtoRequest.StoreDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.StoreDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.StoreDtoDetailResponse;
import com.example.StoreManagement.model.dtoResponse.StoreDtoResponse;
import com.example.StoreManagement.model.entity.Address;
import com.example.StoreManagement.model.entity.Store;
import com.example.StoreManagement.model.repository.AddressRepository;
import com.example.StoreManagement.model.repository.StoreRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final AddressRepository addressRepository;


    public List<StoreDtoResponse> findAll() {
        List<Store> stores = this.storeRepository.findByActiveTrue();

        return this.storeMapper.entitiesToDtoResponse(stores);
    }

    public StoreDtoResponse findById(Long id) {
        Store store = this.storeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with this ID."));

        return this.storeMapper.entityToDtoResponse(store);
    }

    public List<StoreDtoResponse> findByName(String name) {
        List<Store> stores = this.storeRepository.findByNameAndActiveTrue(name);

        return this.storeMapper.entitiesToDtoResponse(stores);
    }

    public StoreDtoResponse findByCnpj(String cnpj) {
        Store store = this.storeRepository.findByCnpjAndActiveTrue(cnpj);

        if (store == null) {
            throw new EntityNotFoundException("Store not found with this CNPJ.");
        }

        return this.storeMapper.entityToDtoResponse(store);
    }

    public List<StoreDtoResponse> findByAddressId(Long id) {
        List<Store> stores = this.storeRepository.findByAddressId(id);

        return this.storeMapper.entitiesToDtoResponse(stores);
    }



    public StoreDtoDetailResponse create(StoreDtoPostRequest dtoPostRequest) {
        if (dtoPostRequest.cnpj().length() != 13) 
            throw new RuntimeException("Invalid CNPJ");

        Store existingByCnpj = this.storeRepository.findByCnpj(dtoPostRequest.cnpj());

        if (existingByCnpj != null) {
            if (existingByCnpj.isActive())
                throw new EntityExistsException("This Store already exists");

            this.checkEmailAvailability(dtoPostRequest.email(), existingByCnpj.getId());
            return this.storeMapper.entityToDtoDetailResponse(reactivateStore(existingByCnpj, dtoPostRequest));
        }

        this.checkEmailAvailability(dtoPostRequest.email(), null);

        Store store = this.storeMapper.dtoPostRequestToEntity(dtoPostRequest);
        if (dtoPostRequest.addressID() != null) {
            Address address = this.addressRepository.findById(dtoPostRequest.addressID())
                    .orElseThrow(() -> new EntityNotFoundException("Address not found with this ID!"));
            store.setAddress(address);
        } else {
            store.setAddress(null);
        }

        this.storeRepository.save(store);
        return this.storeMapper.entityToDtoDetailResponse(store);
    }

    public StoreDtoDetailResponse update(Long id, StoreDtoPutRequest dtoPutRequest) {
        Store existingActiveStore = this.storeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with this ID"));


        this.checkCnpjAvailability(dtoPutRequest.cnpj(), existingActiveStore.getId());

        this.checkEmailAvailability(dtoPutRequest.email(), existingActiveStore.getId());

        Store updatedStore = this.storeMapper.dtoPutRequestToEntity(dtoPutRequest);
        if (dtoPutRequest.addressID() != null) {
            Address address = this.addressRepository.findById(dtoPutRequest.addressID())
                    .orElseThrow(() -> new RuntimeException("Address not found"));
            updatedStore.setAddress(address);
        } else {
            updatedStore.setAddress(null);
        }

        updatedStore.setId(existingActiveStore.getId());
        this.storeRepository.save(updatedStore);
        return this.storeMapper.entityToDtoDetailResponse(updatedStore);
    }

    public StoreDtoDetailResponse update(Long storeId, String cnpj) {
        Store existingActiveStore = this.storeRepository.findByIdAndActiveTrue(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with this ID"));


        this.checkCnpjAvailability(cnpj, existingActiveStore.getId());

        existingActiveStore.setCnpj(cnpj);
        this.storeRepository.save(existingActiveStore);
        return this.storeMapper.entityToDtoDetailResponse(existingActiveStore);
    }

    public StoreDtoResponse update(Long storeId, Long addressId) {
        Store store = this.storeRepository.findByIdAndActiveTrue(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with this ID"));


        Address address = this.addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityExistsException("Address not found with this ID"));


        store.setAddress(address);
        this.storeRepository.save(store);
        return this.storeMapper.entityToDtoResponse(store);
    }

    public void delete(Long id) {
        Store store = this.storeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with this ID."));


        this.storeRepository.delete(store);
    }

    private Store reactivateStore(Store store, StoreDtoPostRequest dtoPostRequest) {

        if (dtoPostRequest.addressID() != null) {
            Address address = this.addressRepository.findById(dtoPostRequest.addressID())
                    .orElseThrow(() -> new EntityNotFoundException("Address not found!"));
            store.setAddress(address);
        } else {
            store.setAddress(null);
        }

        store.setName(dtoPostRequest.name());
        store.setPhoneNumber(dtoPostRequest.phoneNumber());
        store.setEmail(dtoPostRequest.email());
        store.setActive(true);
        return this.storeRepository.save(store);
    }

    private void checkEmailAvailability(String email, Long currentStoreId) {
        Store existingByEmail = this.storeRepository.findByEmail(email);

        if (existingByEmail != null &&
                (currentStoreId == null || !existingByEmail.getId().equals(currentStoreId)))
            throw new EntityExistsException("Email already in use");
    }

    private void checkCnpjAvailability(String cnpj, Long currentStoreId) {

        Store existingByCnpj = this.storeRepository.findByCnpj(cnpj);
        if (existingByCnpj != null && !existingByCnpj.getId().equals(currentStoreId))
            throw new EntityExistsException("This CNPJ already belongs to another Store");
    }

}
