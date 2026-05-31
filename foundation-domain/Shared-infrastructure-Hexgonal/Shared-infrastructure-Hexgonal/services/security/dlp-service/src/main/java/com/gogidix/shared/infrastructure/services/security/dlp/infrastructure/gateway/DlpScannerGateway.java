package com.gogidix.shared.infrastructure.services.security.dlp.infrastructure.gateway;

import java.util.List;
import java.util.Map;

/**
 * Gateway for Data Loss Prevention (DLP) scanning providers.
 *
 * <p>This interface abstracts different DLP provider APIs allowing
 * the DLP service to work with any scanning provider transparently.</p>
 *
 * <p>Supported providers:
 * <ul>
 *   <li>Google Cloud Data Loss Prevention API</li>
 *   <li>Microsoft Priva (Azure Purview)</li>
 *   <li>AWS Macie</li>
 *   <li>Apache NLP / OpenNLP (Self-hosted)</li>
 * </ul>
 *
 * <p>Configuration:
 * <pre>
 * dlp.provider=gcp|microsoft|aws|local
 * dlp.gcp.project-id=your-project-id
 * dlp.gcp.credentials-path=/path/to/credentials.json
 * dlp.microsoft.endpoint=https://your-region.purview.azure.com
 * dlp.microsoft.api-key=your_api_key
 * </pre>
 */
public interface DlpScannerGateway {

    /**
     * Scan text content for sensitive information.
     *
     * @param content The text content to scan
     * @param infoTypes The types of info to detect (EMAIL, PHONE, SSN, CREDIT_CARD, etc.)
     * @return DLP scan results
     */
    ScanResult scanText(String content, List<InfoType> infoTypes);

    /**
     * Scan a file for sensitive information.
     *
     * @param fileBytes The file content as bytes
     * @param fileName The file name (helps determine scanning strategy)
     * @param infoTypes The types of info to detect
     * @return DLP scan results
     */
    ScanResult scanFile(byte[] fileBytes, String fileName, List<InfoType> infoTypes);

    /**
     * Scan a batch of text contents.
     *
     * @param contents List of text contents to scan
     * @param infoTypes The types of info to detect
     * @return List of scan results for each input
     */
    List<ScanResult> scanTextBatch(List<String> contents, List<InfoType> infoTypes);

    /**
     * De-identify (redact) sensitive information in text.
     *
     * @param content The text content with sensitive data
     * @param infoTypes The types of info to redact
     * @param replacementText The replacement text (default: [REDACTED])
     * @return De-identified text
     */
    String deidentifyText(String content, List<InfoType> infoTypes, String replacementText);

    /**
     * Inspect a structured data object for sensitive information.
     *
     * @param structuredData The data object as a map
     * @param infoTypes The types of info to detect
     * @return List of findings in the structured data
     */
    List<Finding> inspectStructuredData(Map<String, Object> structuredData, List<InfoType> infoTypes);

    /**
     * Create a DLP inspection template for reusable scanning rules.
     *
     * @param templateName The template name
     * @param infoTypes The info types to include
     * @param likelihoodThreshold The minimum likelihood threshold (VERY_LIKELY, LIKELY, POSSIBLE, UNLIKELY)
     * @return Template creation result
     */
    boolean createInspectionTemplate(String templateName, List<InfoType> infoTypes, Likelihood likelihoodThreshold);

    /**
     * Check if gateway is properly configured.
     *
     * @return true if configured and ready to use
     */
    boolean isConfigured();

    /**
     * Check health of the DLP service connection.
     *
     * @return true if connection is healthy
     */
    boolean isHealthy();

    /**
     * Get the provider identifier.
     *
     * @return The provider name (gcp, microsoft, aws, local)
     */
    String getProvider();

    /**
     * Get the cost of scanning operations (for billing/monitoring).
     *
     * @param operationType The type of operation (TEXT_SCAN, FILE_SCAN, etc.)
     * @param size The size of data scanned (bytes or characters)
     * @return Estimated cost in USD
     */
    double estimateCost(OperationType operationType, long size);

    /**
     * Sensitive information types.
     */
    enum InfoType {
        EMAIL_ADDRESS,
        PHONE_NUMBER,
        CREDIT_CARD_NUMBER,
        SSN,
        IBAN_CODE,
        IP_ADDRESS,
        MAC_ADDRESS,
        URL,
        DOMAIN_NAME,
        BIRTHDATE,
        PASSPORT_NUMBER,
        DRIVERS_LICENSE,
        BANK_ACCOUNT_NUMBER,
        ROUTING_NUMBER,
        MEDICAL_RECORD_NUMBER,
        HEALTH_PLAN_ID,
        PASSPORT,
        VISA_NUMBER
    }

    /**
     * Scan result.
     */
    record ScanResult(
        String scanId,
        boolean success,
        List<Finding> findings,
        long bytesScanned,
        long processingTimeMs,
        String message
    ) {}

    /**
     * Individual finding.
     */
    record Finding(
        InfoType infoType,
        String value,
        String valueRedacted,
        Likelihood likelihood,
        int startPosition,
        int endPosition,
        String quote
    ) {}

    /**
     * Likelihood levels.
     */
    enum Likelihood {
        VERY_UNLIKELY,
        UNLIKELY,
        POSSIBLE,
        LIKELY,
        VERY_LIKELY
    }

    /**
     * Operation types for cost estimation.
     */
    enum OperationType {
        TEXT_SCAN,
        FILE_SCAN,
        IMAGE_SCAN,
        DEIDENTIFICATION,
        STRUCTURED_INSPECTION
    }
}
