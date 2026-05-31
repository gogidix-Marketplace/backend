package com.gogidix.corporatecms.domain.enums;

import lombok.Getter;

/**
 * Enumeration representing job posting status.
 */
@Getter
public enum JobStatus {
    DRAFT("draft", "Job posting is being drafted"),
    OPEN("open", "Job is open for applications"),
    ON_HOLD("on_hold", "Job is temporarily on hold"),
    CLOSED("closed", "Job is closed"),
    FILLED("filled", "Position has been filled");

    private final String code;
    private final String description;

    JobStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static JobStatus fromCode(String code) {
        for (JobStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown job status: " + code);
    }
}
