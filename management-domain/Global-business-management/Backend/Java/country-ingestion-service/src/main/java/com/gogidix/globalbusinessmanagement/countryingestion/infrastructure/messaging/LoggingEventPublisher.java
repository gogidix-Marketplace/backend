package com.gogidix.globalbusinessmanagement.countryingestion.infrastructure.messaging;

import com.gogidix.globalbusinessmanagement.countryingestion.domain.event.*;
import com.gogidix.globalbusinessmanagement.countryingestion.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishCountryDataCreated(CountryDataCreatedEvent event) {
        log.info("CountryData created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryDataUpdated(CountryDataUpdatedEvent event) {
        log.info("CountryData updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryDataDeleted(CountryDataDeletedEvent event) {
        log.info("CountryData deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDataSchemaCreated(DataSchemaCreatedEvent event) {
        log.info("DataSchema created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDataSchemaUpdated(DataSchemaUpdatedEvent event) {
        log.info("DataSchema updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDataSchemaDeleted(DataSchemaDeletedEvent event) {
        log.info("DataSchema deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishIngestionBatchCreated(IngestionBatchCreatedEvent event) {
        log.info("IngestionBatch created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishIngestionBatchUpdated(IngestionBatchUpdatedEvent event) {
        log.info("IngestionBatch updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishIngestionBatchDeleted(IngestionBatchDeletedEvent event) {
        log.info("IngestionBatch deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishValidationErrorCreated(ValidationErrorCreatedEvent event) {
        log.info("ValidationError created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishValidationErrorUpdated(ValidationErrorUpdatedEvent event) {
        log.info("ValidationError updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishValidationErrorDeleted(ValidationErrorDeletedEvent event) {
        log.info("ValidationError deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
