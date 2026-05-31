package com.gogidix.aiservices.aiproductrecommendationservice.application.service;

import com.gogidix.aiservices.aiproductrecommendationservice.application.command.AddProductsToRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.AnalyzeRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.CreateRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.RemoveProductsFromRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.UpdateRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.ProductRecommendationResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.RecommendationAnalysisResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.mapper.ProductRecommendationMapper;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductRecommendation;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationStatus;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.port.out.ProductRecommendationRepositoryPort;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.policy.RecommendationBusinessPolicy;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.ValidationException;
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
@DisplayName("ProductRecommendationApplicationService Tests")
class ProductRecommendationApplicationServiceTest {

    @Mock
    private ProductRecommendationRepositoryPort repository;

    @Mock
    private ProductRecommendationMapper mapper;

    @Mock
    private RecommendationBusinessPolicy businessPolicy;

    @Mock
    private RecommendationAnalysisService analysisService;

    @InjectMocks
    private ProductRecommendationApplicationService service;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String SEGMENT_ID = "segment-789";

    private ProductRecommendation testRecommendation;
    private ProductRecommendationResponseDto testResponseDto;
    private RecommendationCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = RecommendationCriteria.builder()
                .type(RecommendationCriteria.CriteriaType.CUSTOM)
                .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testRecommendation = ProductRecommendation.builder()
                .segmentId(SEGMENT_ID)
                .tenantId(TENANT_ID)
                .name("Premium Products")
                .description("High value customers")
                .criteria(testCriteria)
                .status(RecommendationStatus.ACTIVE)
                .segmentType(RecommendationType.BEHAVIORAL)
                .customerCount(100L)
                .build();

        testResponseDto = new ProductRecommendationResponseDto(
                SEGMENT_ID,
                "Premium Products",
                "High value customers",
                RecommendationType.BEHAVIORAL,
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
    @DisplayName("createRecommendation() Tests")
    class CreateRecommendationTests {

        @Test
        @DisplayName("Should create segment successfully")
        void shouldCreateRecommendationSuccessfully() {
            CreateRecommendationCommand command = new CreateRecommendationCommand(
                    "Premium Products",
                    "High value customers",
                    RecommendationType.BEHAVIORAL,
                    Map.of("field", "lifetimeValue", "operator", ">", "value", 1000),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(ProductRecommendation.class))).thenReturn(testRecommendation);
            when(mapper.toResponseDto(any(ProductRecommendation.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateRecommendationCreation(anyString());

            ProductRecommendationResponseDto result = service.createRecommendation(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);

            ArgumentCaptor<ProductRecommendation> captor = ArgumentCaptor.forClass(ProductRecommendation.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Products");
            verify(businessPolicy).validateRecommendationCreation(TENANT_ID);
        }

        @Test
        @DisplayName("Should throw exception when criteria map is null")
        void shouldThrowWhenCriteriaNull() {
            CreateRecommendationCommand command = new CreateRecommendationCommand(
                    "Premium Products",
                    "High value customers",
                    RecommendationType.BEHAVIORAL,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createRecommendation(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when criteria map is empty")
        void shouldThrowWhenCriteriaEmpty() {
            CreateRecommendationCommand command = new CreateRecommendationCommand(
                    "Premium Products",
                    "High value customers",
                    RecommendationType.BEHAVIORAL,
                    Map.of(),
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createRecommendation(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should use default values when criteria map missing fields")
        void shouldUseDefaultsWhenCriteriaMissingFields() {
            CreateRecommendationCommand command = new CreateRecommendationCommand(
                    "Premium Products",
                    "High value customers",
                    RecommendationType.BEHAVIORAL,
                    Map.of("value", 500),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(ProductRecommendation.class))).thenReturn(testRecommendation);
            when(mapper.toResponseDto(any(ProductRecommendation.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateRecommendationCreation(anyString());

            ProductRecommendationResponseDto result = service.createRecommendation(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(ProductRecommendation.class));
        }
    }

    @Nested
    @DisplayName("updateRecommendation() Tests")
    class UpdateRecommendationTests {

        @Test
        @DisplayName("Should update segment name and description")
        void shouldUpdateRecommendationNameAndDescription() {
            UpdateRecommendationCommand command = new UpdateRecommendationCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testRecommendation));
            when(repository.save(any(ProductRecommendation.class))).thenReturn(testRecommendation);
            when(mapper.toResponseDto(any(ProductRecommendation.class))).thenReturn(testResponseDto);

            ProductRecommendationResponseDto result = service.updateRecommendation(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(ProductRecommendation.class));
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenRecommendationNotFound() {
            UpdateRecommendationCommand command = new UpdateRecommendationCommand(
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

            assertThatThrownBy(() -> service.updateRecommendation(command))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Recommendation not found");
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            ProductRecommendation otherTenantRecommendation = ProductRecommendation.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Recommendation")
                    .criteria(testCriteria)
                    .build();

            UpdateRecommendationCommand command = new UpdateRecommendationCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantRecommendation));

            assertThatThrownBy(() -> service.updateRecommendation(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should not update when name is null")
        void shouldNotUpdateWhenNameNull() {
            UpdateRecommendationCommand command = new UpdateRecommendationCommand(
                    SEGMENT_ID,
                    null,
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testRecommendation));
            when(repository.save(any(ProductRecommendation.class))).thenReturn(testRecommendation);
            when(mapper.toResponseDto(any(ProductRecommendation.class))).thenReturn(testResponseDto);

            service.updateRecommendation(command);

            ArgumentCaptor<ProductRecommendation> captor = ArgumentCaptor.forClass(ProductRecommendation.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Products");
        }
    }

    @Nested
    @DisplayName("deleteRecommendation() Tests")
    class DeleteRecommendationTests {

        @Test
        @DisplayName("Should delete segment successfully")
        void shouldDeleteRecommendationSuccessfully() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testRecommendation));
            doNothing().when(businessPolicy).validateRecommendationDeletion(any(ProductRecommendation.class));
            doNothing().when(repository).deleteById(SEGMENT_ID);

            service.deleteRecommendation(SEGMENT_ID, TENANT_ID, USER_ID);

            verify(businessPolicy).validateRecommendationDeletion(testRecommendation);
            verify(repository).deleteById(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent segment")
        void shouldThrowWhenDeletingNonExistent() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteRecommendation("nonexistent-id", TENANT_ID, USER_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before deletion")
        void shouldValidatePolicyBeforeDeletion() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testRecommendation));
            doThrow(new ValidationException("Cannot delete active segment with customers"))
                    .when(businessPolicy).validateRecommendationDeletion(any(ProductRecommendation.class));

            assertThatThrownBy(() -> service.deleteRecommendation(SEGMENT_ID, TENANT_ID, USER_ID))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot delete active segment");

            verify(repository, never()).deleteById(anyString());
        }
    }

    @Nested
    @DisplayName("getRecommendationById() Tests")
    class GetRecommendationByIdTests {

        @Test
        @DisplayName("Should return segment when found")
        void shouldReturnRecommendationWhenFound() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testRecommendation));
            when(mapper.toResponseDto(any(ProductRecommendation.class))).thenReturn(testResponseDto);

            ProductRecommendationResponseDto result = service.getRecommendationById(SEGMENT_ID, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenRecommendationNotFound() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getRecommendationById("nonexistent-id", TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            ProductRecommendation otherTenantRecommendation = ProductRecommendation.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Recommendation")
                    .criteria(testCriteria)
                    .build();

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantRecommendation));

            assertThatThrownBy(() -> service.getRecommendationById(SEGMENT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getRecommendationsByTenant() Tests")
    class GetRecommendationsByTenantTests {

        @Test
        @DisplayName("Should return empty paged response")
        void shouldReturnEmptyPagedResponse() {
            PagedResponseDto<ProductRecommendationResponseDto> result = service.getRecommendationsByTenant(
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
            PagedResponseDto<ProductRecommendationResponseDto> result = service.getRecommendationsByTenant(
                    TENANT_ID,
                    RecommendationType.BEHAVIORAL,
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
    @DisplayName("addProductsToRecommendation() Tests")
    class AddProductsTests {

        @Test
        @DisplayName("Should add customers successfully")
        void shouldAddProductsSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2", "cust-3");
            AddProductsToRecommendationCommand command = new AddProductsToRecommendationCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testRecommendation));
            when(repository.save(any(ProductRecommendation.class))).thenReturn(testRecommendation);
            when(mapper.toResponseDto(any(ProductRecommendation.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateProductAddition(any(), anyInt());

            ProductRecommendationResponseDto result = service.addProductsToRecommendation(command);

            assertThat(result).isNotNull();
            verify(businessPolicy).validateProductAddition(testRecommendation, 3);
            verify(repository).save(testRecommendation);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenRecommendationNotFound() {
            List<String> customerIds = List.of("cust-1");
            AddProductsToRecommendationCommand command = new AddProductsToRecommendationCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.addProductsToRecommendation(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before adding customers")
        void shouldValidatePolicyBeforeAdding() {
            List<String> customerIds = List.of("cust-1");
            AddProductsToRecommendationCommand command = new AddProductsToRecommendationCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testRecommendation));
            doThrow(new ValidationException("Recommendation is full"))
                    .when(businessPolicy).validateProductAddition(any(), anyInt());

            assertThatThrownBy(() -> service.addProductsToRecommendation(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Recommendation is full");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeProductsFromRecommendation() Tests")
    class RemoveProductsTests {

        @Test
        @DisplayName("Should remove customers successfully")
        void shouldRemoveProductsSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2");
            RemoveProductsFromRecommendationCommand command = new RemoveProductsFromRecommendationCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testRecommendation));
            when(repository.save(any(ProductRecommendation.class))).thenReturn(testRecommendation);
            when(mapper.toResponseDto(any(ProductRecommendation.class))).thenReturn(testResponseDto);

            ProductRecommendationResponseDto result = service.removeProductsFromRecommendation(command);

            assertThat(result).isNotNull();
            verify(repository).save(testRecommendation);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenRecommendationNotFound() {
            List<String> customerIds = List.of("cust-1");
            RemoveProductsFromRecommendationCommand command = new RemoveProductsFromRecommendationCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeProductsFromRecommendation(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("analyzeRecommendation() Tests")
    class AnalyzeRecommendationTests {

        @Test
        @DisplayName("Should analyze active segment successfully")
        void shouldAnalyzeActiveRecommendationSuccessfully() {
            Map<String, Object> analysisOptions = new HashMap<>();
            AnalyzeRecommendationCommand command = new AnalyzeRecommendationCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    analysisOptions
            );

            RecommendationAnalysisResponseDto analysisResponse = new RecommendationAnalysisResponseDto(
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

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testRecommendation));
            when(analysisService.analyzeRecommendation(eq(testRecommendation), eq(analysisOptions)))
                    .thenReturn(analysisResponse);

            RecommendationAnalysisResponseDto result = service.analyzeRecommendation(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
            assertThat(result.confidenceScore()).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should throw exception when analyzing inactive segment")
        void shouldThrowWhenAnalyzingInactiveRecommendation() {
            ProductRecommendation inactiveRecommendation = ProductRecommendation.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Recommendation")
                    .criteria(testCriteria)
                    .status(RecommendationStatus.INACTIVE)
                    .build();

            AnalyzeRecommendationCommand command = new AnalyzeRecommendationCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(inactiveRecommendation));

            assertThatThrownBy(() -> service.analyzeRecommendation(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when analyzing draft segment")
        void shouldThrowWhenAnalyzingDraftRecommendation() {
            ProductRecommendation draftRecommendation = ProductRecommendation.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Recommendation")
                    .criteria(testCriteria)
                    .status(RecommendationStatus.DRAFT)
                    .build();

            AnalyzeRecommendationCommand command = new AnalyzeRecommendationCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(draftRecommendation));

            assertThatThrownBy(() -> service.analyzeRecommendation(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalyzingNonExistent() {
            AnalyzeRecommendationCommand command = new AnalyzeRecommendationCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.analyzeRecommendation(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
