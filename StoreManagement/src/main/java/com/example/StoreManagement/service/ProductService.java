package com.example.StoreManagement.service;

import com.example.StoreManagement.Exception.ResourceAlreadyInUseException;
import com.example.StoreManagement.Exception.ResourceNotFoundException;
import com.example.StoreManagement.dtos.dtoRequest.ProductDtoPutRequest;
import com.example.StoreManagement.mapstruct.mappers.ProductMapper;
import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PaginationUtils;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.ProductDtoPostRequest;
import com.example.StoreManagement.dtos.dtoResponse.ProductDtoResponse;
import com.example.StoreManagement.model.entity.Category;
import com.example.StoreManagement.model.entity.Product;
import com.example.StoreManagement.repository.CategoryRepository;
import com.example.StoreManagement.repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private static final String entityName = "Product";


    public PagingResult<ProductDtoResponse> findAll(PaginationRequest request) {
        Pageable pageable = PaginationUtils.getPageable(request);

        Page<Product> productsPage = productRepository.findByActiveTrueAndCategoryActiveTrue(pageable);
        List<ProductDtoResponse> productDtoList = productsPage.stream().map(productMapper::entityToDtoResponse).toList();

        return new PagingResult<>(
                productDtoList,
                productsPage.getTotalPages(),
                productsPage.getTotalElements(),
                productsPage.getSize(),
                productsPage.getNumber(),
                productsPage.isEmpty(),
                productsPage.isLast()
        );
    }

    public ProductDtoResponse findById(Long id) {
        Product product = this.productRepository.findByIdAndActiveTrueAndCategoryActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "ID:" + id.toString()));

        return this.productMapper.entityToDtoResponse(product);
    }

    public List<ProductDtoResponse> findByName(String name) {

        List<Product> product = this.productRepository.findByNameAndActiveTrueAndCategoryActiveTrue(name);
        return this.productMapper.entitiesToAllDtoResponse(product);
    }

    public List<ProductDtoResponse> findByCategoryId(Long id) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID:" + id.toString()));

        return this.productMapper.entitiesToAllDtoResponse(this.productRepository.findByCategoryIdAndActiveTrue(id));
    }

    public ProductDtoResponse findByBarcode(String barcode) {

        Product product = this.productRepository.findByBarcodeAndActiveTrue(barcode);
        if (product == null)
            throw new ResourceNotFoundException(entityName, "Barcode:" + barcode.toString());

        return this.productMapper.entityToDtoResponse(product);
    }

    public ProductDtoResponse create(ProductDtoPostRequest dtoPost) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(dtoPost.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID:" + dtoPost.categoryId().toString()));

        Product existingByBarcode = this.productRepository.findByBarcode(dtoPost.barcode());
        if (existingByBarcode != null) {
            if (existingByBarcode.isActive())
                throw new ResourceAlreadyInUseException(entityName, "Barcode:" + dtoPost.barcode());

            Product reactivatedProduct = this.reactivateProduct(existingByBarcode, dtoPost);
            return this.productMapper.entityToDtoResponse(reactivatedProduct);
        }

        Product product = this.productMapper.dtoPostRequestToEntity(dtoPost);
        product.setCategory(category);
        Product savedProduct = this.productRepository.save(product);
        return this.productMapper.entityToDtoResponse(savedProduct);
    }

    public ProductDtoResponse update(Long id, ProductDtoPutRequest putRequest) {
        Product product = this.productRepository.findByIdAndActiveTrueAndCategoryActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "ID:" + id.toString()));

        Category category = this.categoryRepository.findByIdAndActiveTrue(putRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID:" + putRequest.categoryId().toString()));

        Product existingByBarcode = this.productRepository.findByBarcode(putRequest.barcode());
        if (existingByBarcode != null && !existingByBarcode.getId().equals(product.getId()))
            throw new ResourceAlreadyInUseException(entityName, "Barcode:" + putRequest.barcode().toString());


        productMapper.updateEntity(putRequest, product);
        product.setCategory(category);
        productRepository.save(product);

        return this.productMapper.entityToDtoResponse(product);
    }

    public ProductDtoResponse updateCategory(Long productId, Long categoryId) {
        Product product = this.productRepository.findByIdAndActiveTrueAndCategoryActiveTrue(productId)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "ID:" + productId.toString()));

        Category category = this.categoryRepository.findByIdAndActiveTrue(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID:" + categoryId.toString()));

        product.setCategory(category);
        productRepository.save(product);

        return this.productMapper.entityToDtoResponse(product);
    }

    public void delete(Long id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, "ID:" + id.toString()));

        this.productRepository.delete(product);
    }

    private Product reactivateProduct(Product product, ProductDtoPostRequest dtoPostRequest) {
        Category category = this.categoryRepository.findByIdAndActiveTrue(dtoPostRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID:" + dtoPostRequest.categoryId().toString()));

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
