package com.gogidix.shared.warehousing.putaway.application.mapper;

import com.gogidix.shared.warehousing.putaway.application.command.CreatePutawayTaskCommand;
import com.gogidix.shared.warehousing.putaway.application.dto.PutawayTaskDTO;
import com.gogidix.shared.warehousing.putaway.domain.entity.PutawayTask;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PutawayMapper {

    PutawayTask toEntity(CreatePutawayTaskCommand command);
    PutawayTaskDTO toDTO(PutawayTask task);
    List<PutawayTaskDTO> toDTOList(List<PutawayTask> tasks);
}
