package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResearchData Domain Model Tests")
class ResearchDataTest {

    @Test
    @DisplayName("Should create research data successfully")
    void shouldCreateResearchData() {
        ResearchData data = new ResearchData(
                "data-001",
                "proj-001",
                "Training Dataset",
                "Dataset for ML training",
                ResearchData.DataType.STRUCTURED,
                "/data/datasets/training",
                "uploader@example.com"
        );

        assertNotNull(data);
        assertEquals("data-001", data.getDatasetId());
        assertEquals("proj-001", data.getProjectId());
        assertEquals("Training Dataset", data.getName());
        assertEquals("Dataset for ML training", data.getDescription());
        assertEquals(ResearchData.DataType.STRUCTURED, data.getDataType());
        assertEquals("/data/datasets/training", data.getStorageLocation());
        assertEquals("uploader@example.com", data.getUploadedBy());
        assertEquals(ResearchData.DataStatus.ACTIVE, data.getStatus());
        assertEquals(0, data.getRecordCount());
        assertEquals("1.0", data.getSchemaVersion());
    }

    @Test
    @DisplayName("Should throw exception when datasetId is null")
    void shouldThrowWhenDatasetIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchData(
                        null,
                        "proj-001",
                        "Name",
                        "Description",
                        ResearchData.DataType.STRUCTURED,
                        "/data",
                        "uploader@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when projectId is null")
    void shouldThrowWhenProjectIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchData(
                        "data-001",
                        null,
                        "Name",
                        "Description",
                        ResearchData.DataType.STRUCTURED,
                        "/data",
                        "uploader@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowWhenNameIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchData(
                        "data-001",
                        "proj-001",
                        null,
                        "Description",
                        ResearchData.DataType.STRUCTURED,
                        "/data",
                        "uploader@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when dataType is null")
    void shouldThrowWhenDataTypeIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchData(
                        "data-001",
                        "proj-001",
                        "Name",
                        "Description",
                        null,
                        "/data",
                        "uploader@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should allow null description")
    void shouldAllowNullDescription() {
        ResearchData data = new ResearchData(
                "data-001",
                "proj-001",
                "Name",
                null,
                ResearchData.DataType.STRUCTURED,
                "/data",
                "uploader@example.com"
        );

        assertNull(data.getDescription());
    }

    @Test
    @DisplayName("Should allow null storageLocation")
    void shouldAllowNullStorageLocation() {
        ResearchData data = new ResearchData(
                "data-001",
                "proj-001",
                "Name",
                "Description",
                ResearchData.DataType.STRUCTURED,
                null,
                "uploader@example.com"
        );

        assertNull(data.getStorageLocation());
    }

    @Test
    @DisplayName("Should allow null uploadedBy")
    void shouldAllowNullUploadedBy() {
        ResearchData data = new ResearchData(
                "data-001",
                "proj-001",
                "Name",
                "Description",
                ResearchData.DataType.STRUCTURED,
                "/data",
                null
        );

        assertNull(data.getUploadedBy());
    }

    @Test
    @DisplayName("Should verify uploadedAt is set to current time")
    void shouldVerifyUploadedAt() {
        LocalDateTime before = LocalDateTime.now();
        ResearchData data = new ResearchData(
                "data-001",
                "proj-001",
                "Name",
                "Description",
                ResearchData.DataType.STRUCTURED,
                "/data",
                "uploader@example.com"
        );
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(data.getUploadedAt());
        assertTrue(data.getUploadedAt().isBefore(after) || data.getUploadedAt().isEqual(after));
        assertTrue(data.getUploadedAt().isAfter(before) || data.getUploadedAt().isEqual(before));
    }

    @Test
    @DisplayName("Should verify equals based on datasetId")
    void shouldVerifyEquals() {
        ResearchData data1 = new ResearchData(
                "data-001",
                "proj-001",
                "Dataset 1",
                "Description 1",
                ResearchData.DataType.STRUCTURED,
                "/data1",
                "uploader1@example.com"
        );

        ResearchData data2 = new ResearchData(
                "data-001",
                "proj-002",
                "Dataset 2",
                "Description 2",
                ResearchData.DataType.UNSTRUCTURED,
                "/data2",
                "uploader2@example.com"
        );

        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());
    }

    @Test
    @DisplayName("Should not equal different datasets")
    void shouldNotEqualDifferentDatasets() {
        ResearchData data1 = new ResearchData(
                "data-001",
                "proj-001",
                "Dataset",
                "Description",
                ResearchData.DataType.STRUCTURED,
                "/data",
                "uploader@example.com"
        );

        ResearchData data2 = new ResearchData(
                "data-002",
                "proj-001",
                "Dataset",
                "Description",
                ResearchData.DataType.STRUCTURED,
                "/data",
                "uploader@example.com"
        );

        assertNotEquals(data1, data2);
    }

    @Test
    @DisplayName("Should verify toString contains key fields")
    void shouldVerifyToString() {
        ResearchData data = new ResearchData(
                "data-001",
                "proj-001",
                "Training Dataset",
                "ML training data",
                ResearchData.DataType.STRUCTURED,
                "/data/training",
                "uploader@example.com"
        );

        String result = data.toString();

        assertTrue(result.contains("data-001"));
        assertTrue(result.contains("proj-001"));
        assertTrue(result.contains("Training Dataset"));
        assertTrue(result.contains("STRUCTURED"));
        assertTrue(result.contains("ACTIVE"));
    }

    @Test
    @DisplayName("Should create dataset with all data types")
    void shouldCreateWithAllDataTypes() {
        ResearchData[] datasets = {
                new ResearchData("d1", "p1", "N", "D", ResearchData.DataType.STRUCTURED, "/d", "u"),
                new ResearchData("d2", "p2", "N", "D", ResearchData.DataType.UNSTRUCTURED, "/d", "u"),
                new ResearchData("d3", "p3", "N", "D", ResearchData.DataType.SEMI_STRUCTURED, "/d", "u"),
                new ResearchData("d4", "p4", "N", "D", ResearchData.DataType.TIME_SERIES, "/d", "u"),
                new ResearchData("d5", "p5", "N", "D", ResearchData.DataType.IMAGE, "/d", "u"),
                new ResearchData("d6", "p6", "N", "D", ResearchData.DataType.VIDEO, "/d", "u"),
                new ResearchData("d7", "p7", "N", "D", ResearchData.DataType.AUDIO, "/d", "u"),
                new ResearchData("d8", "p8", "N", "D", ResearchData.DataType.TEXT, "/d", "u"),
                new ResearchData("d9", "p9", "N", "D", ResearchData.DataType.NUMERICAL, "/d", "u"),
                new ResearchData("d10", "p10", "N", "D", ResearchData.DataType.CATEGORICAL, "/d", "u")
        };

        assertEquals(ResearchData.DataType.STRUCTURED, datasets[0].getDataType());
        assertEquals(ResearchData.DataType.CATEGORICAL, datasets[9].getDataType());
    }

    @Test
    @DisplayName("Should create dataset with all statuses")
    void shouldCreateWithAllStatuses() {
        ResearchData activeData = new ResearchData(
                "d1", "p1", "N", "D", ResearchData.DataType.STRUCTURED, "/d", "u"
        );
        assertEquals(ResearchData.DataStatus.ACTIVE, activeData.getStatus());
    }
}
