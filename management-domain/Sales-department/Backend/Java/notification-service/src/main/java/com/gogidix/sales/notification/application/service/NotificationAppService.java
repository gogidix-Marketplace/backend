package com.gogidix.sales.notification.application.service;

import com.gogidix.sales.notification.domain.model.Notification;
import com.gogidix.sales.notification.domain.repository.NotificationRepository;
import com.gogidix.sales.notification.application.dto.request.NotificationRequestDto;
import com.gogidix.sales.notification.application.dto.response.NotificationResponseDto;
import com.gogidix.sales.notification.application.mapper.NotificationMapper;
import com.gogidix.sales.notification.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationAppService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationResponseDto create(NotificationRequestDto dto) {
        Notification entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public NotificationResponseDto getById(String id) {
        Notification entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Notification not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<NotificationResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public void delete(String id) { repository.deleteById(id); }
}
