package com.gogidix.universal.tracking.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to update a tracking session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSessionCommand {

    private String sessionId;

    private String referrer;

    private String landingPage;

    private String campaign;

    private java.util.Map<String, Object> metadata;

    private Boolean endSession;
}
