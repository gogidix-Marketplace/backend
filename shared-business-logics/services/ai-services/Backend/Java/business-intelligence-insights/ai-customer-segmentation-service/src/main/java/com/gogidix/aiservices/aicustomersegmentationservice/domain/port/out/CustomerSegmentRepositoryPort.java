package com.gogidix.aiservices.aicustomersegmentationservice.domain.port.out;

import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.CustomerSegment;

import java.util.Optional;

/**
 * Output port for segment repository operations.
 * This is the persistence abstraction used by the application layer.
 */
public interface CustomerSegmentRepositoryPort {

    /**
     * Save a segment.
     *
     * @param segment the segment to save
     * @return the saved segment
     */
    CustomerSegment save(CustomerSegment segment);

    /**
     * Find a segment by ID.
     *
     * @param id the segment ID
     * @return the segment if found
     */
    Optional<CustomerSegment> findById(String id);

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
