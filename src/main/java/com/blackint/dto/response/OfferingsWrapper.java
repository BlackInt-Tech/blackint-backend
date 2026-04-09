package com.blackint.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingsWrapper {

    private List<OfferingResponse> services;
    private List<OfferingResponse> packages;
}