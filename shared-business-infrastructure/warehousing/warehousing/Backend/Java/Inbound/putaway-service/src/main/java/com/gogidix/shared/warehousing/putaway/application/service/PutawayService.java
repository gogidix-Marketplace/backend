package com.gogidix.shared.warehousing.putaway.application.service;

import com.gogidix.shared.warehousing.putaway.application.command.CreatePutawayTaskCommand;
import com.gogidix.shared.warehousing.putaway.application.dto.PutawayTaskDTO;
import com.gogidix.shared.warehousing.putaway.application.mapper.PutawayMapper;
import com.gogidix.shared.warehousing.putaway.domain.entity.PutawayTask;
import com.gogidix.shared.warehousing.putaway.domain.repository.PutawayTaskRepository;
import com.gogidix.shared.warehousing.putaway.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PutawayService {

    private final PutawayTaskRepository taskRepository;
    private final PutawayMapper putawayMapper;

    public PutawayTaskDTO createTask(CreatePutawayTaskCommand command) {
        String tenantId = TenantContext.getCurrentTenantId();
        PutawayTask task = putawayMapper.toEntity(command);
        task.setTenantId(tenantId);
        task.setTaskNumber("PT-" + System.currentTimeMillis());
        task.setStatus(PutawayTask.TaskStatus.PENDING);
        return putawayMapper.toDTO(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public List<PutawayTaskDTO> getPendingTasks() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<PutawayTask> tasks = taskRepository.findByTenantIdAndStatus(tenantId, PutawayTask.TaskStatus.PENDING);
        return putawayMapper.toDTOList(tasks);
    }

    public PutawayTaskDTO completeTask(String id) {
        PutawayTask task = taskRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
        task.setStatus(PutawayTask.TaskStatus.COMPLETED);
        task.setCompletedDate(LocalDateTime.now());
        return putawayMapper.toDTO(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public List<PutawayTaskDTO> getTasksByReceipt(String receiptId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<PutawayTask> tasks = taskRepository.findByTenantIdAndReceiptId(tenantId, receiptId);
        return putawayMapper.toDTOList(tasks);
    }
}
