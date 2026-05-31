package com.gogidix.aiservices.aiuserprofilingservice.application.service;

import com.gogidix.aiservices.aiuserprofilingservice.application.command.AddUsersToProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.AnalyzeProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.CreateProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.RemoveUsersFromProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.UpdateProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.UserProfileResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.ProfileAnalysisResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.mapper.UserProfileMapper;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.UserProfile;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileCriteria;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileStatus;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;
import com.gogidix.aiservices.aiuserprofilingservice.domain.port.out.UserProfileRepositoryPort;
import com.gogidix.aiservices.aiuserprofilingservice.domain.policy.ProfileBusinessPolicy;
import com.gogidix.aiservices.aiuserprofilingservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aiuserprofilingservice.shared.exception.ValidationException;
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
@DisplayName("UserProfileApplicationService Tests")
class UserProfileApplicationServiceTest {

    @Mock
    private UserProfileRepositoryPort repository;

    @Mock
    private UserProfileMapper mapper;

    @Mock
    private ProfileBusinessPolicy businessPolicy;

    @Mock
    private ProfileAnalysisService analysisService;

    @InjectMocks
    private UserProfileApplicationService service;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String SEGMENT_ID = "segment-789";

    private UserProfile testProfile;
    private UserProfileResponseDto testResponseDto;
    private ProfileCriteria testCriteria;

    @BeforeEach
    void setUp() {
        testCriteria = ProfileCriteria.builder()
                .type(ProfileCriteria.CriteriaType.CUSTOM)
                .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .build();

        testProfile = UserProfile.builder()
                .segmentId(SEGMENT_ID)
                .tenantId(TENANT_ID)
                .name("Premium Users")
                .description("High value customers")
                .criteria(testCriteria)
                .status(ProfileStatus.ACTIVE)
                .segmentType(ProfileType.BEHAVIORAL)
                .customerCount(100L)
                .build();

        testResponseDto = new UserProfileResponseDto(
                SEGMENT_ID,
                "Premium Users",
                "High value customers",
                ProfileType.BEHAVIORAL,
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
    @DisplayName("createProfile() Tests")
    class CreateProfileTests {

        @Test
        @DisplayName("Should create segment successfully")
        void shouldCreateProfileSuccessfully() {
            CreateProfileCommand command = new CreateProfileCommand(
                    "Premium Users",
                    "High value customers",
                    ProfileType.BEHAVIORAL,
                    Map.of("field", "lifetimeValue", "operator", ">", "value", 1000),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(UserProfile.class))).thenReturn(testProfile);
            when(mapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateProfileCreation(anyString());

            UserProfileResponseDto result = service.createProfile(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);

            ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Users");
            verify(businessPolicy).validateProfileCreation(TENANT_ID);
        }

        @Test
        @DisplayName("Should throw exception when criteria map is null")
        void shouldThrowWhenCriteriaNull() {
            CreateProfileCommand command = new CreateProfileCommand(
                    "Premium Users",
                    "High value customers",
                    ProfileType.BEHAVIORAL,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createProfile(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should throw exception when criteria map is empty")
        void shouldThrowWhenCriteriaEmpty() {
            CreateProfileCommand command = new CreateProfileCommand(
                    "Premium Users",
                    "High value customers",
                    ProfileType.BEHAVIORAL,
                    Map.of(),
                    TENANT_ID,
                    USER_ID
            );

            assertThatThrownBy(() -> service.createProfile(command))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criteria cannot be null or empty");
        }

        @Test
        @DisplayName("Should use default values when criteria map missing fields")
        void shouldUseDefaultsWhenCriteriaMissingFields() {
            CreateProfileCommand command = new CreateProfileCommand(
                    "Premium Users",
                    "High value customers",
                    ProfileType.BEHAVIORAL,
                    Map.of("value", 500),
                    TENANT_ID,
                    USER_ID
            );

            when(repository.save(any(UserProfile.class))).thenReturn(testProfile);
            when(mapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateProfileCreation(anyString());

            UserProfileResponseDto result = service.createProfile(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(UserProfile.class));
        }
    }

    @Nested
    @DisplayName("updateProfile() Tests")
    class UpdateProfileTests {

        @Test
        @DisplayName("Should update segment name and description")
        void shouldUpdateProfileNameAndDescription() {
            UpdateProfileCommand command = new UpdateProfileCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testProfile));
            when(repository.save(any(UserProfile.class))).thenReturn(testProfile);
            when(mapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

            UserProfileResponseDto result = service.updateProfile(command);

            assertThat(result).isNotNull();
            verify(repository).save(any(UserProfile.class));
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenProfileNotFound() {
            UpdateProfileCommand command = new UpdateProfileCommand(
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

            assertThatThrownBy(() -> service.updateProfile(command))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Profile not found");
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            UserProfile otherTenantProfile = UserProfile.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Profile")
                    .criteria(testCriteria)
                    .build();

            UpdateProfileCommand command = new UpdateProfileCommand(
                    SEGMENT_ID,
                    "Updated Name",
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantProfile));

            assertThatThrownBy(() -> service.updateProfile(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should not update when name is null")
        void shouldNotUpdateWhenNameNull() {
            UpdateProfileCommand command = new UpdateProfileCommand(
                    SEGMENT_ID,
                    null,
                    "Updated Description",
                    null,
                    null,
                    null,
                    TENANT_ID,
                    USER_ID
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testProfile));
            when(repository.save(any(UserProfile.class))).thenReturn(testProfile);
            when(mapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

            service.updateProfile(command);

            ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
            verify(repository).save(captor.capture());
            assertThat(captor.getValue().getName()).isEqualTo("Premium Users");
        }
    }

    @Nested
    @DisplayName("deleteProfile() Tests")
    class DeleteProfileTests {

        @Test
        @DisplayName("Should delete segment successfully")
        void shouldDeleteProfileSuccessfully() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testProfile));
            doNothing().when(businessPolicy).validateProfileDeletion(any(UserProfile.class));
            doNothing().when(repository).deleteById(SEGMENT_ID);

            service.deleteProfile(SEGMENT_ID, TENANT_ID, USER_ID);

            verify(businessPolicy).validateProfileDeletion(testProfile);
            verify(repository).deleteById(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent segment")
        void shouldThrowWhenDeletingNonExistent() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.deleteProfile("nonexistent-id", TENANT_ID, USER_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before deletion")
        void shouldValidatePolicyBeforeDeletion() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testProfile));
            doThrow(new ValidationException("Cannot delete active segment with customers"))
                    .when(businessPolicy).validateProfileDeletion(any(UserProfile.class));

            assertThatThrownBy(() -> service.deleteProfile(SEGMENT_ID, TENANT_ID, USER_ID))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot delete active segment");

            verify(repository, never()).deleteById(anyString());
        }
    }

    @Nested
    @DisplayName("getProfileById() Tests")
    class GetProfileByIdTests {

        @Test
        @DisplayName("Should return segment when found")
        void shouldReturnProfileWhenFound() {
            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testProfile));
            when(mapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

            UserProfileResponseDto result = service.getProfileById(SEGMENT_ID, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenProfileNotFound() {
            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getProfileById("nonexistent-id", TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should throw exception when segment belongs to different tenant")
        void shouldThrowWhenDifferentTenant() {
            UserProfile otherTenantProfile = UserProfile.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId("other-tenant")
                    .name("Test Profile")
                    .criteria(testCriteria)
                    .build();

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(otherTenantProfile));

            assertThatThrownBy(() -> service.getProfileById(SEGMENT_ID, TENANT_ID))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getProfilesByTenant() Tests")
    class GetProfilesByTenantTests {

        @Test
        @DisplayName("Should return empty paged response")
        void shouldReturnEmptyPagedResponse() {
            PagedResponseDto<UserProfileResponseDto> result = service.getProfilesByTenant(
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
            PagedResponseDto<UserProfileResponseDto> result = service.getProfilesByTenant(
                    TENANT_ID,
                    ProfileType.BEHAVIORAL,
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
    @DisplayName("addUsersToProfile() Tests")
    class AddUsersTests {

        @Test
        @DisplayName("Should add customers successfully")
        void shouldAddUsersSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2", "cust-3");
            AddUsersToProfileCommand command = new AddUsersToProfileCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testProfile));
            when(repository.save(any(UserProfile.class))).thenReturn(testProfile);
            when(mapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);
            doNothing().when(businessPolicy).validateUserAddition(any(), anyInt());

            UserProfileResponseDto result = service.addUsersToProfile(command);

            assertThat(result).isNotNull();
            verify(businessPolicy).validateUserAddition(testProfile, 3);
            verify(repository).save(testProfile);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenProfileNotFound() {
            List<String> customerIds = List.of("cust-1");
            AddUsersToProfileCommand command = new AddUsersToProfileCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.addUsersToProfile(command))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Should validate business policy before adding customers")
        void shouldValidatePolicyBeforeAdding() {
            List<String> customerIds = List.of("cust-1");
            AddUsersToProfileCommand command = new AddUsersToProfileCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testProfile));
            doThrow(new ValidationException("Profile is full"))
                    .when(businessPolicy).validateUserAddition(any(), anyInt());

            assertThatThrownBy(() -> service.addUsersToProfile(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Profile is full");

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("removeUsersFromProfile() Tests")
    class RemoveUsersTests {

        @Test
        @DisplayName("Should remove customers successfully")
        void shouldRemoveUsersSuccessfully() {
            List<String> customerIds = List.of("cust-1", "cust-2");
            RemoveUsersFromProfileCommand command = new RemoveUsersFromProfileCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testProfile));
            when(repository.save(any(UserProfile.class))).thenReturn(testProfile);
            when(mapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

            UserProfileResponseDto result = service.removeUsersFromProfile(command);

            assertThat(result).isNotNull();
            verify(repository).save(testProfile);
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenProfileNotFound() {
            List<String> customerIds = List.of("cust-1");
            RemoveUsersFromProfileCommand command = new RemoveUsersFromProfileCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    customerIds
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeUsersFromProfile(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("analyzeProfile() Tests")
    class AnalyzeProfileTests {

        @Test
        @DisplayName("Should analyze active segment successfully")
        void shouldAnalyzeActiveProfileSuccessfully() {
            Map<String, Object> analysisOptions = new HashMap<>();
            AnalyzeProfileCommand command = new AnalyzeProfileCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    analysisOptions
            );

            ProfileAnalysisResponseDto analysisResponse = new ProfileAnalysisResponseDto(
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

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(testProfile));
            when(analysisService.analyzeProfile(eq(testProfile), eq(analysisOptions)))
                    .thenReturn(analysisResponse);

            ProfileAnalysisResponseDto result = service.analyzeProfile(command);

            assertThat(result).isNotNull();
            assertThat(result.segmentId()).isEqualTo(SEGMENT_ID);
            assertThat(result.confidenceScore()).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should throw exception when analyzing inactive segment")
        void shouldThrowWhenAnalyzingInactiveProfile() {
            UserProfile inactiveProfile = UserProfile.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Profile")
                    .criteria(testCriteria)
                    .status(ProfileStatus.INACTIVE)
                    .build();

            AnalyzeProfileCommand command = new AnalyzeProfileCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(inactiveProfile));

            assertThatThrownBy(() -> service.analyzeProfile(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when analyzing draft segment")
        void shouldThrowWhenAnalyzingDraftProfile() {
            UserProfile draftProfile = UserProfile.builder()
                    .segmentId(SEGMENT_ID)
                    .tenantId(TENANT_ID)
                    .name("Test Profile")
                    .criteria(testCriteria)
                    .status(ProfileStatus.DRAFT)
                    .build();

            AnalyzeProfileCommand command = new AnalyzeProfileCommand(
                    SEGMENT_ID,
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById(SEGMENT_ID)).thenReturn(Optional.of(draftProfile));

            assertThatThrownBy(() -> service.analyzeProfile(command))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("Cannot analyze inactive segment");
        }

        @Test
        @DisplayName("Should throw exception when segment not found")
        void shouldThrowWhenAnalyzingNonExistent() {
            AnalyzeProfileCommand command = new AnalyzeProfileCommand(
                    "nonexistent-id",
                    TENANT_ID,
                    USER_ID,
                    Map.of()
            );

            when(repository.findById("nonexistent-id")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.analyzeProfile(command))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
