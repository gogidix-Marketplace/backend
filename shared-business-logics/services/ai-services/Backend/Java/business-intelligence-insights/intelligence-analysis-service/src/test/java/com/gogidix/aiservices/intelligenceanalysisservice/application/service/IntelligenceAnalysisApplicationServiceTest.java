package com.gogidix.aiservices.intelligenceanalysisservice.application.service;

import com.gogidix.aiservices.intelligenceanalysisservice.application.command.AddIntelligenceReportsToAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.AnalyzeAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.CreateAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.RemoveIntelligenceReportsFromAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.UpdateAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.IntelligenceAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.AnalysisAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.mapper.IntelligenceAnalysisMapper;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.IntelligenceAnalysis;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisStatus;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.port.out.IntelligenceAnalysisRepositoryPort;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.policy.AnalysisBusinessPolicy;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.exception.ValidationException;
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
@DisplayName("IntelligenceAnalysisApplicationService Tests")
class IntelligenceAnalysisApplicationServiceTest {

    @Mock
    private IntelligenceAnalysisRepositoryPort repository;

    @Mock
    private IntelligenceAnalysisMapper mapper;

    @Mock
    private AnalysisBusinessPolicy businessPolicy;

    @Mock
    private AnalysisAnalysisService analysisService;

    @InjectMocks
    private IntelligenceAnalysisApplicationService service;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String SEGMENT_ID = "segment-789";

    private IntelligenceAnalysis testAnalysis;
    private IntelligenceAnalysisResponseDto testResponseDto;
    private AnalysisCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = AnalysisCriteria.builder()
                .type(AnalysisCriteria.CriteriaType.CUSTOM)
                .operator(AnalysisCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testAnalysis = IntelligenceAnalysis.builder()
                .segmentId(SEGMENT_ID)
                .tenantId(TENANT_ID)
                .name("Premium IntelligenceReports")
                .description("High value customers")
                .criteria(testCriteria)
                .status(AnalysisStatus.ACTIVE)
                .segmentType(AnalysisType.BEHAVIORAL)
                .customerCount(100L)
                .build();

        testResponseDto = new IntelligenceAnalysisResponseDto(
                SEGMENT_ID,
                "Premium IntelligenceReports",
                "High value customers",
                AnalysisType.BEHAVIORAL,
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
    @DisplayName("createAnalysis() Tests")
    class CreateAnalysisTests {

        @Test
        @DisplayName("Should create segment successfully")
        void shouldCreateAnalysisSuccessfully() {
            CreateAnalysisCommand command = new CreateAnalysisCommand(
                    "Premium IntelligenceReports",
                    "High value customers",
                    AnalysisType.BEHAVIORAL,
                    Map.of("field", "lifetimeValue", "operator", ">", "value", 1000),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(IntelligenceAnalysis.class))).thenReturn(testAnalysis);
            when(mapper.toResponseDto(any(IntelligenceAnalysis.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateAnalysisCreation(anyString());

            IntelligenceAnalysisResponseDto result = service.createAnalysis(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);

            ArgumentCaptor<IntelligenceAnalysis> captor = ArgumentCaptor.forClass(IntelligenceAnalysis.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium IntelligenceReports");
            verify(businessPolicy).validateAnalysisCreation(TENANT_ID);
        }

        @Test
        @DisplayName("Should throw exception when criteria map is null")
        void shouldThrowWhenCriteriaNull() {
            CreateAnalysisCommand command = new CreateAnalysisCommand(
                    "Premium IntelligenceReports",
                    "High value customers",
                    AnalysisType.BEHAVIORAL,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createAnalysis(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when criteria map is empty")
        void shouldThrowWhenCriteriaEmpty() {
            CreateAnalysisCommand command = new CreateAnalysisCommand(
                    "Premium IntelligenceReports",
                    "High value customers",
                    AnalysisType.BEHAVIORAL,
                    Map.of(),
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createAnalysis(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should use default values when criteria map missing fields")
        void shouldUseDefaultsWhenCriteriaMissingFields() {
            CreateAnalysisCommand command = new CreateAnalysisCommand(
                    "Premium IntelligenceReports",
                    "High value customers",
                    AnalysisType.BEHAVIORAL,
                    Map.of("value", 500),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(IntelligenceAnalysis.class))).thenReturn(testAnalysis);
            when(mapper.toResponseDto(any(IntelligenceAnalysis.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateAnalysisCreation(anyString());

            IntelligenceAnalysisResponseDto result = service.createAnalysis(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(IntelligenceAnalysis.class));
        }
    }

    @Nested
    @DisplayName("updateAnalysis() Tests")
    class UpdateAnalysisTests {

        @Test
        @DisplayName("Should update segment name and description")
        void shouldUpdateAnalysisNameAndDescription() {
            UpdateAnalysisCommand command = new UpdateAnalysisCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testAnalysis));
            when(repository.save(any(IntelligenceAnalysis.class))).thenReturn(testAnalysis);
            when(mapper.toResponseDto(any(IntelligenceAnalysis.class))).thenReturn(testResponseDto);

            IntelligenceAnalysisResponseDto result = service.updateAnalysis(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(IntelligenceAnalysis.class));
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalysisNotFound() {
            UpdateAnalysisCommand command = new UpdateAnalysisCommand(
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

            assertThatThrownBy(() -> service.updateAnalysis(command))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Analysis not found");
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            IntelligenceAnalysis otherTenantAnalysis = IntelligenceAnalysis.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Analysis")
                    .criteria(testCriteria)
                    .build();

            UpdateAnalysisCommand command = new UpdateAnalysisCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantAnalysis));

            assertThatThrownBy(() -> service.updateAnalysis(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should not update when name is null")
        void shouldNotUpdateWhenNameNull() {
            UpdateAnalysisCommand command = new UpdateAnalysisCommand(
                    SEGMENT_ID,
                    null,
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testAnalysis));
            when(repository.save(any(IntelligenceAnalysis.class))).thenReturn(testAnalysis);
            when(mapper.toResponseDto(any(IntelligenceAnalysis.class))).thenReturn(testResponseDto);

            service.updateAnalysis(command);

            ArgumentCaptor<IntelligenceAnalysis> captor = ArgumentCaptor.forClass(IntelligenceAnalysis.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium IntelligenceReports");
        }
    }

    @Nested
    @DisplayName("deleteAnalysis() Tests")
    class DeleteAnalysisTests {

        @Test
        @DisplayName("Should delete segment successfully")
        void shouldDeleteAnalysisSuccessfully() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testAnalysis));
            doNothing().when(businessPolicy).validateAnalysisDeletion(any(IntelligenceAnalysis.class));
            doNothing().when(repository).deleteById(SEGMENT_ID);

            service.deleteAnalysis(SEGMENT_ID, TENANT_ID, USER_ID);

            verify(businessPolicy).validateAnalysisDeletion(testAnalysis);
            verify(repository).deleteById(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent segment")
        void shouldThrowWhenDeletingNonExistent() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteAnalysis("nonexistent-id", TENANT_ID, USER_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before deletion")
        void shouldValidatePolicyBeforeDeletion() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testAnalysis));
            doThrow(new ValidationException("Cannot delete active segment with customers"))
                    .when(businessPolicy).validateAnalysisDeletion(any(IntelligenceAnalysis.class));

            assertThatThrownBy(() -> service.deleteAnalysis(SEGMENT_ID, TENANT_ID, USER_ID))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot delete active segment");

            verify(repository, never()).deleteById(anyString());
        }
    }

    @Nested
    @DisplayName("getAnalysisById() Tests")
    class GetAnalysisByIdTests {

        @Test
        @DisplayName("Should return segment when found")
        void shouldReturnAnalysisWhenFound() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testAnalysis));
            when(mapper.toResponseDto(any(IntelligenceAnalysis.class))).thenReturn(testResponseDto);

            IntelligenceAnalysisResponseDto result = service.getAnalysisById(SEGMENT_ID, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalysisNotFound() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getAnalysisById("nonexistent-id", TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            IntelligenceAnalysis otherTenantAnalysis = IntelligenceAnalysis.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Analysis")
                    .criteria(testCriteria)
                    .build();

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantAnalysis));

            assertThatThrownBy(() -> service.getAnalysisById(SEGMENT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getAnalysissByTenant() Tests")
    class GetAnalysissByTenantTests {

        @Test
        @DisplayName("Should return empty paged response")
        void shouldReturnEmptyPagedResponse() {
            PagedResponseDto<IntelligenceAnalysisResponseDto> result = service.getAnalysissByTenant(
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
            PagedResponseDto<IntelligenceAnalysisResponseDto> result = service.getAnalysissByTenant(
                    TENANT_ID,
                    AnalysisType.BEHAVIORAL,
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
    @DisplayName("addIntelligenceReportsToAnalysis() Tests")
    class AddIntelligenceReportsTests {

        @Test
        @DisplayName("Should add customers successfully")
        void shouldAddIntelligenceReportsSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2", "cust-3");
            AddIntelligenceReportsToAnalysisCommand command = new AddIntelligenceReportsToAnalysisCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testAnalysis));
            when(repository.save(any(IntelligenceAnalysis.class))).thenReturn(testAnalysis);
            when(mapper.toResponseDto(any(IntelligenceAnalysis.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateIntelligenceReportAddition(any(), anyInt());

            IntelligenceAnalysisResponseDto result = service.addIntelligenceReportsToAnalysis(command);

            assertThat(result).isNotNull();
            verify(businessPolicy).validateIntelligenceReportAddition(testAnalysis, 3);
            verify(repository).save(testAnalysis);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalysisNotFound() {
            List<String> customerIds = List.of("cust-1");
            AddIntelligenceReportsToAnalysisCommand command = new AddIntelligenceReportsToAnalysisCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.addIntelligenceReportsToAnalysis(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before adding customers")
        void shouldValidatePolicyBeforeAdding() {
            List<String> customerIds = List.of("cust-1");
            AddIntelligenceReportsToAnalysisCommand command = new AddIntelligenceReportsToAnalysisCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testAnalysis));
            doThrow(new ValidationException("Analysis is full"))
                    .when(businessPolicy).validateIntelligenceReportAddition(any(), anyInt());

            assertThatThrownBy(() -> service.addIntelligenceReportsToAnalysis(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Analysis is full");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeIntelligenceReportsFromAnalysis() Tests")
    class RemoveIntelligenceReportsTests {

        @Test
        @DisplayName("Should remove customers successfully")
        void shouldRemoveIntelligenceReportsSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2");
            RemoveIntelligenceReportsFromAnalysisCommand command = new RemoveIntelligenceReportsFromAnalysisCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testAnalysis));
            when(repository.save(any(IntelligenceAnalysis.class))).thenReturn(testAnalysis);
            when(mapper.toResponseDto(any(IntelligenceAnalysis.class))).thenReturn(testResponseDto);

            IntelligenceAnalysisResponseDto result = service.removeIntelligenceReportsFromAnalysis(command);

            assertThat(result).isNotNull();
            verify(repository).save(testAnalysis);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalysisNotFound() {
            List<String> customerIds = List.of("cust-1");
            RemoveIntelligenceReportsFromAnalysisCommand command = new RemoveIntelligenceReportsFromAnalysisCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeIntelligenceReportsFromAnalysis(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("analyzeAnalysis() Tests")
    class AnalyzeAnalysisTests {

        @Test
        @DisplayName("Should analyze active segment successfully")
        void shouldAnalyzeActiveAnalysisSuccessfully() {
            Map<String, Object> analysisOptions = new HashMap<>();
            AnalyzeAnalysisCommand command = new AnalyzeAnalysisCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    analysisOptions
            );

            AnalysisAnalysisResponseDto analysisResponse = new AnalysisAnalysisResponseDto(
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

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testAnalysis));
            when(analysisService.analyzeAnalysis(eq(testAnalysis), eq(analysisOptions)))
                    .thenReturn(analysisResponse);

            AnalysisAnalysisResponseDto result = service.analyzeAnalysis(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
            assertThat(result.confidenceScore()).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should throw exception when analyzing inactive segment")
        void shouldThrowWhenAnalyzingInactiveAnalysis() {
            IntelligenceAnalysis inactiveAnalysis = IntelligenceAnalysis.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Analysis")
                    .criteria(testCriteria)
                    .status(AnalysisStatus.INACTIVE)
                    .build();

            AnalyzeAnalysisCommand command = new AnalyzeAnalysisCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(inactiveAnalysis));

            assertThatThrownBy(() -> service.analyzeAnalysis(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when analyzing draft segment")
        void shouldThrowWhenAnalyzingDraftAnalysis() {
            IntelligenceAnalysis draftAnalysis = IntelligenceAnalysis.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Analysis")
                    .criteria(testCriteria)
                    .status(AnalysisStatus.DRAFT)
                    .build();

            AnalyzeAnalysisCommand command = new AnalyzeAnalysisCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(draftAnalysis));

            assertThatThrownBy(() -> service.analyzeAnalysis(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalyzingNonExistent() {
            AnalyzeAnalysisCommand command = new AnalyzeAnalysisCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.analyzeAnalysis(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
