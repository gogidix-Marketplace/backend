package com.gogidix.shared.courier.ecommerce.application.query;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrackOrderQuery {
    private String orderId;
}
