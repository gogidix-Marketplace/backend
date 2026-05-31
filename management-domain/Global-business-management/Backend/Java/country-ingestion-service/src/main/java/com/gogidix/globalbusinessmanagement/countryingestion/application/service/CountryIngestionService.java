package com.gogidix.globalbusinessmanagement.countryingestion.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.CountryDataDto;
import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.IngestionBatchDto;
import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.ValidationErrorDto;
import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.mapper.CountryIngestionMapper;
import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.CountryData;
import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.IngestionBatch;
import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.ValidationError;
import com.gogidix.globalbusinessmanagement.countryingestion.domain.repository.CountryDataRepository;
import com.gogidix.globalbusinessmanagement.countryingestion.domain.repository.IngestionBatchRepository;
import com.gogidix.globalbusinessmanagement.countryingestion.domain.repository.ValidationErrorRepository;
import com.gogidix.globalbusinessmanagement.countryingestion.shared.exception.IngestionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for country data ingestion operations.
 * Handles file parsing, validation, and storage of country data.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CountryIngestionService {

    private final CountryDataRepository countryDataRepository;
    private final IngestionBatchRepository ingestionBatchRepository;
    private final ValidationErrorRepository validationErrorRepository;
    private final DataValidationService dataValidationService;
    private final BatchProcessingService batchProcessingService;
    private final CountryIngestionMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final Set<String> SUPPORTED_FORMATS = Set.of("csv", "json", "xml", "xlsx", "xls");

    /**
     * Creates a new ingestion batch.
     */
    @Transactional
    public IngestionBatchDto createBatch(String source, String format,
                                         Long fileSize, String fileName,
                                         String schemaId, String createdBy) {
        log.info("Creating ingestion batch for source: {}", source);

        String batchId = generateBatchId();

        IngestionBatch batch = IngestionBatch.builder()
                .batchId(batchId)
                .batchType(IngestionBatch.BatchType.MANUAL_UPLOAD)
                .source(source)
                .format(format)
                .fileSizeBytes(fileSize)
                .fileName(fileName)
                .schemaId(schemaId)
                .status(IngestionBatch.BatchStatus.PENDING)
                .createdBy(createdBy)
                .createdDate(LocalDateTime.now())
                .totalRecords(0L)
                .processedRecords(0L)
                .successfulRecords(0L)
                .failedRecords(0L)
                .validationEnabled(true)
                .build();

        batch = ingestionBatchRepository.save(batch);

        publishBatchEvent(batch, "CREATED");

        return mapper.toDto(batch);
    }

    /**
     * Ingests country data from a CSV file.
     */
    @Transactional
    public IngestionBatchDto ingestFromCsv(String batchId, MultipartFile file,
                                           Map<String, String> columnMappings,
                                           boolean validateOnly) throws IOException {
        log.info("Starting CSV ingestion for batch: {}", batchId);

        IngestionBatch batch = ingestionBatchRepository.findByBatchId(batchId)
                .orElseThrow(() -> new IngestionException("Batch not found: " + batchId));

        batch.setStatus(IngestionBatch.BatchStatus.IN_PROGRESS);
        batch.setStartTime(LocalDateTime.now());
        ingestionBatchRepository.save(batch);

        List<CountryData> countryDataList = new ArrayList<>();
        List<ValidationError> validationErrors = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build())) {

            List<CSVRecord> records = csvParser.getRecords();
            batch.setTotalRecords((long) records.size());

            int recordNumber = 1;
            for (CSVRecord record : records) {
                try {
                    CountryData countryData = parseCsvRecord(record, columnMappings, batchId, recordNumber);
                    List<ValidationError> errors = dataValidationService.validateCountryData(countryData);

                    if (errors.isEmpty()) {
                        countryDataList.add(countryData);
                        batch.incrementProcessed(true);
                    } else {
                        validationErrors.addAll(errors);
                        validationErrorRepository.saveAll(errors);
                        batch.incrementProcessed(false);
                    }
                } catch (Exception e) {
                    log.error("Error processing record {}: {}", recordNumber, e.getMessage());
                    ValidationError error = ValidationError.createHigh(
                            batchId,
                            "record_" + recordNumber,
                            "PARSING_ERROR",
                            "Failed to parse record: " + e.getMessage()
                    );
                    error.setRowNumber(recordNumber);
                    validationErrors.add(error);
                    validationErrorRepository.save(error);
                    batch.incrementProcessed(false);
                }

                recordNumber++;
                if (recordNumber % 100 == 0) {
                    batch.calculateProgress();
                    ingestionBatchRepository.save(batch);
                }
            }
        } catch (Exception e) {
            log.error("Error parsing CSV file", e);
            batch.setStatus(IngestionBatch.BatchStatus.FAILED);
            batch.setErrorMessage("CSV parsing error: " + e.getMessage());
            ingestionBatchRepository.save(batch);
            throw new IngestionException("Failed to parse CSV file", e);
        }

        // Save country data if not validating only
        if (!validateOnly && !countryDataList.isEmpty()) {
            List<CountryData> savedData = countryDataRepository.saveAll(countryDataList);
            log.info("Saved {} country records", savedData.size());
        }

        // Complete the batch
        batch.markAsCompleted();
        batch = ingestionBatchRepository.save(batch);

        publishBatchEvent(batch, "COMPLETED");

        IngestionBatchDto result = mapper.toDto(batch);

        return result;
    }

    /**
     * Ingests country data from JSON.
     */
    @Transactional
    public IngestionBatchDto ingestFromJson(String batchId, String jsonData,
                                             boolean validateOnly) throws IOException {
        log.info("Starting JSON ingestion for batch: {}", batchId);

        IngestionBatch batch = ingestionBatchRepository.findByBatchId(batchId)
                .orElseThrow(() -> new IngestionException("Batch not found: " + batchId));

        batch.setStatus(IngestionBatch.BatchStatus.IN_PROGRESS);
        batch.setStartTime(LocalDateTime.now());
        ingestionBatchRepository.save(batch);

        List<CountryData> countryDataList = new ArrayList<>();
        List<ValidationError> validationErrors = new ArrayList<>();

        CountryDataDto[] dataArray = objectMapper.readValue(jsonData, CountryDataDto[].class);
        batch.setTotalRecords((long) dataArray.length);

        int recordNumber = 1;
        for (CountryDataDto dto : dataArray) {
            final int currentRecordNumber = recordNumber;
            try {
                CountryData countryData = mapper.toEntity(dto);
                countryData.setIngestionBatchId(batchId);
                countryData.setIngestionTimestamp(LocalDateTime.now());

                List<ValidationError> errors = dataValidationService.validateCountryData(countryData);

                if (errors.isEmpty()) {
                    countryDataList.add(countryData);
                    batch.incrementProcessed(true);
                } else {
                    validationErrors.addAll(errors);
                    errors.forEach(e -> {
                        e.setRowNumber(currentRecordNumber);
                        validationErrorRepository.save(e);
                    });
                    batch.incrementProcessed(false);
                }
            } catch (Exception e) {
                log.error("Error processing record {}: {}", currentRecordNumber, e.getMessage());
                ValidationError error = ValidationError.createHigh(
                        batchId,
                        "record_" + currentRecordNumber,
                        "PARSING_ERROR",
                        "Failed to parse record: " + e.getMessage()
                );
                error.setRowNumber(recordNumber);
                validationErrors.add(error);
                validationErrorRepository.save(error);
                batch.incrementProcessed(false);
            }

            recordNumber++;
            if (recordNumber % 100 == 0) {
                batch.calculateProgress();
                ingestionBatchRepository.save(batch);
            }
        }

        if (!validateOnly && !countryDataList.isEmpty()) {
            countryDataRepository.saveAll(countryDataList);
        }

        batch.markAsCompleted();
        batch = ingestionBatchRepository.save(batch);

        publishBatchEvent(batch, "COMPLETED");

        IngestionBatchDto result = mapper.toDto(batch);

        return result;
    }

    /**
     * Parses a CSV record into CountryData.
     */
    private CountryData parseCsvRecord(CSVRecord record, Map<String, String> columnMappings,
                                       String batchId, int rowNumber) {
        CountryData.CountryDataBuilder builder = CountryData.builder();

        builder.countryCode(getMappedValue(record, columnMappings, "countryCode", "code"));
        builder.countryName(getMappedValue(record, columnMappings, "countryName", "name"));
        builder.region(getMappedValue(record, columnMappings, "region"));
        builder.subRegion(getMappedValue(record, columnMappings, "subRegion", "subregion"));
        builder.continent(getMappedValue(record, columnMappings, "continent"));
        builder.capitalCity(getMappedValue(record, columnMappings, "capital", "capitalCity"));
        builder.currencyCode(getMappedValue(record, columnMappings, "currencyCode", "currency"));
        builder.callingCode(getMappedValue(record, columnMappings, "callingCode", "phone"));
        builder.internetTld(getMappedValue(record, columnMappings, "tld", "internetTld"));

        // Numeric fields
        String populationStr = getMappedValue(record, columnMappings, "population");
        if (StringUtils.isNotBlank(populationStr)) {
            try {
                builder.population(new BigDecimal(populationStr.replaceAll("[,\\s]", "")));
            } catch (NumberFormatException e) {
                log.warn("Invalid population value: {}", populationStr);
            }
        }

        String gdpStr = getMappedValue(record, columnMappings, "gdp", "gdpUsd");
        if (StringUtils.isNotBlank(gdpStr)) {
            try {
                builder.gdpUsd(new BigDecimal(gdpStr.replaceAll("[,\\s$]", "")));
            } catch (NumberFormatException e) {
                log.warn("Invalid GDP value: {}", gdpStr);
            }
        }

        String latitudeStr = getMappedValue(record, columnMappings, "latitude", "lat");
        if (StringUtils.isNotBlank(latitudeStr)) {
            try {
                builder.latitude(new BigDecimal(latitudeStr));
            } catch (NumberFormatException e) {
                log.warn("Invalid latitude value: {}", latitudeStr);
            }
        }

        String longitudeStr = getMappedValue(record, columnMappings, "longitude", "lon", "lng");
        if (StringUtils.isNotBlank(longitudeStr)) {
            try {
                builder.longitude(new BigDecimal(longitudeStr));
            } catch (NumberFormatException e) {
                log.warn("Invalid longitude value: {}", longitudeStr);
            }
        }

        // ISO codes
        builder.isoCodeAlpha3(getMappedValue(record, columnMappings, "iso3", "isoCodeAlpha3"));
        String isoNumericStr = getMappedValue(record, columnMappings, "isoNumeric", "iso_num");
        if (StringUtils.isNotBlank(isoNumericStr)) {
            try {
                builder.isoNumericCode(Integer.parseInt(isoNumericStr));
            } catch (NumberFormatException e) {
                log.warn("Invalid ISO numeric code: {}", isoNumericStr);
            }
        }

        // Ingestion metadata
        builder.ingestionBatchId(batchId);
        builder.ingestionTimestamp(LocalDateTime.now());
        builder.validated(false);
        builder.active(true);

        CountryData countryData = builder.build();
        countryData.calculateDataQualityScore();

        return countryData;
    }

    /**
     * Gets a value from CSV record with mapping support.
     */
    private String getMappedValue(CSVRecord record, Map<String, String> mappings, String... possibleKeys) {
        for (String key : possibleKeys) {
            // Try mapped name first
            if (mappings != null && mappings.containsKey(key)) {
                String mappedName = mappings.get(key);
                if (record.isSet(mappedName)) {
                    return record.get(mappedName).trim();
                }
            }
            // Try direct key
            if (record.isSet(key)) {
                return record.get(key).trim();
            }
        }
        return null;
    }

    /**
     * Gets country data by ID.
     */
    public CountryDataDto getCountryById(String id) {
        CountryData countryData = countryDataRepository.findById(id)
                .orElseThrow(() -> new IngestionException("Country data not found: " + id));
        return mapper.toDto(countryData);
    }

    /**
     * Gets country data by country code.
     */
    public CountryDataDto getCountryByCode(String countryCode) {
        CountryData countryData = countryDataRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new IngestionException("Country not found with code: " + countryCode));
        return mapper.toDto(countryData);
    }

    /**
     * Searches countries by name or code.
     */
    public Page<CountryDataDto> searchCountries(String searchTerm, Pageable pageable) {
        return countryDataRepository.searchByCountryNameOrCode(searchTerm, pageable)
                .map(mapper::toDto);
    }

    /**
     * Gets all countries by region.
     */
    public List<CountryDataDto> getCountriesByRegion(String region) {
        return countryDataRepository.findByRegion(region).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates country data.
     */
    @Transactional
    public CountryDataDto updateCountry(String id, CountryDataDto dto) {
        CountryData existing = countryDataRepository.findById(id)
                .orElseThrow(() -> new IngestionException("Country data not found: " + id));

        mapper.updateEntityFromDto(dto, existing);
        existing.updateTimestamp();
        existing.calculateDataQualityScore();

        CountryData saved = countryDataRepository.save(existing);
        return mapper.toDto(saved);
    }

    /**
     * Deletes country data.
     */
    @Transactional
    public void deleteCountry(String id) {
        if (!countryDataRepository.existsById(id)) {
            throw new IngestionException("Country data not found: " + id);
        }
        countryDataRepository.deleteById(id);
    }

    /**
     * Gets batch by ID.
     */
    public IngestionBatchDto getBatch(String batchId) {
        IngestionBatch batch = ingestionBatchRepository.findByBatchId(batchId)
                .orElseThrow(() -> new IngestionException("Batch not found: " + batchId));
        return mapper.toDto(batch);
    }

    /**
     * Gets all validation errors for a batch.
     */
    public List<ValidationErrorDto> getValidationErrorsForBatch(String batchId) {
        return validationErrorRepository.findByBatchId(batchId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Generates a unique batch ID.
     */
    private String generateBatchId() {
        return "BATCH-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) +
                "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Publishes a batch event to Kafka.
     */
    private void publishBatchEvent(IngestionBatch batch, String eventType) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", eventType);
            event.put("batchId", batch.getBatchId());
            event.put("status", batch.getStatus().name());
            event.put("timestamp", LocalDateTime.now());

            kafkaTemplate.send("country.ingestion.events", event);
        } catch (Exception e) {
            log.warn("Failed to publish batch event: {}", e.getMessage());
        }
    }

    /**
     * Re-queues failed records from a batch for reprocessing.
     */
    @Transactional
    public IngestionBatchDto reprocessFailedRecords(String batchId, String createdBy) {
        IngestionBatch originalBatch = ingestionBatchRepository.findByBatchId(batchId)
                .orElseThrow(() -> new IngestionException("Batch not found: " + batchId));

        List<ValidationError> errors = validationErrorRepository.findByBatchIdAndStatus(
                batchId, ValidationError.ErrorStatus.OPEN);

        if (errors.isEmpty()) {
            throw new IngestionException("No failed records found for batch: " + batchId);
        }

        // Create retry batch
        String newBatchId = generateBatchId();
        IngestionBatch retryBatch = IngestionBatch.builder()
                .batchId(newBatchId)
                .batchType(IngestionBatch.BatchType.BULK_CORRECTION)
                .source(originalBatch.getSource())
                .status(IngestionBatch.BatchStatus.PENDING)
                .createdBy(createdBy)
                .createdDate(LocalDateTime.now())
                .parentBatchId(batchId)
                .totalRecords((long) errors.size())
                .validationEnabled(true)
                .build();

        retryBatch = ingestionBatchRepository.save(retryBatch);

        // Mark errors for reprocessing
        errors.forEach(error -> {
            error.setStatus(ValidationError.ErrorStatus.IN_PROGRESS);
            error.setAssignedTo(createdBy);
        });
        validationErrorRepository.saveAll(errors);

        return mapper.toDto(retryBatch);
    }
}
