package com.blackint.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeadAnalyticsResponse {

    private long totalLeads;
    private long newLeads;
    private long contactedLeads;
    private long convertedLeads;
    private Long todayLeads;
    private Long monthLeads;
}

