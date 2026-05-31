package com.gogidix.globalbusinessmanagement.countryingestion.domain.port;

import com.gogidix.globalbusinessmanagement.countryingestion.domain.event.*;

public interface DomainEventPublisher {
    void publishCountryDataCreated(CountryDataCreatedEvent event);
    void publishCountryDataUpdated(CountryDataUpdatedEvent event);
    void publishCountryDataDeleted(CountryDataDeletedEvent event);
    void publishDataSchemaCreated(DataSchemaCreatedEvent event);
    void publishDataSchemaUpdated(DataSchemaUpdatedEvent event);
    void publishDataSchemaDeleted(DataSchemaDeletedEvent event);
    void publishIngestionBatchCreated(IngestionBatchCreatedEvent event);
    void publishIngestionBatchUpdated(IngestionBatchUpdatedEvent event);
    void publishIngestionBatchDeleted(IngestionBatchDeletedEvent event);
    void publishValidationErrorCreated(ValidationErrorCreatedEvent event);
    void publishValidationErrorUpdated(ValidationErrorUpdatedEvent event);
    void publishValidationErrorDeleted(ValidationErrorDeletedEvent event);
}
