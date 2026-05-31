package com.gogidix.corporatecms.application.mapper;

import com.gogidix.corporatecms.application.dto.JobDTO;
import com.gogidix.corporatecms.domain.model.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for Job entity and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface JobMapper {

    JobDTO toDto(Job job);

    Job toEntity(JobDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(JobDTO dto, @MappingTarget Job job);

    List<JobDTO> toDtoList(List<Job> jobList);
}
