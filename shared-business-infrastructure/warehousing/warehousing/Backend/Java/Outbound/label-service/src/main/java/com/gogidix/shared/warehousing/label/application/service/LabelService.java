package com.gogidix.shared.warehousing.label.application.service;

import com.gogidix.shared.warehousing.label.application.command.GenerateLabelCommand;
import com.gogidix.shared.warehousing.label.application.dto.ShippingLabelDTO;
import com.gogidix.shared.warehousing.label.application.mapper.LabelMapper;
import com.gogidix.shared.warehousing.label.domain.entity.ShippingLabel;
import com.gogidix.shared.warehousing.label.domain.repository.ShippingLabelRepository;
import com.gogidix.shared.warehousing.label.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LabelService {

    private final ShippingLabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public ShippingLabelDTO generateLabel(GenerateLabelCommand command) {
        String tenantId = TenantContext.getCurrentTenantId();

        ShippingLabel label = ShippingLabel.builder()
            .tenantId(tenantId)
            .labelNumber("LBL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
            .shipmentId(command.getShipmentId())
            .orderId(command.getOrderId())
            .carrier(command.getCarrier())
            .serviceLevel(command.getServiceLevel())
            .format(command.getFormat())
            .templateId(command.getTemplateId())
            .labelWidth(4) // inches
            .labelHeight(6) // inches
            .labelUrl("/labels/" + UUID.randomUUID() + ".pdf")
            .build();

        return labelMapper.toDTO(labelRepository.save(label));
    }

    @Transactional(readOnly = true)
    public List<ShippingLabelDTO> getLabelsByShipment(String shipmentId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<ShippingLabel> labels = labelRepository.findByTenantIdAndShipmentId(tenantId, shipmentId);
        return labelMapper.toDTOList(labels);
    }

    @Transactional(readOnly = true)
    public List<ShippingLabelDTO> getAllLabels() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<ShippingLabel> labels = labelRepository.findByTenantId(tenantId);
        return labelMapper.toDTOList(labels);
    }
}
