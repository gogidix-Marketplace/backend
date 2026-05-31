package com.gogidix.digitalmarketing.brandmanagement.application.dto;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DtoBuilderTest {

    @Test
    void testBrandAssetResponseDtoBuilder() {
        var dto = BrandAssetResponseDto.builder()
            .id("id1").tenantId("t1").name("name1").type("type1")
            .url("url1").category("cat1").description("desc1")
            .status("active").version("1.0").fileFormat("png")
            .fileSize("1MB").storageLocation("s3://bucket")
            .isActive("true").isPublic("false")
            .createdBy("user1").createdAt(Instant.now()).updatedAt(Instant.now())
            .build();
        assertNotNull(dto);
        assertEquals("id1", dto.getId());
        assertEquals("t1", dto.getTenantId());
        dto.toString();
        dto.hashCode();
        assertEquals(dto, dto);
    }

    @Test
    void testBrandAssetResponseDtoNoArgs() {
        var dto = new BrandAssetResponseDto();
        assertNotNull(dto);
        dto.setName("test");
        assertEquals("test", dto.getName());
    }

    @Test
    void testBrandAssetRequestDto() {
        var dto = new BrandAssetRequestDto();
        dto.setName("test");
        assertEquals("test", dto.getName());
    }
}
