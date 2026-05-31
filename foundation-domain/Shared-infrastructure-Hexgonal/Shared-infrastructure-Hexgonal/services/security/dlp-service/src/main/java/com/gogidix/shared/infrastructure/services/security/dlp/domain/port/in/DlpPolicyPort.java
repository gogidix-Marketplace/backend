package com.gogidix.shared.infrastructure.services.security.dlp.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.CreateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.UpdateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.response.DlpPolicyResponseDto;

import java.util.List;

/**
 * Input port for DlpPolicy use cases.
 */
public interface DlpPolicyPort {

    DlpPolicyResponseDto create(CreateDlpPolicyRequestDto dto);

    DlpPolicyResponseDto findById(String id);

    List<DlpPolicyResponseDto> findAll();

    List<DlpPolicyResponseDto> findByStatus(String status);

    DlpPolicyResponseDto update(String id, UpdateDlpPolicyRequestDto dto);

    void delete(String id);

    DlpPolicyResponseDto activate(String id);

    DlpPolicyResponseDto deactivate(String id);
}
