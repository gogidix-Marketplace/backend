package shared.exception.ErrorResponse;

import java.util.Date;

/**
 * Standard error response for ai-notification-service.
 */
public class ErrorResponse {
    private int status;
    private Date timestamp;
    private String message;
    private String details;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, Date timestamp, String message, String details) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    // Getters
    public int getStatus() { return status; }
    public Date getTimestamp() { return timestamp; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
}
