package com.gogidix.globalbusinessmanagement.countryingestion.application.dto.mapper;

import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.*;
import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CountryIngestionMapper {

    CountryDataDto toDto(CountryData entity);

    @Mapping(target = "validationErrorsList", ignore = true)
    @Mapping(target = "validationErrors", ignore = true)
    @Mapping(target = "ingestionTimestamp", ignore = true)
    CountryData toEntity(CountryDataDto dto);

    @Mapping(target = "validationErrorsList", ignore = true)
    @Mapping(target = "validationErrors", ignore = true)
    void updateEntityFromDto(CountryDataDto dto, @MappingTarget CountryData entity);

    List<CountryDataDto> toCountryDataDtoList(List<CountryData> entities);

    IngestionBatchDto toDto(IngestionBatch entity);

    @Mapping(target = "validationErrors", ignore = true)
    @Mapping(target = "dataSchema", ignore = true)
    IngestionBatch toEntity(IngestionBatchDto dto);

    @Mapping(target = "validationErrors", ignore = true)
    @Mapping(target = "dataSchema", ignore = true)
    void updateBatchFromDto(IngestionBatchDto dto, @MappingTarget IngestionBatch entity);

    List<IngestionBatchDto> toIngestionBatchDtoList(List<IngestionBatch> entities);

    DataSchemaDto toDto(DataSchema entity);

    @Mapping(target = "usedInBatches", ignore = true)
    DataSchema toEntity(DataSchemaDto dto);

    @Mapping(target = "usedInBatches", ignore = true)
    void updateSchemaFromDto(DataSchemaDto dto, @MappingTarget DataSchema entity);

    List<DataSchemaDto> toDataSchemaDtoList(List<DataSchema> entities);

    ValidationErrorDto toDto(ValidationError entity);

    ValidationError toEntity(ValidationErrorDto dto);

    void updateErrorFromDto(ValidationErrorDto dto, @MappingTarget ValidationError entity);

    List<ValidationErrorDto> toValidationErrorDtoList(List<ValidationError> entities);

    DataSchema.SchemaField toSchemaFieldEntity(DataSchemaDto.SchemaFieldDto dto);

    DataSchemaDto.SchemaFieldDto toSchemaFieldDto(DataSchema.SchemaField entity);

    List<DataSchema.SchemaField> toSchemaFieldEntityList(List<DataSchemaDto.SchemaFieldDto> dtos);

    List<DataSchemaDto.SchemaFieldDto> toSchemaFieldDtoList(List<DataSchema.SchemaField> entities);

    default IngestionBatchDto.BatchTypeDto batchTypeToDto(IngestionBatch.BatchType batchType) {
        if (batchType == null) return null;
        return IngestionBatchDto.BatchTypeDto.valueOf(batchType.name());
    }

    default IngestionBatch.BatchType batchTypeDtoToEntity(IngestionBatchDto.BatchTypeDto batchTypeDto) {
        if (batchTypeDto == null) return null;
        return IngestionBatch.BatchType.valueOf(batchTypeDto.name());
    }

    default IngestionBatchDto.BatchStatusDto batchStatusToDto(IngestionBatch.BatchStatus status) {
        if (status == null) return null;
        return IngestionBatchDto.BatchStatusDto.valueOf(status.name());
    }

    default IngestionBatch.BatchStatus batchStatusDtoToEntity(IngestionBatchDto.BatchStatusDto statusDto) {
        if (statusDto == null) return null;
        return IngestionBatch.BatchStatus.valueOf(statusDto.name());
    }

    default DataSchemaDto.SchemaTypeDto schemaTypeToDto(DataSchema.SchemaType schemaType) {
        if (schemaType == null) return null;
        return DataSchemaDto.SchemaTypeDto.valueOf(schemaType.name());
    }

    default DataSchema.SchemaType schemaTypeDtoToEntity(DataSchemaDto.SchemaTypeDto schemaTypeDto) {
        if (schemaTypeDto == null) return null;
        return DataSchema.SchemaType.valueOf(schemaTypeDto.name());
    }

    default DataSchemaDto.FieldTypeDto fieldTypeToDto(DataSchema.SchemaField.FieldType fieldType) {
        if (fieldType == null) return null;
        return DataSchemaDto.FieldTypeDto.valueOf(fieldType.name());
    }

    default DataSchema.SchemaField.FieldType fieldTypeDtoToEntity(DataSchemaDto.FieldTypeDto fieldTypeDto) {
        if (fieldTypeDto == null) return null;
        return DataSchema.SchemaField.FieldType.valueOf(fieldTypeDto.name());
    }

    default ValidationErrorDto.ErrorLevelDto errorLevelToDto(ValidationError.ErrorLevel errorLevel) {
        if (errorLevel == null) return null;
        return ValidationErrorDto.ErrorLevelDto.valueOf(errorLevel.name());
    }

    default ValidationError.ErrorLevel errorLevelDtoToEntity(ValidationErrorDto.ErrorLevelDto errorLevelDto) {
        if (errorLevelDto == null) return null;
        return ValidationError.ErrorLevel.valueOf(errorLevelDto.name());
    }

    default ValidationErrorDto.ErrorTypeDto errorTypeToDto(ValidationError.ErrorType errorType) {
        if (errorType == null) return null;
        return ValidationErrorDto.ErrorTypeDto.valueOf(errorType.name());
    }

    default ValidationError.ErrorType errorTypeDtoToEntity(ValidationErrorDto.ErrorTypeDto errorTypeDto) {
        if (errorTypeDto == null) return null;
        return ValidationError.ErrorType.valueOf(errorTypeDto.name());
    }

    default ValidationErrorDto.ErrorStatusDto errorStatusToDto(ValidationError.ErrorStatus status) {
        if (status == null) return null;
        return ValidationErrorDto.ErrorStatusDto.valueOf(status.name());
    }

    default ValidationError.ErrorStatus errorStatusDtoToEntity(ValidationErrorDto.ErrorStatusDto statusDto) {
        if (statusDto == null) return null;
        return ValidationError.ErrorStatus.valueOf(statusDto.name());
    }
}
