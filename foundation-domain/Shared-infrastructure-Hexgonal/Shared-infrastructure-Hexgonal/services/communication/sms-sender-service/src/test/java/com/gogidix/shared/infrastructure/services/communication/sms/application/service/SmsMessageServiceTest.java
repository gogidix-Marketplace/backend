package com.gogidix.shared.infrastructure.services.communication.sms.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.request.CreateSmsMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.response.SmsMessageResponseDto;
import com.gogidix.shared.infrastructure.services.communication.sms.application.mapper.SmsMessageMapper;
import com.gogidix.shared.infrastructure.services.communication.sms.domain.model.SmsMessage;
import com.gogidix.shared.infrastructure.services.communication.sms.domain.port.in.ISmsMessageUseCase;
import com.gogidix.shared.infrastructure.services.communication.sms.domain.port.out.ISmsMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SmsMessageService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SMS Message Service Tests")
class SmsMessageServiceTest {

    @Mock
    private SmsMessageMapper mapper;

    @Mock
    private ISmsMessageRepository repository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private SmsMessageService smsMessageService;

    private static final String TENANT_ID = "tenant123";

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
    }

    @Test
    @DisplayName("Should create SMS message")
    void shouldCreateSmsMessage() {
        CreateSmsMessageRequestDto dto = new CreateSmsMessageRequestDto(
                "+1234567890", "+1", "Test message", "verify-template", "TWILIO", "campaign123"
        );

        SmsMessage entity = new SmsMessage();
        entity.setId("sms123");
        entity.setPhoneNumber("+1234567890");

        when(mapper.toEntity(dto, TENANT_ID)).thenReturn(entity);
        when(repository.save(any(SmsMessage.class))).thenReturn(entity);
        when(mapper.toResponseDto(any())).thenReturn(new SmsMessageResponseDto(
                "sms123", TENANT_ID, "+1234567890", "+1", "PENDING",
                "Test message", "verify-template", "TWILIO", "campaign123",
                null, null, 0, 3,
                LocalDateTime.now(), null, null
        ));

        SmsMessageResponseDto response = smsMessageService.create(dto);

        assertNotNull(response);
        assertEquals("sms123", response.id());
        verify(mapper).toEntity(dto, TENANT_ID);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should find SMS by ID")
    void shouldFindSmsById() {
        SmsMessage entity = new SmsMessage();
        entity.setId("sms123");
        entity.setPhoneNumber("+1234567890");

        when(repository.findByIdAndTenantId("sms123", TENANT_ID)).thenReturn(entity);
        when(mapper.toResponseDto(any())).thenReturn(new SmsMessageResponseDto(
                "sms123", TENANT_ID, "+1234567890", "+1", "PENDING",
                "Test message", null, "TWILIO", null,
                null, null, 0, 3,
                LocalDateTime.now(), null, null
        ));

        SmsMessageResponseDto response = smsMessageService.findById("sms123");

        assertNotNull(response);
        assertEquals("sms123", response.id());
        verify(repository).findByIdAndTenantId("sms123", TENANT_ID);
    }

    @Test
    @DisplayName("Should throw exception when SMS not found by ID")
    void shouldThrowExceptionWhenSmsNotFoundById() {
        when(repository.findByIdAndTenantId("sms123", TENANT_ID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> smsMessageService.findById("sms123"));
    }

    @Test
    @DisplayName("Should find all SMS messages")
    void shouldFindAllSmsMessages() {
        SmsMessage entity1 = new SmsMessage();
        entity1.setId("sms1");
        SmsMessage entity2 = new SmsMessage();
        entity2.setId("sms2");

        when(repository.findAllByTenantId(TENANT_ID)).thenReturn(List.of(entity1, entity2));
        when(mapper.toResponseDto(any())).thenReturn(new SmsMessageResponseDto(
                "sms1", TENANT_ID, "+1234567890", "+1", "PENDING",
                "Test message", null, "TWILIO", null,
                null, null, 0, 3,
                LocalDateTime.now(), null, null
        ));

        List<SmsMessageResponseDto> response = smsMessageService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    @DisplayName("Should find SMS messages by phone number")
    void shouldFindSmsMessagesByPhoneNumber() {
        SmsMessage entity = new SmsMessage();
        entity.setId("sms123");

        when(repository.findByPhoneNumberAndTenantId("+1234567890", TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new SmsMessageResponseDto(
                "sms123", TENANT_ID, "+1234567890", "+1", "PENDING",
                "Test message", null, "TWILIO", null,
                null, null, 0, 3,
                LocalDateTime.now(), null, null
        ));

        List<SmsMessageResponseDto> response = smsMessageService.findByPhoneNumber("+1234567890");

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(repository).findByPhoneNumberAndTenantId("+1234567890", TENANT_ID);
    }

    @Test
    @DisplayName("Should find SMS messages by status")
    void shouldFindSmsMessagesByStatus() {
        SmsMessage entity = new SmsMessage();
        entity.setId("sms123");

        when(repository.findByStatusAndTenantId("SENT", TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new SmsMessageResponseDto(
                "sms123", TENANT_ID, "+1234567890", "+1", "SENT",
                "Test message", null, "TWILIO", null,
                null, null, 0, 3,
                LocalDateTime.now(), null, null
        ));

        List<SmsMessageResponseDto> response = smsMessageService.findByStatus("SENT");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should find SMS messages by campaign ID")
    void shouldFindSmsMessagesByCampaignId() {
        SmsMessage entity = new SmsMessage();
        entity.setId("sms123");

        when(repository.findByCampaignIdAndTenantId("campaign123", TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new SmsMessageResponseDto(
                "sms123", TENANT_ID, "+1234567890", "+1", "PENDING",
                "Test message", null, "TWILIO", "campaign123",
                null, null, 0, 3,
                LocalDateTime.now(), null, null
        ));

        List<SmsMessageResponseDto> response = smsMessageService.findByCampaignId("campaign123");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should delete SMS by ID")
    void shouldDeleteSmsById() {
        SmsMessage entity = new SmsMessage();
        entity.setId("sms123");

        when(repository.findByIdAndTenantId("sms123", TENANT_ID)).thenReturn(entity);
        doNothing().when(repository).deleteByIdAndTenantId("sms123", TENANT_ID);

        smsMessageService.delete("sms123");

        verify(repository).deleteByIdAndTenantId("sms123", TENANT_ID);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent SMS")
    void shouldThrowExceptionWhenDeletingNonExistentSms() {
        when(repository.findByIdAndTenantId("sms123", TENANT_ID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> smsMessageService.delete("sms123"));
    }

    @Test
    @DisplayName("Should cleanup old messages")
    void shouldCleanupOldMessages() {
        doNothing().when(repository).deleteOldByTenantIdAndCreatedAt(eq(TENANT_ID), any(LocalDateTime.class));

        smsMessageService.cleanupOldMessages(30);

        verify(repository).deleteOldByTenantIdAndCreatedAt(eq(TENANT_ID), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should get SMS statistics")
    void shouldGetSmsStatistics() {
        when(repository.countByTenantId(TENANT_ID)).thenReturn(100L);
        when(repository.countByStatusAndTenantId("PENDING", TENANT_ID)).thenReturn(10L);
        when(repository.countByStatusAndTenantId("SENT", TENANT_ID)).thenReturn(70L);
        when(repository.countByStatusAndTenantId("DELIVERED", TENANT_ID)).thenReturn(15L);
        when(repository.countByStatusAndTenantId("FAILED", TENANT_ID)).thenReturn(5L);

        ISmsMessageUseCase.SmsMessageStatsDto stats = smsMessageService.getStats();

        assertNotNull(stats);
        assertEquals(100, stats.totalCount());
        assertEquals(10, stats.pendingCount());
        assertEquals(70, stats.sentCount());
        assertEquals(15, stats.deliveredCount());
        assertEquals(5, stats.failedCount());
    }
}
