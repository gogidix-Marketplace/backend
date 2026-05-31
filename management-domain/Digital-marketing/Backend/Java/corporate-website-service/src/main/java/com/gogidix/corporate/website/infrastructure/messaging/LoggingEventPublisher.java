package com.gogidix.corporate.website.infrastructure.messaging;

import com.gogidix.corporate.website.domain.event.*;
import com.gogidix.corporate.website.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishBlogPostCreated(BlogPostCreatedEvent event) {
        log.info("BlogPost created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBlogPostUpdated(BlogPostUpdatedEvent event) {
        log.info("BlogPost updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBlogPostDeleted(BlogPostDeletedEvent event) {
        log.info("BlogPost deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCaseStudyCreated(CaseStudyCreatedEvent event) {
        log.info("CaseStudy created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCaseStudyUpdated(CaseStudyUpdatedEvent event) {
        log.info("CaseStudy updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCaseStudyDeleted(CaseStudyDeletedEvent event) {
        log.info("CaseStudy deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishContentStatusCreated(ContentStatusCreatedEvent event) {
        log.info("ContentStatus created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishContentStatusUpdated(ContentStatusUpdatedEvent event) {
        log.info("ContentStatus updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishContentStatusDeleted(ContentStatusDeletedEvent event) {
        log.info("ContentStatus deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishJobCreated(JobCreatedEvent event) {
        log.info("Job created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishJobUpdated(JobUpdatedEvent event) {
        log.info("Job updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishJobDeleted(JobDeletedEvent event) {
        log.info("Job deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLanguageCreated(LanguageCreatedEvent event) {
        log.info("Language created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLanguageUpdated(LanguageUpdatedEvent event) {
        log.info("Language updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLanguageDeleted(LanguageDeletedEvent event) {
        log.info("Language deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLocalizedContentCreated(LocalizedContentCreatedEvent event) {
        log.info("LocalizedContent created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLocalizedContentUpdated(LocalizedContentUpdatedEvent event) {
        log.info("LocalizedContent updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLocalizedContentDeleted(LocalizedContentDeletedEvent event) {
        log.info("LocalizedContent deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMediaAssetCreated(MediaAssetCreatedEvent event) {
        log.info("MediaAsset created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMediaAssetUpdated(MediaAssetUpdatedEvent event) {
        log.info("MediaAsset updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMediaAssetDeleted(MediaAssetDeletedEvent event) {
        log.info("MediaAsset deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricCreated(MetricCreatedEvent event) {
        log.info("Metric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricUpdated(MetricUpdatedEvent event) {
        log.info("Metric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricDeleted(MetricDeletedEvent event) {
        log.info("Metric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPageCreated(PageCreatedEvent event) {
        log.info("Page created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPageUpdated(PageUpdatedEvent event) {
        log.info("Page updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPageDeleted(PageDeletedEvent event) {
        log.info("Page deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPageVersionCreated(PageVersionCreatedEvent event) {
        log.info("PageVersion created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPageVersionUpdated(PageVersionUpdatedEvent event) {
        log.info("PageVersion updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPageVersionDeleted(PageVersionDeletedEvent event) {
        log.info("PageVersion deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPressReleaseCreated(PressReleaseCreatedEvent event) {
        log.info("PressRelease created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPressReleaseUpdated(PressReleaseUpdatedEvent event) {
        log.info("PressRelease updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPressReleaseDeleted(PressReleaseDeletedEvent event) {
        log.info("PressRelease deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPricingInfoCreated(PricingInfoCreatedEvent event) {
        log.info("PricingInfo created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPricingInfoUpdated(PricingInfoUpdatedEvent event) {
        log.info("PricingInfo updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPricingInfoDeleted(PricingInfoDeletedEvent event) {
        log.info("PricingInfo deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishProductCreated(ProductCreatedEvent event) {
        log.info("Product created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishProductUpdated(ProductUpdatedEvent event) {
        log.info("Product updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishProductDeleted(ProductDeletedEvent event) {
        log.info("Product deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishProductFeatureCreated(ProductFeatureCreatedEvent event) {
        log.info("ProductFeature created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishProductFeatureUpdated(ProductFeatureUpdatedEvent event) {
        log.info("ProductFeature updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishProductFeatureDeleted(ProductFeatureDeletedEvent event) {
        log.info("ProductFeature deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionCreated(RegionCreatedEvent event) {
        log.info("Region created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionUpdated(RegionUpdatedEvent event) {
        log.info("Region updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRegionDeleted(RegionDeletedEvent event) {
        log.info("Region deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSeoMetadataCreated(SeoMetadataCreatedEvent event) {
        log.info("SeoMetadata created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSeoMetadataUpdated(SeoMetadataUpdatedEvent event) {
        log.info("SeoMetadata updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSeoMetadataDeleted(SeoMetadataDeletedEvent event) {
        log.info("SeoMetadata deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
