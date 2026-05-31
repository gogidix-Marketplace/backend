package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.port.in;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.CreateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.UpdateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.response.ServiceLevelAgreementResponseDto;
import java.util.List;
/**
 * Input port for ServiceLevelAgreement use cases
 */
public interface IServiceLevelAgreementUseCase {
    ServiceLevelAgreementResponseDto create(CreateServiceLevelAgreementRequestDto dto);
    ServiceLevelAgreementResponseDto findById(String id);
    List<ServiceLevelAgreementResponseDto> findAll();
    List<ServiceLevelAgreementResponseDto> findByStatus(String status);
    List<ServiceLevelAgreementResponseDto> findByServiceType(String serviceType);
    List<ServiceLevelAgreementResponseDto> findActive();
    ServiceLevelAgreementResponseDto update(String id, UpdateServiceLevelAgreementRequestDto dto);
    void delete(String id);
    void deactivate(String id);
}
