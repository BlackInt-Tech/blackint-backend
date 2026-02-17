package com.blackint.controller;

import com.blackint.dto.request.OfferingRequest;
import com.blackint.dto.response.OfferingResponse;
import com.blackint.service.OfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offering")
@RequiredArgsConstructor
public class OfferingController {

    private final OfferingService service;

    @PostMapping
    public OfferingResponse create(@RequestBody OfferingRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public OfferingResponse update(@PathVariable UUID id,
                                  @RequestBody OfferingRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.softDelete(id);
    }

    @PutMapping("/{id}/restore")
    public void restore(@PathVariable UUID id) {
        service.restore(id);
    }

    @PutMapping("/{id}/publish")
    public OfferingResponse publish(@PathVariable UUID id) {
        return service.publish(id);
    }

    @GetMapping("/published")
    public List<OfferingResponse> getPublished() {
        return service.getPublished();
    }

    @GetMapping("/{slug}")
    public OfferingResponse getBySlug(@PathVariable String slug) {
        return service.getBySlug(slug);
    }

    @GetMapping("/admin/all")
    public List<OfferingResponse> getAllForAdmin() {
        return service.getAllForAdmin();
    }
}
