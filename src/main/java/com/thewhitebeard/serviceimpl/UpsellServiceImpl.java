package com.thewhitebeard.serviceimpl;

import com.thewhitebeard.dto.request.UpsellRequest;
import com.thewhitebeard.dto.response.UpsellResponse;
import com.thewhitebeard.exception.ResourceNotFoundException;
import com.thewhitebeard.model.Product;
import com.thewhitebeard.model.Upsell;
import com.thewhitebeard.repository.ProductRepository;
import com.thewhitebeard.repository.UpsellRepository;
import com.thewhitebeard.service.UpsellService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpsellServiceImpl implements UpsellService {

    private final UpsellRepository upsellRepo;
    private final ProductRepository productRepo;

    @Override
    public List<UpsellResponse> getAllUpsells() {
        return upsellRepo.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public UpsellResponse checkUpsell(int cartQuantity) {
        return upsellRepo
                .findFirstByEnabledTrueAndTriggerQuantityLessThanEqualOrderByTriggerQuantityDesc(cartQuantity)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    public UpsellResponse createUpsell(UpsellRequest request) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
        Upsell upsell = Upsell.builder()
                .product(product)
                .triggerQuantity(request.getTriggerQuantity())
                .message(request.getMessage())
                .price(request.getPrice())
                .enabled(request.isEnabled())
                .build();
        return toResponse(upsellRepo.save(upsell));
    }

    @Override
    public UpsellResponse updateUpsell(Long id, UpsellRequest request) {
        Upsell upsell = upsellRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Upsell rule not found with id: " + id));

        if (request.getProductId() != null) {
            Product product = productRepo.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));
            upsell.setProduct(product);
        }
        if (request.getTriggerQuantity() > 0) upsell.setTriggerQuantity(request.getTriggerQuantity());
        if (request.getMessage() != null) upsell.setMessage(request.getMessage());
        if (request.getPrice() > 0) upsell.setPrice(request.getPrice());
        upsell.setEnabled(request.isEnabled());

        return toResponse(upsellRepo.save(upsell));
    }

    @Override
    public void deleteUpsell(Long id) {
        if (!upsellRepo.existsById(id)) {
            throw new ResourceNotFoundException("Upsell rule not found with id: " + id);
        }
        upsellRepo.deleteById(id);
    }

    private UpsellResponse toResponse(Upsell u) {
        return UpsellResponse.builder()
                .id(u.getId())
                .productId(u.getProduct() != null ? u.getProduct().getId() : null)
                .productName(u.getProduct() != null ? u.getProduct().getName() : null)
                .productImage(u.getProduct() != null ? u.getProduct().getImage() : null)
                .triggerQuantity(u.getTriggerQuantity())
                .message(u.getMessage())
                .price(u.getPrice())
                .enabled(u.isEnabled())
                .build();
    }
}
