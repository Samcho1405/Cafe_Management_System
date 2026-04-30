package com.thewhitebeard.serviceimpl;

import com.thewhitebeard.dto.request.ProductRequest;
import com.thewhitebeard.dto.response.ProductResponse;
import com.thewhitebeard.exception.ResourceNotFoundException;
import com.thewhitebeard.model.Category;
import com.thewhitebeard.model.Product;
import com.thewhitebeard.repository.CategoryRepository;
import com.thewhitebeard.repository.ProductRepository;
import com.thewhitebeard.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @Override
    public List<ProductResponse> getAllProducts(Long categoryId) {
        List<Product> products = categoryId != null
         ? productRepo.findByCategoryId(categoryId)
        : productRepo.findAll();
        return products.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return toResponse(p);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        Product product = Product.builder()
                .name(request.getName())
                .category(category)
                .price(request.getPrice())
                .stock(request.getStock())
                .description(request.getDescription())
                .image(request.getImage())
                .build();
        return toResponse(productRepo.save(product));
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (request.getCategoryId() != null) {
            Category category = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            product.setCategory(category);
        }
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());

        return toResponse(productRepo.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepo.deleteById(id);
    }

    public ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .stock(p.getStock())
                .description(p.getDescription())
                .image(p.getImage())
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                .build();
    }
}
