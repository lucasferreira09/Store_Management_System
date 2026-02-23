package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.CategoryMapper;
import com.example.StoreManagement.mapstruct.mappers.ProductMapper;
import com.example.StoreManagement.model.dtoRequest.ProductDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.ProductDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.ProductDtoResponse;
import com.example.StoreManagement.model.entity.Category;
import com.example.StoreManagement.model.entity.Product;
import com.example.StoreManagement.model.repository.CategoryRepository;
import com.example.StoreManagement.model.repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper,
            CategoryMapper categoryMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDtoResponse> findAll() {
        List<Product> products = this.productRepository.findAll();

        return this.productMapper.entitiesToAllDtoResponse(products);
    }

    public ProductDtoResponse findById(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with this id!"));

        return this.productMapper.entityToDtoResponse(product);
    }

    public List<ProductDtoResponse> findByName(String name) {

        List<Product> product = this.productRepository.findByName(name);
        return this.productMapper.entitiesToAllDtoResponse(product);
    }

    public List<ProductDtoResponse> findByCategoryId(Long categoryId) {
        return this.productMapper.entitiesToAllDtoResponse(this.productRepository.findByCategoryId(categoryId));
    }

    public ProductDtoResponse findByBarcode(String barcode) {

        Product product = this.productRepository.findByBarcode(barcode);
        if (product == null) {
            throw new EntityNotFoundException("Product not found with this barcode!");
        }
        return this.productMapper.entityToDtoResponse(product);
    }

    public ProductDtoResponse create(ProductDtoPostRequest dtoPost) {
        if (this.productRepository.existsByBarcode(dtoPost.barcode())) {
            throw new EntityExistsException("This product already exists");
        }

        Product product = this.productMapper.dtoPostRequestToEntity(dtoPost);
        Category category = this.categoryRepository.findById(
                dtoPost.categoryID()).orElseThrow(() -> new EntityNotFoundException("Category not found!"));

        product.setCategory(category);
        Product savedProduct = this.productRepository.save(product);

        return this.productMapper.entityToDtoResponse(savedProduct);
    }

    public ProductDtoResponse update(Long id, ProductDtoPostRequest dtoPostRequest) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this id"));

        Category category = this.categoryRepository.findById(dtoPostRequest.categoryID())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (!dtoPostRequest.barcode().equals(product.getBarcode())) {
            if (this.productRepository.existsByBarcode(dtoPostRequest.barcode()))
                throw new EntityExistsException("Another product already has this barcode");
        }

        Product updatedProduct = this.productMapper.dtoPostRequestToEntity(dtoPostRequest);
        updatedProduct.setId(id);
        updatedProduct.setCategory(category);
        productRepository.save(updatedProduct);

        return this.productMapper.entityToDtoResponse(updatedProduct);
    }

    public ProductDtoResponse update(Long productId, Long categoryId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this id"));

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this id"));

        product.setCategory(category);
        productRepository.save(product);

        return this.productMapper.entityToDtoResponse(product);
    }

    public void delete(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this id"));

        this.productRepository.delete(product);
    }
}
