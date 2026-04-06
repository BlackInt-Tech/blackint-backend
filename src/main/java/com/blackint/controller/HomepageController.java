package com.blackint.controller;

import com.blackint.common.ApiResponse;
import com.blackint.dto.response.HomepageResponse;
import com.blackint.service.HomepageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class HomepageController {

    @Autowired
    private HomepageService homepageService;

    @GetMapping("/homepage-data")
    public ApiResponse<HomepageResponse> getHomepageData() {
        HomepageResponse response = homepageService.getHomepageData();
        return ApiResponse.success(response);
    }
}