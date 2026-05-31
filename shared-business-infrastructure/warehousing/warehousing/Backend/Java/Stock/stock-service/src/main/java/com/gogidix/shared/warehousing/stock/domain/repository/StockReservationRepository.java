package com.gogidix.shared.warehousing.stock.domain.repository;

import com.gogidix.shared.warehousing.stock.domain.entity.StockReservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockReservationRepository extends MongoRepository<StockReservation, String> {

    List<StockReservation> findByStatusAndExpiresAtBefore(StockReservation.ReservationStatus status, LocalDateTime expiresAt);
}
