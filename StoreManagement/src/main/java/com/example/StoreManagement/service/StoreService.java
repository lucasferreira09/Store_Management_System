package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.StoreMapper;
import com.example.StoreManagement.model.dtoRequest.StoreDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.StoreDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.StoreDtoResponse;
import com.example.StoreManagement.model.entity.Address;
import com.example.StoreManagement.model.entity.Store;
import com.example.StoreManagement.model.repository.AddressRepository;
import com.example.StoreManagement.model.repository.StoreRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private StoreRepository storeRepository;
    private StoreMapper storeMapper;
    private AddressRepository addressRepository;

    public StoreService(StoreRepository storeRepository, StoreMapper storeMapper, AddressRepository addressRepository) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.addressRepository = addressRepository;
    }

    public List<StoreDtoResponse> findAll() {
        List<Store> stores = this.storeRepository.findAll();

        return this.storeMapper.entitiesToDtoResponse(stores);
    }

    public StoreDtoResponse findById(Long id) {
        Store store = this.storeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Store not found with this ID!"));

        return this.storeMapper.entityToDtoResponse(store);
    }

    public List<StoreDtoResponse> findByName(String name) {
        List<Store> stores = this.storeRepository.findByName(name);

        return this.storeMapper.entitiesToDtoResponse(stores);
    }

    public StoreDtoResponse findByCnpj(String cnpj) {
        Store store = this.storeRepository.findByCnpj(cnpj);

        if (store == null) {
            throw new EntityNotFoundException("Store not found with this CNPJ.");
        }

        return this.storeMapper.entityToDtoResponse(store);
    }

    public List<StoreDtoResponse> findByAddressId(Long id) {
        List<Store> stores = this.storeRepository.findByAddressId(id);

        return this.storeMapper.entitiesToDtoResponse(stores);
    }

    public StoreDtoResponse create(StoreDtoPostRequest storeDtoPostRequest) {

        if (this.storeRepository.existsByCnpj(storeDtoPostRequest.cnpj())) {
            throw new EntityExistsException("This Store already exists");
        }

        Store store = this.storeMapper.dtoPostRequestToEntity(storeDtoPostRequest);

        if (storeDtoPostRequest.addressID() != null) {
            Address address = this.addressRepository.findById(storeDtoPostRequest.addressID())
                    .orElseThrow(() -> new EntityNotFoundException("Address not found"));
            store.setAddress(address);
        } else {
            store.setAddress(null);
        }

        this.storeRepository.save(store);
        return this.storeMapper.entityToDtoResponse(store);
    }

    public StoreDtoResponse update(Long id, StoreDtoPutRequest dtoPutRequest) {
        Store store = this.storeRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("Store not found with this ID"));

        Store updatedStore = this.storeMapper.dtoPutRequestToEntity(dtoPutRequest);

        if (dtoPutRequest.addressID() != null) {
            Address address = this.addressRepository.findById(dtoPutRequest.addressID())
                    .orElseThrow(() -> new RuntimeException("Address not found"));
        } else {
            updatedStore.setAddress(null);
        }

        updatedStore.setId(store.getId());

        this.storeRepository.save(updatedStore);
        return this.storeMapper.entityToDtoResponse(updatedStore);
    }

    public StoreDtoResponse update(Long storeId, String cnpj) {
        Store store = this.storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityExistsException("Store not found with this ID"));


        Store existingStore = this.storeRepository.findByCnpj(cnpj);

        if (existingStore != null && !existingStore.getCnpj().equals(store.getCnpj())) {
            throw new EntityExistsException("This CNPJ already belongs to another Store");
        }

        store.setCnpj(cnpj);
        this.storeRepository.save(store);
        return this.storeMapper.entityToDtoResponse(store);
    }

    public StoreDtoResponse update(Long storeId, Long addressId) {
        Store store = this.storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityExistsException("Store not found with this ID"));

        Address address = this.addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityExistsException("Address not found with this ID"));


        store.setAddress(address);
        this.storeRepository.save(store);
        return this.storeMapper.entityToDtoResponse(store);
    }

    public void delete(Long id) {
        Store store = this.storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with this ID"));

        this.storeRepository.delete(store);
    }
}
