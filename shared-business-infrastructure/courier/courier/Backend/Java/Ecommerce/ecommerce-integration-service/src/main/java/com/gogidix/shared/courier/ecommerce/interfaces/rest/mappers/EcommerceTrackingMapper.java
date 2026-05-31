package com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking.HubStage;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.StageDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.TrackingResponse;
import org.springframework.stereotype.Component;

@Component
public class EcommerceTrackingMapper {

    public TrackingResponse toResponse(EcommerceDeliveryTracking tracking) {
        return new TrackingResponse(
            tracking.getOrderId(),
            tracking.getSubOrderId(),
            tracking.getDeliveryType() != null ? tracking.getDeliveryType().name() : null,
            tracking.getCurrentStage() != null ? tracking.getCurrentStage().name() : null,
            toStageDtos(tracking.getStages()),
            tracking.getEta()
        );
    }

    private List<StageDto> toStageDtos(List<HubStage> stages) {
        if (stages == null) return List.of();
        return stages.stream()
            .map(this::toStageDto)
            .collect(Collectors.toList());
    }

    private StageDto toStageDto(HubStage stage) {
        return new StageDto(
            stage.getStage() != null ? stage.getStage().name() : null,
            stage.getStatus() != null ? stage.getStatus().name() : null,
            stage.getCourierName(),
            stage.getCourierPhone(),
            stage.getHubName(),
            stage.getCompletedAt(),
            stage.getArrivedAt()
        );
    }
}
