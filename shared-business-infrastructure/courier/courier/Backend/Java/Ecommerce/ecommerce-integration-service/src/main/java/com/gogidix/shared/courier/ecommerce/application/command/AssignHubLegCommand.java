package com.gogidix.shared.courier.ecommerce.application.command;

import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryLeg;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.CustomerDeliveryDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.HubInfoDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.PackageDto;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignHubLegCommand {
    private String orderId;
    private String subOrderId;
    private DeliveryLeg leg;
    private HubInfoDto originHub;
    private HubInfoDto destinationHub;
    private CustomerDeliveryDto customerDelivery;
    private List<PackageDto> packages;
    private Instant scheduledFor;
}
