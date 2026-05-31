package com.gogidix.dashboard.realtime.domain.model;

/**
 * Compression Type Enumeration
 */
public enum CompressionType {
    NONE(0),
    GZIP(1024),
    LZ4(512);

    private final long minSizeForCompression;

    CompressionType(long minSizeForCompression) {
        this.minSizeForCompression = minSizeForCompression;
    }

    public long getMinSizeForCompression() {
        return minSizeForCompression;
    }
}
