package com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment.Dimensions;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment.PackageInfo;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.AssignCourierResponse;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.DimensionsDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.PackageDto;
import org.springframework.stereotype.Component;

@Component
public class EcommerceAssignmentMapper {

    public AssignCourierResponse toResponse(EcommerceCourierAssignment assignment) {
        return new AssignCourierResponse(
            assignment.getId(),
            assignment.getCourierId(),
            assignment.getCourierName(),
            assignment.getCourierPhone(),
            assignment.getVehicleType() != null ? assignment.getVehicleType().name() : null,
            assignment.getEstimatedPickupTime(),
            assignment.getEstimatedDeliveryTime(),
            assignment.getTrackingId(),
            assignment.getStatus() != null ? assignment.getStatus().name() : null
        );
    }

    public List<PackageInfo> toPackageInfoList(List<PackageDto> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
            .map(this::toPackageInfo)
            .collect(Collectors.toList());
    }

    private PackageInfo toPackageInfo(PackageDto dto) {
        return new PackageInfo(
            dto.description(),
            dto.weight(),
            dto.quantity(),
            dto.dimensions() != null ? toDimensions(dto.dimensions()) : null
        );
    }

    private Dimensions toDimensions(DimensionsDto dto) {
        return new Dimensions(dto.length(), dto.width(), dto.height());
    }
}
