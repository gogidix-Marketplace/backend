package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.port.out;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.MarketBasket;

import java.util.Optional;

/**
 * Output port for segment repository operations.
 * This is the persistence abstraction used by the application layer.
 */
public interface MarketBasketRepositoryPort {

    /**
     * Save a segment.
     *
     * @param segment the segment to save
     * @return the saved segment
     */
    MarketBasket save(MarketBasket segment);

    /**
     * Find a segment by ID.
     *
     * @param id the segment ID
     * @return the segment if found
     */
    Optional<MarketBasket> findById(String id);

    /**
     * Delete a segment.
     *
     * @param id the segment ID
     */
    void deleteById(String id);

    /**
     * Check if a segment exists.
     *
     * @param id the segment ID
     * @return true if exists, false otherwise
     */
    boolean existsById(String id);
}
