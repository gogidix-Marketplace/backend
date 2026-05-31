package com.gogidix.shared.courier.ecommerce.application.command;

import com.gogidix.shared.courier.ecommerce.domain.valueobject.AssignmentPriority;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.HubDetailsDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.PackageDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.ZoneInfoDto;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignCourierCommand {
    private String orderId;
    private String subOrderId;
    private String vendorId;
    private DeliveryType deliveryType;
    private ZoneInfoDto pickupZone;
    private ZoneInfoDto deliveryZone;
    private List<PackageDto> packages;
    private AssignmentPriority priority;
    private Instant scheduledFor;
    private boolean requiresHubProcessing;
    private HubDetailsDto hubDetails;
}
