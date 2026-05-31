package com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.port.in;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.request.CreateMessageQueueRequestDto;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.response.MessageQueueResponseDto;
import java.util.List;
public interface IMessageQueueUseCase {
    MessageQueueResponseDto create(CreateMessageQueueRequestDto dto);
    MessageQueueResponseDto findById(String id);
    List<MessageQueueResponseDto> findAll();
    List<MessageQueueResponseDto> findByStatus(String status);
    List<MessageQueueResponseDto> findByType(String type);
    MessageQueueResponseDto update(String id, CreateMessageQueueRequestDto dto);
    void delete(String id);
    void activate(String id);
    void deactivate(String id);
}
