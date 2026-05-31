package com.gogidix.shared.infrastructure.services.security.orchestration.domain.port.in;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.CreateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.UpdateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.response.SecurityWorkflowResponseDto;
import java.util.List;
/**
 * Input port for SecurityWorkflow use cases.
 */
public interface SecurityWorkflowPort {
    SecurityWorkflowResponseDto create(CreateSecurityWorkflowRequestDto dto);
    SecurityWorkflowResponseDto findById(String id);
    List<SecurityWorkflowResponseDto> findAll();
    List<SecurityWorkflowResponseDto> findByStatus(String status);
    SecurityWorkflowResponseDto update(String id, UpdateSecurityWorkflowRequestDto dto);
    void delete(String id);
    SecurityWorkflowResponseDto activate(String id);
    SecurityWorkflowResponseDto deactivate(String id);
    SecurityWorkflowResponseDto execute(String id);
}
