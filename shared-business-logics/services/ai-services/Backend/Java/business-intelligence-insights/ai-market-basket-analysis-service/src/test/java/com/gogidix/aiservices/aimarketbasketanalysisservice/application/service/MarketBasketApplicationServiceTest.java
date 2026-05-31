package com.gogidix.aiservices.aimarketbasketanalysisservice.application.service;

import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.AddCustomersToBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.AnalyzeBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.CreateBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.RemoveCustomersFromBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.UpdateBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.MarketBasketResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.BasketAnalysisResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.mapper.MarketBasketMapper;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.MarketBasket;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketStatus;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.port.out.MarketBasketRepositoryPort;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.policy.BasketBusinessPolicy;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.ValidationException;
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
@DisplayName("MarketBasketApplicationService Tests")
class MarketBasketApplicationServiceTest {

    @Mock
    private MarketBasketRepositoryPort repository;

    @Mock
    private MarketBasketMapper mapper;

    @Mock
    private BasketBusinessPolicy businessPolicy;

    @Mock
    private BasketAnalysisService analysisService;

    @InjectMocks
    private MarketBasketApplicationService service;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String SEGMENT_ID = "segment-789";

    private MarketBasket testBasket;
    private MarketBasketResponseDto testResponseDto;
    private BasketCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = BasketCriteria.builder()
                .type(BasketCriteria.CriteriaType.CUSTOM)
                .operator(BasketCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testBasket = MarketBasket.builder()
                .segmentId(SEGMENT_ID)
                .tenantId(TENANT_ID)
                .name("Premium Customers")
                .description("High value customers")
                .criteria(testCriteria)
                .status(BasketStatus.ACTIVE)
                .segmentType(BasketType.BEHAVIORAL)
                .customerCount(100L)
                .build();

        testResponseDto = new MarketBasketResponseDto(
                SEGMENT_ID,
                "Premium Customers",
                "High value customers",
                BasketType.BEHAVIORAL,
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
    @DisplayName("createBasket() Tests")
    class CreateBasketTests {

        @Test
        @DisplayName("Should create segment successfully")
        void shouldCreateBasketSuccessfully() {
            CreateBasketCommand command = new CreateBasketCommand(
                    "Premium Customers",
                    "High value customers",
                    BasketType.BEHAVIORAL,
                    Map.of("field", "lifetimeValue", "operator", ">", "value", 1000),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(MarketBasket.class))).thenReturn(testBasket);
            when(mapper.toResponseDto(any(MarketBasket.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateBasketCreation(anyString());

            MarketBasketResponseDto result = service.createBasket(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);

            ArgumentCaptor<MarketBasket> captor = ArgumentCaptor.forClass(MarketBasket.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Customers");
            verify(businessPolicy).validateBasketCreation(TENANT_ID);
        }

        @Test
        @DisplayName("Should throw exception when criteria map is null")
        void shouldThrowWhenCriteriaNull() {
            CreateBasketCommand command = new CreateBasketCommand(
                    "Premium Customers",
                    "High value customers",
                    BasketType.BEHAVIORAL,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createBasket(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when criteria map is empty")
        void shouldThrowWhenCriteriaEmpty() {
            CreateBasketCommand command = new CreateBasketCommand(
                    "Premium Customers",
                    "High value customers",
                    BasketType.BEHAVIORAL,
                    Map.of(),
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createBasket(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should use default values when criteria map missing fields")
        void shouldUseDefaultsWhenCriteriaMissingFields() {
            CreateBasketCommand command = new CreateBasketCommand(
                    "Premium Customers",
                    "High value customers",
                    BasketType.BEHAVIORAL,
                    Map.of("value", 500),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(MarketBasket.class))).thenReturn(testBasket);
            when(mapper.toResponseDto(any(MarketBasket.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateBasketCreation(anyString());

            MarketBasketResponseDto result = service.createBasket(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(MarketBasket.class));
        }
    }

    @Nested
    @DisplayName("updateBasket() Tests")
    class UpdateBasketTests {

        @Test
        @DisplayName("Should update segment name and description")
        void shouldUpdateBasketNameAndDescription() {
            UpdateBasketCommand command = new UpdateBasketCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testBasket));
            when(repository.save(any(MarketBasket.class))).thenReturn(testBasket);
            when(mapper.toResponseDto(any(MarketBasket.class))).thenReturn(testResponseDto);

            MarketBasketResponseDto result = service.updateBasket(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(MarketBasket.class));
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenBasketNotFound() {
            UpdateBasketCommand command = new UpdateBasketCommand(
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

            assertThatThrownBy(() -> service.updateBasket(command))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Basket not found");
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            MarketBasket otherTenantBasket = MarketBasket.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Basket")
                    .criteria(testCriteria)
                    .build();

            UpdateBasketCommand command = new UpdateBasketCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantBasket));

            assertThatThrownBy(() -> service.updateBasket(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should not update when name is null")
        void shouldNotUpdateWhenNameNull() {
            UpdateBasketCommand command = new UpdateBasketCommand(
                    SEGMENT_ID,
                    null,
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testBasket));
            when(repository.save(any(MarketBasket.class))).thenReturn(testBasket);
            when(mapper.toResponseDto(any(MarketBasket.class))).thenReturn(testResponseDto);

            service.updateBasket(command);

            ArgumentCaptor<MarketBasket> captor = ArgumentCaptor.forClass(MarketBasket.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Customers");
        }
    }

    @Nested
    @DisplayName("deleteBasket() Tests")
    class DeleteBasketTests {

        @Test
        @DisplayName("Should delete segment successfully")
        void shouldDeleteBasketSuccessfully() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testBasket));
            doNothing().when(businessPolicy).validateBasketDeletion(any(MarketBasket.class));
            doNothing().when(repository).deleteById(SEGMENT_ID);

            service.deleteBasket(SEGMENT_ID, TENANT_ID, USER_ID);

            verify(businessPolicy).validateBasketDeletion(testBasket);
            verify(repository).deleteById(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent segment")
        void shouldThrowWhenDeletingNonExistent() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteBasket("nonexistent-id", TENANT_ID, USER_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before deletion")
        void shouldValidatePolicyBeforeDeletion() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testBasket));
            doThrow(new ValidationException("Cannot delete active segment with customers"))
                    .when(businessPolicy).validateBasketDeletion(any(MarketBasket.class));

            assertThatThrownBy(() -> service.deleteBasket(SEGMENT_ID, TENANT_ID, USER_ID))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot delete active segment");

            verify(repository, never()).deleteById(anyString());
        }
    }

    @Nested
    @DisplayName("getBasketById() Tests")
    class GetBasketByIdTests {

        @Test
        @DisplayName("Should return segment when found")
        void shouldReturnBasketWhenFound() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testBasket));
            when(mapper.toResponseDto(any(MarketBasket.class))).thenReturn(testResponseDto);

            MarketBasketResponseDto result = service.getBasketById(SEGMENT_ID, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenBasketNotFound() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getBasketById("nonexistent-id", TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            MarketBasket otherTenantBasket = MarketBasket.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Basket")
                    .criteria(testCriteria)
                    .build();

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantBasket));

            assertThatThrownBy(() -> service.getBasketById(SEGMENT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getBasketsByTenant() Tests")
    class GetBasketsByTenantTests {

        @Test
        @DisplayName("Should return empty paged response")
        void shouldReturnEmptyPagedResponse() {
            PagedResponseDto<MarketBasketResponseDto> result = service.getBasketsByTenant(
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
            PagedResponseDto<MarketBasketResponseDto> result = service.getBasketsByTenant(
                    TENANT_ID,
                    BasketType.BEHAVIORAL,
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
    @DisplayName("addCustomersToBasket() Tests")
    class AddCustomersTests {

        @Test
        @DisplayName("Should add customers successfully")
        void shouldAddCustomersSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2", "cust-3");
            AddCustomersToBasketCommand command = new AddCustomersToBasketCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testBasket));
            when(repository.save(any(MarketBasket.class))).thenReturn(testBasket);
            when(mapper.toResponseDto(any(MarketBasket.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateCustomerAddition(any(), anyInt());

            MarketBasketResponseDto result = service.addCustomersToBasket(command);

            assertThat(result).isNotNull();
            verify(businessPolicy).validateCustomerAddition(testBasket, 3);
            verify(repository).save(testBasket);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenBasketNotFound() {
            List<String> customerIds = List.of("cust-1");
            AddCustomersToBasketCommand command = new AddCustomersToBasketCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.addCustomersToBasket(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before adding customers")
        void shouldValidatePolicyBeforeAdding() {
            List<String> customerIds = List.of("cust-1");
            AddCustomersToBasketCommand command = new AddCustomersToBasketCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testBasket));
            doThrow(new ValidationException("Basket is full"))
                    .when(businessPolicy).validateCustomerAddition(any(), anyInt());

            assertThatThrownBy(() -> service.addCustomersToBasket(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Basket is full");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeCustomersFromBasket() Tests")
    class RemoveCustomersTests {

        @Test
        @DisplayName("Should remove customers successfully")
        void shouldRemoveCustomersSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2");
            RemoveCustomersFromBasketCommand command = new RemoveCustomersFromBasketCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testBasket));
            when(repository.save(any(MarketBasket.class))).thenReturn(testBasket);
            when(mapper.toResponseDto(any(MarketBasket.class))).thenReturn(testResponseDto);

            MarketBasketResponseDto result = service.removeCustomersFromBasket(command);

            assertThat(result).isNotNull();
            verify(repository).save(testBasket);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenBasketNotFound() {
            List<String> customerIds = List.of("cust-1");
            RemoveCustomersFromBasketCommand command = new RemoveCustomersFromBasketCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeCustomersFromBasket(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("analyzeBasket() Tests")
    class AnalyzeBasketTests {

        @Test
        @DisplayName("Should analyze active segment successfully")
        void shouldAnalyzeActiveBasketSuccessfully() {
            Map<String, Object> analysisOptions = new HashMap<>();
            AnalyzeBasketCommand command = new AnalyzeBasketCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    analysisOptions
            );

            BasketAnalysisResponseDto analysisResponse = new BasketAnalysisResponseDto(
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

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testBasket));
            when(analysisService.analyzeBasket(eq(testBasket), eq(analysisOptions)))
                    .thenReturn(analysisResponse);

            BasketAnalysisResponseDto result = service.analyzeBasket(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
            assertThat(result.confidenceScore()).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should throw exception when analyzing inactive segment")
        void shouldThrowWhenAnalyzingInactiveBasket() {
            MarketBasket inactiveBasket = MarketBasket.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Basket")
                    .criteria(testCriteria)
                    .status(BasketStatus.INACTIVE)
                    .build();

            AnalyzeBasketCommand command = new AnalyzeBasketCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(inactiveBasket));

            assertThatThrownBy(() -> service.analyzeBasket(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when analyzing draft segment")
        void shouldThrowWhenAnalyzingDraftBasket() {
            MarketBasket draftBasket = MarketBasket.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Basket")
                    .criteria(testCriteria)
                    .status(BasketStatus.DRAFT)
                    .build();

            AnalyzeBasketCommand command = new AnalyzeBasketCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(draftBasket));

            assertThatThrownBy(() -> service.analyzeBasket(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalyzingNonExistent() {
            AnalyzeBasketCommand command = new AnalyzeBasketCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.analyzeBasket(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
