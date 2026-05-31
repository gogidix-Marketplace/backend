package com.gogidix.aiservices.aicustomersegmentationservice.application.service;

import com.gogidix.aiservices.aicustomersegmentationservice.application.command.AddCustomersToSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.AnalyzeSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.CreateSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.RemoveCustomersFromSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.UpdateSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.CustomerSegmentResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.SegmentAnalysisResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.mapper.CustomerSegmentMapper;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.CustomerSegment;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentStatus;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentType;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.port.out.CustomerSegmentRepositoryPort;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.policy.SegmentBusinessPolicy;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.ValidationException;
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
@DisplayName("CustomerSegmentApplicationService Tests")
class CustomerSegmentApplicationServiceTest {

    @Mock
    private CustomerSegmentRepositoryPort repository;

    @Mock
    private CustomerSegmentMapper mapper;

    @Mock
    private SegmentBusinessPolicy businessPolicy;

    @Mock
    private SegmentAnalysisService analysisService;

    @InjectMocks
    private CustomerSegmentApplicationService service;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String SEGMENT_ID = "segment-789";

    private CustomerSegment testSegment;
    private CustomerSegmentResponseDto testResponseDto;
    private SegmentCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = SegmentCriteria.builder()
                .type(SegmentCriteria.CriteriaType.CUSTOM)
                .operator(SegmentCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testSegment = CustomerSegment.builder()
                .segmentId(SEGMENT_ID)
                .tenantId(TENANT_ID)
                .name("Premium Customers")
                .description("High value customers")
                .criteria(testCriteria)
                .status(SegmentStatus.ACTIVE)
                .segmentType(SegmentType.BEHAVIORAL)
                .customerCount(100L)
                .build();

        testResponseDto = new CustomerSegmentResponseDto(
                SEGMENT_ID,
                "Premium Customers",
                "High value customers",
                SegmentType.BEHAVIORAL,
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
    @DisplayName("createSegment() Tests")
    class CreateSegmentTests {

        @Test
        @DisplayName("Should create segment successfully")
        void shouldCreateSegmentSuccessfully() {
            CreateSegmentCommand command = new CreateSegmentCommand(
                    "Premium Customers",
                    "High value customers",
                    SegmentType.BEHAVIORAL,
                    Map.of("field", "lifetimeValue", "operator", ">", "value", 1000),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(CustomerSegment.class))).thenReturn(testSegment);
            when(mapper.toResponseDto(any(CustomerSegment.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateSegmentCreation(anyString());

            CustomerSegmentResponseDto result = service.createSegment(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);

            ArgumentCaptor<CustomerSegment> captor = ArgumentCaptor.forClass(CustomerSegment.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Customers");
            verify(businessPolicy).validateSegmentCreation(TENANT_ID);
        }

        @Test
        @DisplayName("Should throw exception when criteria map is null")
        void shouldThrowWhenCriteriaNull() {
            CreateSegmentCommand command = new CreateSegmentCommand(
                    "Premium Customers",
                    "High value customers",
                    SegmentType.BEHAVIORAL,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createSegment(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when criteria map is empty")
        void shouldThrowWhenCriteriaEmpty() {
            CreateSegmentCommand command = new CreateSegmentCommand(
                    "Premium Customers",
                    "High value customers",
                    SegmentType.BEHAVIORAL,
                    Map.of(),
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createSegment(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should use default values when criteria map missing fields")
        void shouldUseDefaultsWhenCriteriaMissingFields() {
            CreateSegmentCommand command = new CreateSegmentCommand(
                    "Premium Customers",
                    "High value customers",
                    SegmentType.BEHAVIORAL,
                    Map.of("value", 500),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(CustomerSegment.class))).thenReturn(testSegment);
            when(mapper.toResponseDto(any(CustomerSegment.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateSegmentCreation(anyString());

            CustomerSegmentResponseDto result = service.createSegment(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(CustomerSegment.class));
        }
    }

    @Nested
    @DisplayName("updateSegment() Tests")
    class UpdateSegmentTests {

        @Test
        @DisplayName("Should update segment name and description")
        void shouldUpdateSegmentNameAndDescription() {
            UpdateSegmentCommand command = new UpdateSegmentCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testSegment));
            when(repository.save(any(CustomerSegment.class))).thenReturn(testSegment);
            when(mapper.toResponseDto(any(CustomerSegment.class))).thenReturn(testResponseDto);

            CustomerSegmentResponseDto result = service.updateSegment(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(CustomerSegment.class));
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenSegmentNotFound() {
            UpdateSegmentCommand command = new UpdateSegmentCommand(
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

            assertThatThrownBy(() -> service.updateSegment(command))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Segment not found");
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            CustomerSegment otherTenantSegment = CustomerSegment.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Segment")
                    .criteria(testCriteria)
                    .build();

            UpdateSegmentCommand command = new UpdateSegmentCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantSegment));

            assertThatThrownBy(() -> service.updateSegment(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should not update when name is null")
        void shouldNotUpdateWhenNameNull() {
            UpdateSegmentCommand command = new UpdateSegmentCommand(
                    SEGMENT_ID,
                    null,
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testSegment));
            when(repository.save(any(CustomerSegment.class))).thenReturn(testSegment);
            when(mapper.toResponseDto(any(CustomerSegment.class))).thenReturn(testResponseDto);

            service.updateSegment(command);

            ArgumentCaptor<CustomerSegment> captor = ArgumentCaptor.forClass(CustomerSegment.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Customers");
        }
    }

    @Nested
    @DisplayName("deleteSegment() Tests")
    class DeleteSegmentTests {

        @Test
        @DisplayName("Should delete segment successfully")
        void shouldDeleteSegmentSuccessfully() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testSegment));
            doNothing().when(businessPolicy).validateSegmentDeletion(any(CustomerSegment.class));
            doNothing().when(repository).deleteById(SEGMENT_ID);

            service.deleteSegment(SEGMENT_ID, TENANT_ID, USER_ID);

            verify(businessPolicy).validateSegmentDeletion(testSegment);
            verify(repository).deleteById(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent segment")
        void shouldThrowWhenDeletingNonExistent() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteSegment("nonexistent-id", TENANT_ID, USER_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before deletion")
        void shouldValidatePolicyBeforeDeletion() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testSegment));
            doThrow(new ValidationException("Cannot delete active segment with customers"))
                    .when(businessPolicy).validateSegmentDeletion(any(CustomerSegment.class));

            assertThatThrownBy(() -> service.deleteSegment(SEGMENT_ID, TENANT_ID, USER_ID))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot delete active segment");

            verify(repository, never()).deleteById(anyString());
        }
    }

    @Nested
    @DisplayName("getSegmentById() Tests")
    class GetSegmentByIdTests {

        @Test
        @DisplayName("Should return segment when found")
        void shouldReturnSegmentWhenFound() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testSegment));
            when(mapper.toResponseDto(any(CustomerSegment.class))).thenReturn(testResponseDto);

            CustomerSegmentResponseDto result = service.getSegmentById(SEGMENT_ID, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenSegmentNotFound() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getSegmentById("nonexistent-id", TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            CustomerSegment otherTenantSegment = CustomerSegment.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Segment")
                    .criteria(testCriteria)
                    .build();

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantSegment));

            assertThatThrownBy(() -> service.getSegmentById(SEGMENT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getSegmentsByTenant() Tests")
    class GetSegmentsByTenantTests {

        @Test
        @DisplayName("Should return empty paged response")
        void shouldReturnEmptyPagedResponse() {
            PagedResponseDto<CustomerSegmentResponseDto> result = service.getSegmentsByTenant(
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
            PagedResponseDto<CustomerSegmentResponseDto> result = service.getSegmentsByTenant(
                    TENANT_ID,
                    SegmentType.BEHAVIORAL,
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
    @DisplayName("addCustomersToSegment() Tests")
    class AddCustomersTests {

        @Test
        @DisplayName("Should add customers successfully")
        void shouldAddCustomersSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2", "cust-3");
            AddCustomersToSegmentCommand command = new AddCustomersToSegmentCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testSegment));
            when(repository.save(any(CustomerSegment.class))).thenReturn(testSegment);
            when(mapper.toResponseDto(any(CustomerSegment.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateCustomerAddition(any(), anyInt());

            CustomerSegmentResponseDto result = service.addCustomersToSegment(command);

            assertThat(result).isNotNull();
            verify(businessPolicy).validateCustomerAddition(testSegment, 3);
            verify(repository).save(testSegment);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenSegmentNotFound() {
            List<String> customerIds = List.of("cust-1");
            AddCustomersToSegmentCommand command = new AddCustomersToSegmentCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.addCustomersToSegment(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before adding customers")
        void shouldValidatePolicyBeforeAdding() {
            List<String> customerIds = List.of("cust-1");
            AddCustomersToSegmentCommand command = new AddCustomersToSegmentCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testSegment));
            doThrow(new ValidationException("Segment is full"))
                    .when(businessPolicy).validateCustomerAddition(any(), anyInt());

            assertThatThrownBy(() -> service.addCustomersToSegment(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Segment is full");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeCustomersFromSegment() Tests")
    class RemoveCustomersTests {

        @Test
        @DisplayName("Should remove customers successfully")
        void shouldRemoveCustomersSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2");
            RemoveCustomersFromSegmentCommand command = new RemoveCustomersFromSegmentCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testSegment));
            when(repository.save(any(CustomerSegment.class))).thenReturn(testSegment);
            when(mapper.toResponseDto(any(CustomerSegment.class))).thenReturn(testResponseDto);

            CustomerSegmentResponseDto result = service.removeCustomersFromSegment(command);

            assertThat(result).isNotNull();
            verify(repository).save(testSegment);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenSegmentNotFound() {
            List<String> customerIds = List.of("cust-1");
            RemoveCustomersFromSegmentCommand command = new RemoveCustomersFromSegmentCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeCustomersFromSegment(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("analyzeSegment() Tests")
    class AnalyzeSegmentTests {

        @Test
        @DisplayName("Should analyze active segment successfully")
        void shouldAnalyzeActiveSegmentSuccessfully() {
            Map<String, Object> analysisOptions = new HashMap<>();
            AnalyzeSegmentCommand command = new AnalyzeSegmentCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    analysisOptions
            );

            SegmentAnalysisResponseDto analysisResponse = new SegmentAnalysisResponseDto(
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

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testSegment));
            when(analysisService.analyzeSegment(eq(testSegment), eq(analysisOptions)))
                    .thenReturn(analysisResponse);

            SegmentAnalysisResponseDto result = service.analyzeSegment(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
            assertThat(result.confidenceScore()).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should throw exception when analyzing inactive segment")
        void shouldThrowWhenAnalyzingInactiveSegment() {
            CustomerSegment inactiveSegment = CustomerSegment.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Segment")
                    .criteria(testCriteria)
                    .status(SegmentStatus.INACTIVE)
                    .build();

            AnalyzeSegmentCommand command = new AnalyzeSegmentCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(inactiveSegment));

            assertThatThrownBy(() -> service.analyzeSegment(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when analyzing draft segment")
        void shouldThrowWhenAnalyzingDraftSegment() {
            CustomerSegment draftSegment = CustomerSegment.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Segment")
                    .criteria(testCriteria)
                    .status(SegmentStatus.DRAFT)
                    .build();

            AnalyzeSegmentCommand command = new AnalyzeSegmentCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(draftSegment));

            assertThatThrownBy(() -> service.analyzeSegment(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalyzingNonExistent() {
            AnalyzeSegmentCommand command = new AnalyzeSegmentCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.analyzeSegment(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
