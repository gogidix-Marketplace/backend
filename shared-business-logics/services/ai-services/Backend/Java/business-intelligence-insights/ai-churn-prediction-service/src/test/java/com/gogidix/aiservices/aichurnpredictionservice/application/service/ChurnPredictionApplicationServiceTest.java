package com.gogidix.aiservices.aichurnpredictionservice.application.service;

import com.gogidix.aiservices.aichurnpredictionservice.application.command.AddModelsToPredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.AnalyzePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.CreatePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.RemoveModelsFromPredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.UpdatePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.ChurnPredictionResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.PredictionAnalysisResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.mapper.ChurnPredictionMapper;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.ChurnPrediction;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionCriteria;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionStatus;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import com.gogidix.aiservices.aichurnpredictionservice.domain.port.out.ChurnPredictionRepositoryPort;
import com.gogidix.aiservices.aichurnpredictionservice.domain.policy.PredictionBusinessPolicy;
import com.gogidix.aiservices.aichurnpredictionservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aichurnpredictionservice.shared.exception.ValidationException;
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
@DisplayName("ChurnPredictionApplicationService Tests")
class ChurnPredictionApplicationServiceTest {

    @Mock
    private ChurnPredictionRepositoryPort repository;

    @Mock
    private ChurnPredictionMapper mapper;

    @Mock
    private PredictionBusinessPolicy businessPolicy;

    @Mock
    private PredictionAnalysisService analysisService;

    @InjectMocks
    private ChurnPredictionApplicationService service;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String SEGMENT_ID = "segment-789";

    private ChurnPrediction testPrediction;
    private ChurnPredictionResponseDto testResponseDto;
    private PredictionCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = PredictionCriteria.builder()
                .type(PredictionCriteria.CriteriaType.CUSTOM)
                .operator(PredictionCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testPrediction = ChurnPrediction.builder()
                .segmentId(SEGMENT_ID)
                .tenantId(TENANT_ID)
                .name("Premium Customers")
                .description("High value customers")
                .criteria(testCriteria)
                .status(PredictionStatus.ACTIVE)
                .segmentType(PredictionType.BEHAVIORAL)
                .customerCount(100L)
                .build();

        testResponseDto = new ChurnPredictionResponseDto(
                SEGMENT_ID,
                "Premium Customers",
                "High value customers",
                PredictionType.BEHAVIORAL,
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
    @DisplayName("createPrediction() Tests")
    class CreatePredictionTests {

        @Test
        @DisplayName("Should create segment successfully")
        void shouldCreatePredictionSuccessfully() {
            CreatePredictionCommand command = new CreatePredictionCommand(
                    "Premium Customers",
                    "High value customers",
                    PredictionType.BEHAVIORAL,
                    Map.of("field", "lifetimeValue", "operator", ">", "value", 1000),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(ChurnPrediction.class))).thenReturn(testPrediction);
            when(mapper.toResponseDto(any(ChurnPrediction.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validatePredictionCreation(anyString());

            ChurnPredictionResponseDto result = service.createPrediction(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);

            ArgumentCaptor<ChurnPrediction> captor = ArgumentCaptor.forClass(ChurnPrediction.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Customers");
            verify(businessPolicy).validatePredictionCreation(TENANT_ID);
        }

        @Test
        @DisplayName("Should use default values when criteria map missing fields")
        void shouldUseDefaultsWhenCriteriaMissingFields() {
            CreatePredictionCommand command = new CreatePredictionCommand(
                    "Premium Customers",
                    "High value customers",
                    PredictionType.BEHAVIORAL,
                    Map.of("value", 500),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(ChurnPrediction.class))).thenReturn(testPrediction);
            when(mapper.toResponseDto(any(ChurnPrediction.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validatePredictionCreation(anyString());

            ChurnPredictionResponseDto result = service.createPrediction(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(ChurnPrediction.class));
        }
    }

    @Nested
    @DisplayName("updatePrediction() Tests")
    class UpdatePredictionTests {

        @Test
        @DisplayName("Should update segment name and description")
        void shouldUpdatePredictionNameAndDescription() {
            UpdatePredictionCommand command = new UpdatePredictionCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testPrediction));
            when(repository.save(any(ChurnPrediction.class))).thenReturn(testPrediction);
            when(mapper.toResponseDto(any(ChurnPrediction.class))).thenReturn(testResponseDto);

            ChurnPredictionResponseDto result = service.updatePrediction(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(ChurnPrediction.class));
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenPredictionNotFound() {
            UpdatePredictionCommand command = new UpdatePredictionCommand(
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

            assertThatThrownBy(() -> service.updatePrediction(command))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Prediction not found");
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            ChurnPrediction otherTenantPrediction = ChurnPrediction.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Prediction")
                    .criteria(testCriteria)
                    .build();

            UpdatePredictionCommand command = new UpdatePredictionCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantPrediction));

            assertThatThrownBy(() -> service.updatePrediction(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should not update when name is null")
        void shouldNotUpdateWhenNameNull() {
            UpdatePredictionCommand command = new UpdatePredictionCommand(
                    SEGMENT_ID,
                    null,
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testPrediction));
            when(repository.save(any(ChurnPrediction.class))).thenReturn(testPrediction);
            when(mapper.toResponseDto(any(ChurnPrediction.class))).thenReturn(testResponseDto);

            service.updatePrediction(command);

            ArgumentCaptor<ChurnPrediction> captor = ArgumentCaptor.forClass(ChurnPrediction.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Customers");
        }
    }

    @Nested
    @DisplayName("deletePrediction() Tests")
    class DeletePredictionTests {

        @Test
        @DisplayName("Should delete segment successfully")
        void shouldDeletePredictionSuccessfully() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testPrediction));
            doNothing().when(businessPolicy).validatePredictionDeletion(any(ChurnPrediction.class));
            doNothing().when(repository).deleteById(SEGMENT_ID);

            service.deletePrediction(SEGMENT_ID, TENANT_ID, USER_ID);

            verify(businessPolicy).validatePredictionDeletion(testPrediction);
            verify(repository).deleteById(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent segment")
        void shouldThrowWhenDeletingNonExistent() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deletePrediction("nonexistent-id", TENANT_ID, USER_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before deletion")
        void shouldValidatePolicyBeforeDeletion() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testPrediction));
            doThrow(new ValidationException("Cannot delete active segment with customers"))
                    .when(businessPolicy).validatePredictionDeletion(any(ChurnPrediction.class));

            assertThatThrownBy(() -> service.deletePrediction(SEGMENT_ID, TENANT_ID, USER_ID))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot delete active segment");

            verify(repository, never()).deleteById(anyString());
        }
    }

    @Nested
    @DisplayName("getPredictionById() Tests")
    class GetPredictionByIdTests {

        @Test
        @DisplayName("Should return segment when found")
        void shouldReturnPredictionWhenFound() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testPrediction));
            when(mapper.toResponseDto(any(ChurnPrediction.class))).thenReturn(testResponseDto);

            ChurnPredictionResponseDto result = service.getPredictionById(SEGMENT_ID, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenPredictionNotFound() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getPredictionById("nonexistent-id", TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            ChurnPrediction otherTenantPrediction = ChurnPrediction.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Prediction")
                    .criteria(testCriteria)
                    .build();

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantPrediction));

            assertThatThrownBy(() -> service.getPredictionById(SEGMENT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getPredictionsByTenant() Tests")
    class GetPredictionsByTenantTests {

        @Test
        @DisplayName("Should return empty paged response")
        void shouldReturnEmptyPagedResponse() {
            PagedResponseDto<ChurnPredictionResponseDto> result = service.getPredictionsByTenant(
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
            PagedResponseDto<ChurnPredictionResponseDto> result = service.getPredictionsByTenant(
                    TENANT_ID,
                    PredictionType.BEHAVIORAL,
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
    @DisplayName("addCustomersToPrediction() Tests")
    class AddCustomersTests {

        @Test
        @DisplayName("Should add customers successfully")
        void shouldAddCustomersSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2", "cust-3");
            AddModelsToPredictionCommand command = new AddModelsToPredictionCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testPrediction));
            when(repository.save(any(ChurnPrediction.class))).thenReturn(testPrediction);
            when(mapper.toResponseDto(any(ChurnPrediction.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateCustomerAddition(any(), anyInt());

            ChurnPredictionResponseDto result = service.addCustomersToPrediction(command);

            assertThat(result).isNotNull();
            verify(businessPolicy).validateCustomerAddition(testPrediction, 3);
            verify(repository).save(testPrediction);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenPredictionNotFound() {
            List<String> customerIds = List.of("cust-1");
            AddModelsToPredictionCommand command = new AddModelsToPredictionCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.addCustomersToPrediction(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before adding customers")
        void shouldValidatePolicyBeforeAdding() {
            List<String> customerIds = List.of("cust-1");
            AddModelsToPredictionCommand command = new AddModelsToPredictionCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testPrediction));
            doThrow(new ValidationException("Prediction is full"))
                    .when(businessPolicy).validateCustomerAddition(any(), anyInt());

            assertThatThrownBy(() -> service.addCustomersToPrediction(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Prediction is full");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeCustomersFromPrediction() Tests")
    class RemoveCustomersTests {

        @Test
        @DisplayName("Should remove customers successfully")
        void shouldRemoveCustomersSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2");
            RemoveModelsFromPredictionCommand command = new RemoveModelsFromPredictionCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testPrediction));
            when(repository.save(any(ChurnPrediction.class))).thenReturn(testPrediction);
            when(mapper.toResponseDto(any(ChurnPrediction.class))).thenReturn(testResponseDto);

            ChurnPredictionResponseDto result = service.removeCustomersFromPrediction(command);

            assertThat(result).isNotNull();
            verify(repository).save(testPrediction);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenPredictionNotFound() {
            List<String> customerIds = List.of("cust-1");
            RemoveModelsFromPredictionCommand command = new RemoveModelsFromPredictionCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeCustomersFromPrediction(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("analyzePrediction() Tests")
    class AnalyzePredictionTests {

        @Test
        @DisplayName("Should analyze active segment successfully")
        void shouldAnalyzeActivePredictionSuccessfully() {
            Map<String, Object> analysisOptions = new HashMap<>();
            AnalyzePredictionCommand command = new AnalyzePredictionCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    analysisOptions
            );

            PredictionAnalysisResponseDto analysisResponse = new PredictionAnalysisResponseDto(
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

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testPrediction));
            when(analysisService.analyzePrediction(eq(testPrediction), eq(analysisOptions)))
                    .thenReturn(analysisResponse);

            PredictionAnalysisResponseDto result = service.analyzePrediction(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
            assertThat(result.confidenceScore()).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should throw exception when analyzing inactive segment")
        void shouldThrowWhenAnalyzingInactivePrediction() {
            ChurnPrediction inactivePrediction = ChurnPrediction.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Prediction")
                    .criteria(testCriteria)
                    .status(PredictionStatus.INACTIVE)
                    .build();

            AnalyzePredictionCommand command = new AnalyzePredictionCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(inactivePrediction));

            assertThatThrownBy(() -> service.analyzePrediction(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when analyzing draft segment")
        void shouldThrowWhenAnalyzingDraftPrediction() {
            ChurnPrediction draftPrediction = ChurnPrediction.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Prediction")
                    .criteria(testCriteria)
                    .status(PredictionStatus.DRAFT)
                    .build();

            AnalyzePredictionCommand command = new AnalyzePredictionCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(draftPrediction));

            assertThatThrownBy(() -> service.analyzePrediction(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalyzingNonExistent() {
            AnalyzePredictionCommand command = new AnalyzePredictionCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.analyzePrediction(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
