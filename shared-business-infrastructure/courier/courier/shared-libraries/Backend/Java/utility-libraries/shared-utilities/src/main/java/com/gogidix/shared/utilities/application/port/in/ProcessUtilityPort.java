package com.gogidix.shared.utilities.application.port.in;

import com.gogidix.shared.utilities.domain.model.ProcessingRequest;
import com.gogidix.shared.utilities.domain.model.UtilityResult;

import java.util.concurrent.CompletableFuture;

/**
 * Port for processing utility operations
 * Defines the contract for utility processing use cases
 */
public interface ProcessUtilityPort {

    /**
     * Process a utility request synchronously
     * 
     * @param request The processing request containing utility operation details
     * @return UtilityResult containing the operation result
     */
    UtilityResult<?> processUtilityRequest(ProcessingRequest request);

    /**
     * Process a utility request asynchronously
     * 
     * @param request The processing request containing utility operation details
     * @return CompletableFuture of UtilityResult containing the operation result
     */
    CompletableFuture<UtilityResult<?>> processUtilityRequestAsync(ProcessingRequest request);
}