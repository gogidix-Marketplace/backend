package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import com.gogidix.shared.courier.ecommerce.application.service.ZoneCourierPoolService;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;
import org.junit.jupiter.api.Test;

class ZoneCourierPoolServiceTest {

    private final ZoneCourierPoolService service = new ZoneCourierPoolService();

    @Test
    void shouldFindAvailableCourier() {
        ZoneCourierPoolService.CourierInfo courier = service.findAvailableCourier("zone-lagos", VehicleType.MOTORCYCLE);

        assertNotNull(courier.courierId());
        assertNotNull(courier.courierName());
        assertNotNull(courier.courierPhone());
        assertEquals(VehicleType.MOTORCYCLE, courier.vehicleType());
    }

    @Test
    void shouldFindCourierWithDefaultVehicleType() {
        ZoneCourierPoolService.CourierInfo courier = service.findAvailableCourier("zone-abuja", null);
        assertEquals(VehicleType.MOTORCYCLE, courier.vehicleType());
    }

    @Test
    void shouldGetAvailableVehicleTypes() {
        Map<String, Integer> types = service.getAvailableVehicleTypes("zone-lagos");

        assertEquals(3, types.size());
        assertTrue(types.containsKey(VehicleType.MOTORCYCLE.name()));
        assertTrue(types.containsKey(VehicleType.VAN.name()));
        assertTrue(types.containsKey(VehicleType.TRUCK.name()));
    }

    @Test
    void shouldGetAvailableCourierCount() {
        int count = service.getAvailableCourierCount("zone-lagos");
        assertTrue(count > 0);
    }

    @Test
    void shouldGetAverageETA() {
        String eta = service.getAverageETA("zone-lagos");
        assertNotNull(eta);
    }

    @Test
    void shouldCheckSurgePricing() {
        boolean surge = service.isSurgePricing("zone-lagos");
        assertFalse(surge);
    }
}
