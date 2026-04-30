package com.thewhitebeard.serviceimpl;

import com.thewhitebeard.dto.request.CategoryRequest;
import com.thewhitebeard.dto.response.CategoryResponse;
import com.thewhitebeard.exception.BadRequestException;
import com.thewhitebeard.exception.ResourceNotFoundException;
import com.thewhitebeard.model.Category;
import com.thewhitebeard.repository.CategoryRepository;
import com.thewhitebeard.repository.ProductRepository;
import com.thewhitebeard.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepo.findAll()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepo.existsByName(request.getName())) {
            throw new BadRequestException("Category '" + request.getName() + "' already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .build();
        return toResponse(categoryRepo.save(category));
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setName(request.getName());
        return toResponse(categoryRepo.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        // Check karo koi product linked hai ya nahi
        long productCount = productRepo.countByCategoryId(id);
        if (productCount > 0) {
            throw new BadRequestException(
                "Is category mein " + productCount + " product(s) hain — pehle products delete karo ya doosri category mein move karo"
            );
        }
        categoryRepo.deleteById(id);
    }

    private CategoryResponse toResponse(Category c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .build();
    }
}
