package com.thewhitebeard.serviceimpl;

import com.thewhitebeard.dto.request.AboutRequest;
import com.thewhitebeard.dto.response.AboutResponse;
import com.thewhitebeard.model.AboutPage;
import com.thewhitebeard.repository.AboutPageRepository;
import com.thewhitebeard.service.AboutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AboutServiceImpl implements AboutService {

    private final AboutPageRepository aboutRepo;

    @Override
    public AboutResponse getAbout() {
        return aboutRepo.findAll().stream().findFirst()
                .map(this::toResponse)
                .orElse(AboutResponse.builder().content("").build());
    }

    @Override
    public AboutResponse updateAbout(AboutRequest request) {
        AboutPage page = aboutRepo.findAll().stream().findFirst()
                .orElse(new AboutPage());
        page.setContent(request.getContent());
        return toResponse(aboutRepo.save(page));
    }

    private AboutResponse toResponse(AboutPage a) {
        return AboutResponse.builder()
                .id(a.getId())
                .content(a.getContent())
                .build();
    }
}
