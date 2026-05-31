package com.gogidix.digitalmarketing.countrymarketingdashboard.interfaces.rest;

import com.gogidix.digitalmarketing.countrymarketingdashboard.application.dto.CampaignDTO;
import com.gogidix.digitalmarketing.countrymarketingdashboard.application.service.CampaignService;
import com.gogidix.digitalmarketing.countrymarketingdashboard.domain.model.Campaign;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService service;

    @PostMapping
    public ResponseEntity<Campaign> create(@RequestParam String tenantId, @RequestBody CampaignDTO.CreateCampaignRequest request) {
        return ResponseEntity.ok(service.createCampaign(tenantId, request));
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<Campaign> getById(@RequestParam String tenantId, @PathVariable String campaignId) {
        return service.getCampaign(tenantId, campaignId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Campaign>> getCampaigns(
            @RequestParam String tenantId, @RequestParam String country,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.getCampaigns(tenantId, country, PageRequest.of(page, size)));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Campaign>> getActive(@RequestParam String tenantId, @RequestParam String country) {
        return ResponseEntity.ok(service.getActiveCampaigns(tenantId, country));
    }

    @PostMapping("/{campaignId}/launch")
    public ResponseEntity<Campaign> launch(@RequestParam String tenantId, @PathVariable String campaignId) {
        return ResponseEntity.ok(service.launchCampaign(tenantId, campaignId));
    }

    @PostMapping("/{campaignId}/pause")
    public ResponseEntity<Campaign> pause(@RequestParam String tenantId, @PathVariable String campaignId) {
        return ResponseEntity.ok(service.pauseCampaign(tenantId, campaignId));
    }

    @DeleteMapping("/{campaignId}")
    public ResponseEntity<Void> delete(@RequestParam String tenantId, @PathVariable String campaignId) {
        service.deleteCampaign(tenantId, campaignId);
        return ResponseEntity.noContent().build();
    }
}
