package com.gogidix.sysadmin.accessrequest.application.service;

import com.gogidix.sysadmin.accessrequest.domain.model.AccessRequest;
import com.gogidix.sysadmin.accessrequest.domain.repository.AccessRequestRepository;
import com.gogidix.sysadmin.accessrequest.application.dto.AccessRequestDto;
import com.gogidix.sysadmin.accessrequest.application.dto.AccessRequestResponseDto;
import com.gogidix.sysadmin.accessrequest.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessRequestService {

    private final AccessRequestRepository repository;

    public AccessRequestResponseDto create(AccessRequestDto dto) {
        AccessRequest request = new AccessRequest();
        request.setTenantId(dto.getTenantId());
        request.setRequestNumber("AR-" + UUID.randomUUID().toString().substring(0, 8));
        request.setRequestedFor(dto.getRequestedFor());
        request.setRequestType(AccessRequest.RequestType.valueOf(dto.getRequestType()));
        request.setResourceType(dto.getResourceType());
        request.setResourceIds(dto.getResourceIds());
        request.setAccessLevel(dto.getAccessLevel());
        request.setJustification(dto.getJustification());
        request.setStartDateTime(dto.getStartDateTime());
        request.setEndDateTime(dto.getEndDateTime());
        request = repository.save(request);
        return toResponseDto(request);
    }

    public AccessRequestResponseDto getById(String id) {
        AccessRequest request = repository.findById(id).orElseThrow(() -> new RuntimeException("AccessRequest not found: " + id));
        return toResponseDto(request);
    }

    public List<AccessRequestResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    public void delete(String id) { repository.deleteById(id); }

    private AccessRequestResponseDto toResponseDto(AccessRequest r) {
        return AccessRequestResponseDto.builder().id(r.getId()).tenantId(r.getTenantId()).requestNumber(r.getRequestNumber()).requestedBy(r.getRequestedBy()).requestedFor(r.getRequestedFor()).requestType(r.getRequestType() != null ? r.getRequestType().name() : null).status(r.getStatus() != null ? r.getStatus().name() : null).resourceType(r.getResourceType()).resourceIds(r.getResourceIds()).accessLevel(r.getAccessLevel()).justification(r.getJustification()).createdAt(r.getCreatedAt()).updatedAt(r.getUpdatedAt()).build();
    }
}
