package com.gogidix.centralconfiguration.configserver.application.mapper;

import com.gogidix.centralconfiguration.configserver.application.dto.request.CreateConfigRequestDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigHistoryResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigurationResponseDto;
import com.gogidix.centralconfiguration.configserver.domain.model.Configuration;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationHistory;
import com.gogidix.centralconfiguration.configserver.domain.port.in.CreateConfigCommand;
import com.gogidix.centralconfiguration.configserver.domain.port.in.UpdateConfigCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for Configuration entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface ConfigMapper {

    ConfigurationResponseDto toResponseDto(Configuration configuration);

    ConfigHistoryResponseDto toHistoryResponseDto(ConfigurationHistory history);

    @Mapping(target = "createdBy", source = "createdBy", defaultValue = "system")
    CreateConfigCommand toCreateCommand(CreateConfigRequestDto dto, String tenantId, String createdBy);

    UpdateConfigCommand toUpdateCommand(Long configurationId, String tenantId, String configValue,
                                        Boolean isEncrypted, String description, String updatedBy, String changeReason);

    @Named("maskIfEncrypted")
    static String maskIfEncrypted(String value, Boolean isEncrypted) {
        if (Boolean.TRUE.equals(isEncrypted) && value != null) {
            return "******";
        }
        return value;
    }
}
