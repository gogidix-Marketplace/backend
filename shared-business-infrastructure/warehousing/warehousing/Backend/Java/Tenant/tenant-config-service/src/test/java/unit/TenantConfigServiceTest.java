package unit;

import com.gogidix.shared.warehousing.tenant.application.command.CreateTenantCommand;
import com.gogidix.shared.warehousing.tenant.application.command.UpdateTenantCommand;
import com.gogidix.shared.warehousing.tenant.application.mapper.TenantDtoMapper;
import com.gogidix.shared.warehousing.tenant.application.service.TenantConfigApplicationService;
import com.gogidix.shared.warehousing.tenant.domain.entity.Tenant;
import com.gogidix.shared.warehousing.tenant.domain.repository.TenantRepository;
import com.gogidix.shared.warehousing.tenant.infrastructure.messaging.TenantEventPublisher;
import com.gogidix.shared.warehousing.tenant.interfaces.rest.dto.TenantResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for Tenant Configuration Service
 */
@ExtendWith(MockitoExtension.class)
class TenantConfigServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private TenantDtoMapper tenantDtoMapper;

    @Mock
    private TenantEventPublisher eventPublisher;

    @InjectMocks
    private TenantConfigApplicationService tenantService;

    private Tenant testTenant;
    private CreateTenantCommand createCommand;
    private TenantResponse response;

    @BeforeEach
    void setUp() {
        Map<String, Object> businessRules = new HashMap<>();
        businessRules.put("maxLocations", 10);
        businessRules.put("allowVendorStorage", true);

        testTenant = Tenant.builder()
                .id("1")
                .tenantId("tenant-001")
                .tenantName("Test Tenant")
                .tenantType("ECOMMERCE_VENDOR")
                .storageModel("HYBRID")
                .businessRules(businessRules)
                .status("ACTIVE")
                .build();

        createCommand = CreateTenantCommand.builder()
                .tenantId("tenant-001")
                .tenantName("Test Tenant")
                .tenantType("ECOMMERCE_VENDOR")
                .storageModel("HYBRID")
                .businessRules(businessRules)
                .status("ACTIVE")
                .build();

        response = TenantResponse.builder()
                .id("1")
                .tenantId("tenant-001")
                .tenantName("Test Tenant")
                .tenantType("ECOMMERCE_VENDOR")
                .storageModel("HYBRID")
                .businessRules(businessRules)
                .status("ACTIVE")
                .build();
    }

    @Test
    void shouldCreateTenant() {
        // Given
        when(tenantRepository.existsByTenantId("tenant-001")).thenReturn(false);
        when(tenantDtoMapper.toEntity(createCommand)).thenReturn(testTenant);
        when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
        when(tenantDtoMapper.toDTO(testTenant)).thenReturn(response);

        // When
        TenantResponse result = tenantService.createTenant(createCommand);

        // Then
        assertNotNull(result);
        assertEquals("tenant-001", result.getTenantId());
        assertEquals("Test Tenant", result.getTenantName());
        verify(tenantRepository).save(any(Tenant.class));
        verify(eventPublisher).publishTenantCreated(any());
    }

    @Test
    void shouldThrowExceptionWhenTenantAlreadyExists() {
        // Given
        when(tenantRepository.existsByTenantId("tenant-001")).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            tenantService.createTenant(createCommand);
        });

        verify(tenantRepository, never()).save(any(Tenant.class));
    }

    @Test
    void shouldGetTenantByTenantId() {
        // Given
        when(tenantRepository.findByTenantId("tenant-001")).thenReturn(Optional.of(testTenant));
        when(tenantDtoMapper.toDTO(testTenant)).thenReturn(response);

        // When
        TenantResponse result = tenantService.getTenantByTenantId("tenant-001");

        // Then
        assertNotNull(result);
        assertEquals("tenant-001", result.getTenantId());
        verify(tenantRepository).findByTenantId("tenant-001");
    }

    @Test
    void shouldThrowExceptionWhenTenantNotFound() {
        // Given
        when(tenantRepository.findByTenantId("non-existent")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            tenantService.getTenantByTenantId("non-existent");
        });
    }

    @Test
    void shouldUpdateTenant() {
        // Given
        UpdateTenantCommand updateCommand = UpdateTenantCommand.builder()
                .tenantName("Updated Tenant")
                .status("SUSPENDED")
                .build();

        when(tenantRepository.findByTenantId("tenant-001")).thenReturn(Optional.of(testTenant));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
        when(tenantDtoMapper.toDTO(testTenant)).thenReturn(response);

        // When
        TenantResponse result = tenantService.updateTenant("tenant-001", updateCommand);

        // Then
        assertNotNull(result);
        verify(tenantRepository).save(any(Tenant.class));
    }

    @Test
    void shouldDeleteTenant() {
        // Given
        when(tenantRepository.findByTenantId("tenant-001")).thenReturn(Optional.of(testTenant));

        // When
        tenantService.deleteTenant("tenant-001");

        // Then
        verify(tenantRepository).delete(testTenant);
    }

    @Test
    void shouldGetBusinessRules() {
        // Given
        when(tenantRepository.findByTenantId("tenant-001")).thenReturn(Optional.of(testTenant));

        // When
        Object rules = tenantService.getBusinessRules("tenant-001");

        // Then
        assertNotNull(rules);
        verify(tenantRepository).findByTenantId("tenant-001");
    }

    @Test
    void shouldUpdateBusinessRules() {
        // Given
        Map<String, Object> newRules = new HashMap<>();
        newRules.put("maxLocations", 20);

        when(tenantRepository.findByTenantId("tenant-001")).thenReturn(Optional.of(testTenant));
        when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
        when(tenantDtoMapper.toDTO(testTenant)).thenReturn(response);

        // When
        TenantResponse result = tenantService.updateBusinessRules("tenant-001", newRules);

        // Then
        assertNotNull(result);
        verify(tenantRepository).save(any(Tenant.class));
    }

    @Test
    void shouldGetAllTenants() {
        // Given
        when(tenantRepository.findAll()).thenReturn(java.util.List.of(testTenant));
        when(tenantDtoMapper.toDTOList(any())).thenReturn(java.util.List.of(response));

        // When
        var result = tenantService.getAllTenants();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tenantRepository).findAll();
    }
}
