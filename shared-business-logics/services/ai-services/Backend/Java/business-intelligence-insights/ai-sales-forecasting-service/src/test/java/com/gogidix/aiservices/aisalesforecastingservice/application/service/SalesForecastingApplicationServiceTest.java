package com.gogidix.aiservices.aisalesforecastingservice.application.service;

import com.gogidix.aiservices.aisalesforecastingservice.application.command.AddForecastModelsCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.AnalyzeForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.CreateForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.RemoveForecastModelsCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.UpdateForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.SalesForecastingResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.ForecastAnalysisResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.mapper.SalesForecastingMapper;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.SalesForecast;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastStatus;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import com.gogidix.aiservices.aisalesforecastingservice.domain.port.out.SalesForecastingRepositoryPort;
import com.gogidix.aiservices.aisalesforecastingservice.domain.policy.ForecastBusinessPolicy;
import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("SalesForecastingApplicationService Tests")
class SalesForecastingApplicationServiceTest {

    @Mock
    private SalesForecastingRepositoryPort repository;

    @Mock
    private SalesForecastingMapper mapper;

    @Mock
    private ForecastBusinessPolicy businessPolicy;

    @Mock
    private ForecastAnalysisService analysisService;

    @InjectMocks
    private SalesForecastingApplicationService service;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String SEGMENT_ID = "segment-789";

    private SalesForecast testForecast;
    private SalesForecastingResponseDto testResponseDto;
    private ForecastCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = ForecastCriteria.builder()
                .type(ForecastCriteria.CriteriaType.CUSTOM)
                .operator(ForecastCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testForecast = SalesForecast.builder()
                .segmentId(SEGMENT_ID)
                .tenantId(TENANT_ID)
                .name("Premium ForecastModels")
                .description("High value customers")
                .criteria(testCriteria)
                .status(ForecastStatus.ACTIVE)
                .segmentType(ForecastType.BEHAVIORAL)
                .customerCount(100L)
                .build();

        testResponseDto = new SalesForecastingResponseDto(
                SEGMENT_ID,
                "Premium ForecastModels",
                "High value customers",
                ForecastType.BEHAVIORAL,
                Map.of("field", "lifetimeValue", "operator", ">", "value", 1000),
                null,
                100,
                true,
                TENANT_ID,
                null,
                null,
                null
        );
    }

    @Nested
    @DisplayName("createForecast() Tests")
    class CreateForecastTests {

        @Test
        @DisplayName("Should create segment successfully")
        void shouldCreateForecastSuccessfully() {
            CreateForecastCommand command = new CreateForecastCommand(
                    "Premium ForecastModels",
                    "High value customers",
                    ForecastType.BEHAVIORAL,
                    Map.of("field", "lifetimeValue", "operator", ">", "value", 1000),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(SalesForecast.class))).thenReturn(testForecast);
            when(mapper.toResponseDto(any(SalesForecast.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateForecastCreation(anyString());

            SalesForecastingResponseDto result = service.createForecast(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);

            ArgumentCaptor<SalesForecast> captor = ArgumentCaptor.forClass(SalesForecast.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium ForecastModels");
            verify(businessPolicy).validateForecastCreation(TENANT_ID);
        }

        @Test
        @DisplayName("Should throw exception when criteria map is null")
        void shouldThrowWhenCriteriaNull() {
            CreateForecastCommand command = new CreateForecastCommand(
                    "Premium ForecastModels",
                    "High value customers",
                    ForecastType.BEHAVIORAL,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createForecast(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when criteria map is empty")
        void shouldThrowWhenCriteriaEmpty() {
            CreateForecastCommand command = new CreateForecastCommand(
                    "Premium ForecastModels",
                    "High value customers",
                    ForecastType.BEHAVIORAL,
                    Map.of(),
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createForecast(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should use default values when criteria map missing fields")
        void shouldUseDefaultsWhenCriteriaMissingFields() {
            CreateForecastCommand command = new CreateForecastCommand(
                    "Premium ForecastModels",
                    "High value customers",
                    ForecastType.BEHAVIORAL,
                    Map.of("value", 500),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(SalesForecast.class))).thenReturn(testForecast);
            when(mapper.toResponseDto(any(SalesForecast.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateForecastCreation(anyString());

            SalesForecastingResponseDto result = service.createForecast(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(SalesForecast.class));
        }
    }

    @Nested
    @DisplayName("updateForecast() Tests")
    class UpdateForecastTests {

        @Test
        @DisplayName("Should update segment name and description")
        void shouldUpdateForecastNameAndDescription() {
            UpdateForecastCommand command = new UpdateForecastCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testForecast));
            when(repository.save(any(SalesForecast.class))).thenReturn(testForecast);
            when(mapper.toResponseDto(any(SalesForecast.class))).thenReturn(testResponseDto);

            SalesForecastingResponseDto result = service.updateForecast(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(SalesForecast.class));
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenForecastNotFound() {
            UpdateForecastCommand command = new UpdateForecastCommand(
                    "nonexistent-id",
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.updateForecast(command))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Forecast not found");
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            SalesForecast otherTenantForecast = SalesForecast.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Forecast")
                    .criteria(testCriteria)
                    .build();

            UpdateForecastCommand command = new UpdateForecastCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantForecast));

            assertThatThrownBy(() -> service.updateForecast(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should not update when name is null")
        void shouldNotUpdateWhenNameNull() {
            UpdateForecastCommand command = new UpdateForecastCommand(
                    SEGMENT_ID,
                    null,
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testForecast));
            when(repository.save(any(SalesForecast.class))).thenReturn(testForecast);
            when(mapper.toResponseDto(any(SalesForecast.class))).thenReturn(testResponseDto);

            service.updateForecast(command);

            ArgumentCaptor<SalesForecast> captor = ArgumentCaptor.forClass(SalesForecast.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium ForecastModels");
        }
    }

    @Nested
    @DisplayName("deleteForecast() Tests")
    class DeleteForecastTests {

        @Test
        @DisplayName("Should delete segment successfully")
        void shouldDeleteForecastSuccessfully() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testForecast));
            doNothing().when(businessPolicy).validateForecastDeletion(any(SalesForecast.class));
            doNothing().when(repository).deleteById(SEGMENT_ID);

            service.deleteForecast(SEGMENT_ID, TENANT_ID, USER_ID);

            verify(businessPolicy).validateForecastDeletion(testForecast);
            verify(repository).deleteById(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent segment")
        void shouldThrowWhenDeletingNonExistent() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteForecast("nonexistent-id", TENANT_ID, USER_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before deletion")
        void shouldValidatePolicyBeforeDeletion() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testForecast));
            doThrow(new ValidationException("Cannot delete active segment with customers"))
                    .when(businessPolicy).validateForecastDeletion(any(SalesForecast.class));

            assertThatThrownBy(() -> service.deleteForecast(SEGMENT_ID, TENANT_ID, USER_ID))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot delete active segment");

            verify(repository, never()).deleteById(anyString());
        }
    }

    @Nested
    @DisplayName("getForecastById() Tests")
    class GetForecastByIdTests {

        @Test
        @DisplayName("Should return segment when found")
        void shouldReturnForecastWhenFound() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testForecast));
            when(mapper.toResponseDto(any(SalesForecast.class))).thenReturn(testResponseDto);

            SalesForecastingResponseDto result = service.getForecastById(SEGMENT_ID, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenForecastNotFound() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getForecastById("nonexistent-id", TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            SalesForecast otherTenantForecast = SalesForecast.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Forecast")
                    .criteria(testCriteria)
                    .build();

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantForecast));

            assertThatThrownBy(() -> service.getForecastById(SEGMENT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getForecastsByTenant() Tests")
    class GetForecastsByTenantTests {

        @Test
        @DisplayName("Should return empty paged response")
        void shouldReturnEmptyPagedResponse() {
            PagedResponseDto<SalesForecastingResponseDto> result = service.getForecastsByTenant(
                    TENANT_ID,
                    null,
                    null,
                    0,
                    20,
                    "createdAt",
                    "DESC"
            );

            assertThat(result).isNotNull();
            assertThat(result.content()).isEmpty();
            assertThat(result.page()).isEqualTo(0);
            assertThat(result.size()).isEqualTo(20);
            assertThat(result.totalElements()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle different page parameters")
        void shouldHandleDifferentPageParams() {
            PagedResponseDto<SalesForecastingResponseDto> result = service.getForecastsByTenant(
                    TENANT_ID,
                    ForecastType.BEHAVIORAL,
                    true,
                    2,
                    50,
                    "name",
                    "ASC"
            );

            assertThat(result).isNotNull();
            assertThat(result.page()).isEqualTo(2);
            assertThat(result.size()).isEqualTo(50);
        }
    }

    @Nested
    @DisplayName("addForecastModelsToForecast() Tests")
    class AddForecastModelsTests {

        @Test
        @DisplayName("Should add customers successfully")
        void shouldAddForecastModelsSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2", "cust-3");
            AddForecastModelsCommand command = new AddForecastModelsCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testForecast));
            when(repository.save(any(SalesForecast.class))).thenReturn(testForecast);
            when(mapper.toResponseDto(any(SalesForecast.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateForecastModelAddition(any(), anyInt());

            SalesForecastingResponseDto result = service.addForecastModelsToForecast(command);

            assertThat(result).isNotNull();
            verify(businessPolicy).validateForecastModelAddition(testForecast, 3);
            verify(repository).save(testForecast);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenForecastNotFound() {
            List<String> customerIds = List.of("cust-1");
            AddForecastModelsCommand command = new AddForecastModelsCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.addForecastModelsToForecast(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before adding customers")
        void shouldValidatePolicyBeforeAdding() {
            List<String> customerIds = List.of("cust-1");
            AddForecastModelsCommand command = new AddForecastModelsCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testForecast));
            doThrow(new ValidationException("Forecast is full"))
                    .when(businessPolicy).validateForecastModelAddition(any(), anyInt());

            assertThatThrownBy(() -> service.addForecastModelsToForecast(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Forecast is full");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeForecastModelsFromForecast() Tests")
    class RemoveForecastModelsTests {

        @Test
        @DisplayName("Should remove customers successfully")
        void shouldRemoveForecastModelsSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2");
            RemoveForecastModelsCommand command = new RemoveForecastModelsCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testForecast));
            when(repository.save(any(SalesForecast.class))).thenReturn(testForecast);
            when(mapper.toResponseDto(any(SalesForecast.class))).thenReturn(testResponseDto);

            SalesForecastingResponseDto result = service.removeForecastModelsFromForecast(command);

            assertThat(result).isNotNull();
            verify(repository).save(testForecast);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenForecastNotFound() {
            List<String> customerIds = List.of("cust-1");
            RemoveForecastModelsCommand command = new RemoveForecastModelsCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeForecastModelsFromForecast(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("analyzeForecast() Tests")
    class AnalyzeForecastTests {

        @Test
        @DisplayName("Should analyze active segment successfully")
        void shouldAnalyzeActiveForecastSuccessfully() {
            Map<String, Object> analysisOptions = new HashMap<>();
            AnalyzeForecastCommand command = new AnalyzeForecastCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    analysisOptions
            );

            ForecastAnalysisResponseDto analysisResponse = new ForecastAnalysisResponseDto(
                    SEGMENT_ID,
                    java.time.Instant.now(),
                    100,
                    Map.of(),
                    Map.of(),
                    Map.of(),
                    Map.of(),
                    Map.of(),
                    0.85
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testForecast));
            when(analysisService.analyzeForecast(eq(testForecast), eq(analysisOptions)))
                    .thenReturn(analysisResponse);

            ForecastAnalysisResponseDto result = service.analyzeForecast(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
            assertThat(result.confidenceScore()).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should throw exception when analyzing inactive segment")
        void shouldThrowWhenAnalyzingInactiveForecast() {
            SalesForecast inactiveForecast = SalesForecast.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Forecast")
                    .criteria(testCriteria)
                    .status(ForecastStatus.INACTIVE)
                    .build();

            AnalyzeForecastCommand command = new AnalyzeForecastCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(inactiveForecast));

            assertThatThrownBy(() -> service.analyzeForecast(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when analyzing draft segment")
        void shouldThrowWhenAnalyzingDraftForecast() {
            SalesForecast draftForecast = SalesForecast.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Forecast")
                    .criteria(testCriteria)
                    .status(ForecastStatus.DRAFT)
                    .build();

            AnalyzeForecastCommand command = new AnalyzeForecastCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(draftForecast));

            assertThatThrownBy(() -> service.analyzeForecast(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalyzingNonExistent() {
            AnalyzeForecastCommand command = new AnalyzeForecastCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.analyzeForecast(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
