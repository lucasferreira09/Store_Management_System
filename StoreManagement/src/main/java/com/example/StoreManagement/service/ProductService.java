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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;


    public List<ProductDtoResponse> findAll() {

        List<Product> products = this.productRepository.findByActiveTrueAndCategoryActiveTrue();
        return this.productMapper.entitiesToAllDtoResponse(products);
    }

    public ProductDtoResponse findById(Long id) {
        Product product = this.productRepository.findByIdAndActiveTrueAndCategoryActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this ID."));

        return this.productMapper.entityToDtoResponse(product);
    }

    public List<ProductDtoResponse> findByName(String name) {

        List<Product> product = this.productRepository.findByNameAndActiveTrueAndCategoryActiveTrue(name);
        return this.productMapper.entitiesToAllDtoResponse(product);
    }

    public List<ProductDtoResponse> findByCategoryId(Long id) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this ID."));

        return this.productMapper.entitiesToAllDtoResponse(this.productRepository.findByCategoryIdAndActiveTrue(id));
    }

    public ProductDtoResponse findByBarcode(String barcode) {

        Product product = this.productRepository.findByBarcodeAndActiveTrue(barcode);
        if (product == null)
            throw new EntityNotFoundException("Product not found with this barcode!");

        return this.productMapper.entityToDtoResponse(product);
    }

    public ProductDtoResponse create(ProductDtoPostRequest dtoPost) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(dtoPost.categoryID())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this ID."));

        Product existingByBarcode = this.productRepository.findByBarcode(dtoPost.barcode());
        if (existingByBarcode != null) {
            if (existingByBarcode.isActive())
                throw new EntityExistsException("This product already exists");

            return this.productMapper.entityToDtoResponse(this.reactivateProduct(existingByBarcode, dtoPost));
        }

        Product product = this.productMapper.dtoPostRequestToEntity(dtoPost);
        product.setCategory(category);
        Product savedProduct = this.productRepository.save(product);
        return this.productMapper.entityToDtoResponse(savedProduct);
    }

    public ProductDtoResponse update(Long id, ProductDtoPostRequest dtoPostRequest) {
        Product product = this.productRepository.findByIdAndActiveTrueAndCategoryActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this id"));

        Category category = this.categoryRepository.findByIdAndActiveTrue(dtoPostRequest.categoryID())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this ID."));


        Product existingByBarcode = this.productRepository.findByBarcode(dtoPostRequest.barcode());
        if (existingByBarcode != null && !existingByBarcode.getId().equals(product.getId()))
            throw new EntityExistsException("This barcode already belongs to another Product");


        Product updatedProduct = this.productMapper.dtoPostRequestToEntity(dtoPostRequest);
        updatedProduct.setId(id);
        updatedProduct.setCategory(category);
        productRepository.save(updatedProduct);
        return this.productMapper.entityToDtoResponse(updatedProduct);
    }

    public ProductDtoResponse update(Long productId, Long categoryId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this ID"));

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with this ID"));

        product.setCategory(category);
        productRepository.save(product);

        return this.productMapper.entityToDtoResponse(product);
    }

    public void delete(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with this ID"));

        this.productRepository.delete(product);
    }

    private Product reactivateProduct(Product product, ProductDtoPostRequest dtoPostRequest) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(dtoPostRequest.categoryID())
                .orElseThrow(() -> new EntityNotFoundException("Category not found!"));

        product.setActive(true);
        product.setName(dtoPostRequest.name());
        product.setDescription(dtoPostRequest.description());
        product.setPhoto(dtoPostRequest.photo());
        product.setSalePrice(dtoPostRequest.salePrice());
        product.setCostPrice(dtoPostRequest.costPrice());
        product.setCategory(category);
        return this.productRepository.save(product);
    }
}
