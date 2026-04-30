package com.thewhitebeard.service;

import com.thewhitebeard.dto.request.AboutRequest;
import com.thewhitebeard.dto.response.AboutResponse;

public interface AboutService {
    AboutResponse getAbout();
    AboutResponse updateAbout(AboutRequest request);
}
