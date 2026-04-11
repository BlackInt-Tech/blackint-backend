package com.blackint.dto.response;

import java.util.List;

import com.blackint.dto.BlogSummaryDTO;
import com.blackint.dto.ProjectSummaryDTO;

public class HomepageResponse {

    private List<ProjectSummaryDTO> projects;
    private OfferingsWrapper offerings;
    private List<BlogSummaryDTO> insights;
    

    public List<ProjectSummaryDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectSummaryDTO> projects) {
        this.projects = projects;
    }

    public OfferingsWrapper getOfferings() {
        return offerings;
    }

    public void setOfferings(OfferingsWrapper offerings) {
        this.offerings = offerings;
    }

    public List<BlogSummaryDTO> getInsights() {
        return insights;
    }

    public void setInsights(List<BlogSummaryDTO> insights) {
        this.insights = insights;
    }

}