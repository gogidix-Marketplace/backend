package com.gogidix.customersupport.customerportal.application.mapper;

import com.gogidix.customersupport.customerportal.application.dto.CustomerProfileRequestDto;
import com.gogidix.customersupport.customerportal.application.dto.CustomerProfileResponseDto;
import com.gogidix.customersupport.customerportal.domain.model.CustomerProfile;
import org.springframework.stereotype.Component;

@Component
public class CustomerProfileMapper {

    public CustomerProfile toEntity(CustomerProfileRequestDto dto, String tenantId) {
        CustomerProfile profile = CustomerProfile.create(
                dto.getCustomerId(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName()
        );

        profile.setTenantId(tenantId);
        profile.setUserId(dto.getUserId());
        profile.setPhone(dto.getPhone());
        profile.setSecondaryPhone(dto.getSecondaryPhone());
        profile.setCompanyName(dto.getCompanyName());
        profile.setCompanyId(dto.getCompanyId());
        profile.setCustomerType(dto.getCustomerType());
        profile.setTier(dto.getTier());
        profile.setPreferredLanguage(dto.getPreferredLanguage());
        profile.setTimezone(dto.getTimezone());
        profile.setCountry(dto.getCountry());
        profile.setCommunicationChannels(dto.getCommunicationChannels());
        profile.setTags(dto.getTags());
        profile.setCustomFields(dto.getCustomFields());

        if (dto.getAddress() != null) {
            profile.setAddress(CustomerProfile.Address.builder()
                    .street(dto.getAddress().getStreet())
                    .city(dto.getAddress().getCity())
                    .state(dto.getAddress().getState())
                    .postalCode(dto.getAddress().getPostalCode())
                    .country(dto.getAddress().getCountry())
                    .build());
        }

        if (dto.getPreferences() != null) {
            profile.setPreferences(CustomerProfile.CustomerPreferences.builder()
                    .emailNotifications(dto.getPreferences().getEmailNotifications())
                    .smsNotifications(dto.getPreferences().getSmsNotifications())
                    .phoneNotifications(dto.getPreferences().getPhoneNotifications())
                    .pushNotifications(dto.getPreferences().getPushNotifications())
                    .preferredContactMethod(dto.getPreferences().getPreferredContactMethod())
                    .notificationPreferences(dto.getPreferences().getNotificationPreferences())
                    .build());
        }

        return profile;
    }

    public CustomerProfileResponseDto toResponseDto(CustomerProfile entity) {
        CustomerProfileResponseDto.AddressDto addressDto = null;
        if (entity.getAddress() != null) {
            addressDto = CustomerProfileResponseDto.AddressDto.builder()
                    .street(entity.getAddress().getStreet())
                    .city(entity.getAddress().getCity())
                    .state(entity.getAddress().getState())
                    .postalCode(entity.getAddress().getPostalCode())
                    .country(entity.getAddress().getCountry())
                    .build();
        }

        CustomerProfileResponseDto.CustomerPreferencesDto preferencesDto = null;
        if (entity.getPreferences() != null) {
            preferencesDto = CustomerProfileResponseDto.CustomerPreferencesDto.builder()
                    .emailNotifications(entity.getPreferences().getEmailNotifications())
                    .smsNotifications(entity.getPreferences().getSmsNotifications())
                    .phoneNotifications(entity.getPreferences().getPhoneNotifications())
                    .pushNotifications(entity.getPreferences().getPushNotifications())
                    .preferredContactMethod(entity.getPreferences().getPreferredContactMethod())
                    .notificationPreferences(entity.getPreferences().getNotificationPreferences())
                    .build();
        }

        return CustomerProfileResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .customerId(entity.getCustomerId())
                .userId(entity.getUserId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .secondaryPhone(entity.getSecondaryPhone())
                .companyName(entity.getCompanyName())
                .companyId(entity.getCompanyId())
                .customerType(entity.getCustomerType())
                .tier(entity.getTier())
                .preferredLanguage(entity.getPreferredLanguage())
                .timezone(entity.getTimezone())
                .country(entity.getCountry())
                .address(addressDto)
                .preferences(preferencesDto)
                .communicationChannels(entity.getCommunicationChannels())
                .tags(entity.getTags())
                .customFields(entity.getCustomFields())
                .isActive(entity.getIsActive())
                .lastLoginAt(entity.getLastLoginAt())
                .accountCreatedAt(entity.getAccountCreatedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
