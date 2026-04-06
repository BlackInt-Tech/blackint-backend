package com.blackint.service;

import com.blackint.dto.BlogSummaryDTO;
import com.blackint.dto.ProjectSummaryDTO;
import com.blackint.dto.response.*;
import com.blackint.entity.*;
import com.blackint.mapper.*;
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
    private OfferingRepository offeringRepository;

    @Autowired
    private BlogRepository blogRepository;

    public HomepageResponse getHomepageData() {

        HomepageResponse response = new HomepageResponse();

        // ================= PROJECTS =================
        
        List<ProjectSummaryDTO> projects =
                projectRepository.findFeaturedPublishedProjects();

        // ================= OFFERINGS =================
        List<Offering> offerings =
                offeringRepository.findTopPublishedOfferings(
                        PageRequest.of(0, 15)
                );

        // ================= BLOGS =================
        List<BlogSummaryDTO> blogs =
                blogRepository.findTopBlogs(PageRequest.of(0, 15));

        response.setInsights(blogs);

        // ================= SET RESPONSE =================

        response.setProjects(projects);

        response.setOfferings(
                offerings.stream()
                        .map(OfferingMapper::toResponse)
                        .toList()
        );

        response.setInsights(blogs);

        return response;
    }
}