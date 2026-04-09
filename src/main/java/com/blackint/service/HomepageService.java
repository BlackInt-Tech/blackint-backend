package com.blackint.service;

import com.blackint.dto.BlogSummaryDTO;
import com.blackint.dto.ProjectSummaryDTO;
import com.blackint.dto.response.*;
import com.blackint.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomepageService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OfferingService offeringService;

    @Autowired
    private BlogRepository blogRepository;

    public HomepageResponse getHomepageData() {

        HomepageResponse response = new HomepageResponse();

        // ================= PROJECTS =================
        List<ProjectSummaryDTO> projects =
                projectRepository.findFeaturedPublishedProjects();

        // ================= SERVICES =================
        List<OfferingResponse> services =
                offeringService.getByType("SERVICE");

        // ================= PACKAGES =================
        List<OfferingResponse> packages =
                offeringService.getByType("PACKAGE");

        // ================= BLOGS =================
        List<BlogSummaryDTO> blogs =
                blogRepository.findTopBlogs(PageRequest.of(0, 15));

        // ================= BUILD WRAPPER =================
        OfferingsWrapper offeringsWrapper = OfferingsWrapper.builder()
                .services(services)
                .packages(packages)
                .build();

        // ================= SET RESPONSE =================
        response.setProjects(projects);
        response.setOfferings(offeringsWrapper);
        response.setInsights(blogs);

        return response;
    }
}