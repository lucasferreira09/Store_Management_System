package com.example.StoreManagement.service;

import com.example.StoreManagement.mapstruct.mappers.CategoryMapper;
import com.example.StoreManagement.mapstruct.mappers.ProductMapper;
import com.example.StoreManagement.model.dtoRequest.ProductDtoPostRequest;
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

    public List<ProductDtoResponse> getAllProducts() {
        List<Product> products = this.productRepository.findAll();

        return this.productMapper.entitiesToAllDtoResponse(products);
    }

    public ProductDtoResponse getProductById(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with this id!"));

        return this.productMapper.entityToDtoResponse(product);
    }

    public List<ProductDtoResponse> getProductByName(String name) {

        List<Product> product = this.productRepository.findByName(name);
        return this.productMapper.entitiesToAllDtoResponse(product);
    }

    public ProductDtoResponse getProductByBarcode(String barcode) {

        Product product = this.productRepository.findByBarcode(barcode);
        if (product == null) {
            throw new EntityNotFoundException("Product not found with this barcode!");
        }
        return this.productMapper.entityToDtoResponse(product);
    }

    public ProductDtoResponse addProduct(ProductDtoPostRequest dtoPost) {
        if (this.productRepository.existsByBarcode(dtoPost.barcode())) {
            throw new EntityExistsException("This product already exists");
        }

        Product product = this.productMapper.dtoPostRequestToEntity(dtoPost);
        Category category = this.categoryRepository.findById(
                dtoPost.categoryID()).orElseThrow(() -> new EntityNotFoundException("Error while creating product. Category not found!"));

        product.setCategory(category);

        Product savedProduct = this.productRepository.save(product);
        return this.productMapper.entityToDtoResponse(savedProduct);
    }
}
