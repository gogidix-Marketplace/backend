package com.gogidix.corporatecms.application.dto;

import com.gogidix.corporatecms.application.dto.MediaDTO;
import com.gogidix.corporatecms.domain.enums.MediaType;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MediaDTOTest {

        @Test
    void testBuilder() {
        MediaDTO dto = MediaDTO.builder()
                        .id("test-id")
            .fileName("test-fileName")
            .originalFileName("test-originalFileName")
            .mediaType(MediaType.IMAGE)
            .mimeType("test-mimeType")
            .extension("test-extension")
            .fileSize(42L)
            .filePath("test-filePath")
            .cdnUrl("test-cdnUrl")
            .storageProvider("test-storageProvider")
            .folder("test-folder")
            .altText("test-altText")
            .caption("test-caption")
            .description("test-description")
            .uploadedBy("test-uploadedBy")
            .uploadedByName("test-uploadedByName")
            .width(42)
            .height(42)
            .duration(42)
            .metadata(Collections.emptyMap())
            .thumbnailPath("test-thumbnailPath")
            .optimized(true)
            .transformations(Collections.emptyMap())
            .downloadCount(42)
            .usageCount(42)
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-originalFileName", dto.getOriginalFileName());
        assertEquals("test-mimeType", dto.getMimeType());
        assertEquals("test-extension", dto.getExtension());
        assertEquals(42L, dto.getFileSize());
        assertEquals("test-filePath", dto.getFilePath());
        assertEquals("test-cdnUrl", dto.getCdnUrl());
        assertEquals("test-storageProvider", dto.getStorageProvider());
        assertEquals("test-folder", dto.getFolder());
        assertEquals("test-altText", dto.getAltText());
        assertEquals("test-caption", dto.getCaption());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-uploadedBy", dto.getUploadedBy());
        assertEquals("test-uploadedByName", dto.getUploadedByName());
        assertEquals(42, dto.getWidth());
        assertEquals(42, dto.getHeight());
        assertEquals(42, dto.getDuration());
        assertEquals("test-thumbnailPath", dto.getThumbnailPath());
        assertTrue(dto.getOptimized());
        assertEquals(42, dto.getDownloadCount());
        assertEquals(42, dto.getUsageCount());
    }

    @Test
    void testSettersAndGetters() {
        MediaDTO dto = new MediaDTO();
        dto.setId("val-id");
        dto.setFileName("val-fileName");
        dto.setOriginalFileName("val-originalFileName");
        dto.setMimeType("val-mimeType");
        dto.setExtension("val-extension");
        dto.setFilePath("val-filePath");
        dto.setCdnUrl("val-cdnUrl");
        dto.setStorageProvider("val-storageProvider");
        dto.setFolder("val-folder");
        dto.setAltText("val-altText");
        dto.setCaption("val-caption");
        dto.setDescription("val-description");
        dto.setUploadedBy("val-uploadedBy");
        dto.setUploadedByName("val-uploadedByName");
        dto.setWidth(99);
        dto.setHeight(99);
        dto.setDuration(99);
        dto.setThumbnailPath("val-thumbnailPath");
        dto.setOptimized(true);
        dto.setDownloadCount(99);
        dto.setUsageCount(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-originalFileName", dto.getOriginalFileName());
        assertEquals("val-mimeType", dto.getMimeType());
        assertEquals("val-extension", dto.getExtension());
        assertEquals("val-filePath", dto.getFilePath());
        assertEquals("val-cdnUrl", dto.getCdnUrl());
        assertEquals("val-storageProvider", dto.getStorageProvider());
        assertEquals("val-folder", dto.getFolder());
        assertEquals("val-altText", dto.getAltText());
        assertEquals("val-caption", dto.getCaption());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-uploadedBy", dto.getUploadedBy());
        assertEquals("val-uploadedByName", dto.getUploadedByName());
        assertEquals(99, dto.getWidth());
        assertEquals(99, dto.getHeight());
        assertEquals(99, dto.getDuration());
        assertEquals("val-thumbnailPath", dto.getThumbnailPath());
        assertTrue(dto.getOptimized());
        assertEquals(99, dto.getDownloadCount());
        assertEquals(99, dto.getUsageCount());
    }

    @Test
    void testEqualsAndHashCode() {
        MediaDTO dto1 = MediaDTO.builder()
                        .id("test-id")
            .fileName("test-fileName")
            .originalFileName("test-originalFileName")
            .mediaType(MediaType.IMAGE)
            .mimeType("test-mimeType")
            .extension("test-extension")
            .fileSize(42L)
            .filePath("test-filePath")
            .cdnUrl("test-cdnUrl")
            .storageProvider("test-storageProvider")
            .folder("test-folder")
            .altText("test-altText")
            .caption("test-caption")
            .description("test-description")
            .uploadedBy("test-uploadedBy")
            .uploadedByName("test-uploadedByName")
            .width(42)
            .height(42)
            .duration(42)
            .metadata(Collections.emptyMap())
            .thumbnailPath("test-thumbnailPath")
            .optimized(true)
            .transformations(Collections.emptyMap())
            .downloadCount(42)
            .usageCount(42)
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        MediaDTO dto2 = MediaDTO.builder()
                        .id("test-id")
            .fileName("test-fileName")
            .originalFileName("test-originalFileName")
            .mediaType(MediaType.IMAGE)
            .mimeType("test-mimeType")
            .extension("test-extension")
            .fileSize(42L)
            .filePath("test-filePath")
            .cdnUrl("test-cdnUrl")
            .storageProvider("test-storageProvider")
            .folder("test-folder")
            .altText("test-altText")
            .caption("test-caption")
            .description("test-description")
            .uploadedBy("test-uploadedBy")
            .uploadedByName("test-uploadedByName")
            .width(42)
            .height(42)
            .duration(42)
            .metadata(Collections.emptyMap())
            .thumbnailPath("test-thumbnailPath")
            .optimized(true)
            .transformations(Collections.emptyMap())
            .downloadCount(42)
            .usageCount(42)
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MediaDTO dto = MediaDTO.builder()
                        .id("test-id")
            .fileName("test-fileName")
            .originalFileName("test-originalFileName")
            .mediaType(MediaType.IMAGE)
            .mimeType("test-mimeType")
            .extension("test-extension")
            .fileSize(42L)
            .filePath("test-filePath")
            .cdnUrl("test-cdnUrl")
            .storageProvider("test-storageProvider")
            .folder("test-folder")
            .altText("test-altText")
            .caption("test-caption")
            .description("test-description")
            .uploadedBy("test-uploadedBy")
            .uploadedByName("test-uploadedByName")
            .width(42)
            .height(42)
            .duration(42)
            .metadata(Collections.emptyMap())
            .thumbnailPath("test-thumbnailPath")
            .optimized(true)
            .transformations(Collections.emptyMap())
            .downloadCount(42)
            .usageCount(42)
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}