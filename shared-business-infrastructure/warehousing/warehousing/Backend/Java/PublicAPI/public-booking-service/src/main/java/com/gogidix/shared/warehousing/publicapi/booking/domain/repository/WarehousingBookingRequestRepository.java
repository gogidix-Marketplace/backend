package com.gogidix.shared.warehousing.publicapi.booking.domain.repository;

import com.gogidix.shared.warehousing.publicapi.booking.domain.entity.WarehousingBookingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Warehousing Booking Request entity
 */
@Repository
public interface WarehousingBookingRequestRepository extends MongoRepository<WarehousingBookingRequest, String> {

    List<WarehousingBookingRequest> findByTenantId(String tenantId);

    Optional<WarehousingBookingRequest> findByTenantIdAndBookingNumber(String tenantId, String bookingNumber);

    List<WarehousingBookingRequest> findByTenantIdAndCustomerEmail(String tenantId, String customerEmail);

    List<WarehousingBookingRequest> findByTenantIdAndStatus(String tenantId, WarehousingBookingRequest.BookingStatus status);

    List<WarehousingBookingRequest> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    List<WarehousingBookingRequest> findByTenantIdAndBookingType(String tenantId, WarehousingBookingRequest.BookingType bookingType);

    List<WarehousingBookingRequest> findByTenantIdAndStatusAndConfirmedStartDateBefore(
            String tenantId, WarehousingBookingRequest.BookingStatus status, LocalDateTime date);

    List<WarehousingBookingRequest> findByTenantIdAndCreatedAtBetween(
            String tenantId, LocalDateTime start, LocalDateTime end);

    boolean existsByTenantIdAndBookingNumber(String tenantId, String bookingNumber);
}
