package com.gogidix.centralizeddashboard.reporting.service;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    private final FileStorageService service = new FileStorageService();

    @Test
    void storeFile_returnsUrl() {
        InputStream is = new ByteArrayInputStream("test data".getBytes());
        String url = service.storeFile("report.csv", "text/csv", is);
        assertNotNull(url);
        assertTrue(url.contains("report.csv"));
    }

    @Test
    void fileExists_returnsFalse() {
        assertFalse(service.fileExists("nonexistent.csv"));
    }

    @Test
    void deleteFile_returnsTrue() {
        assertTrue(service.deleteFile("report.csv"));
    }
}
