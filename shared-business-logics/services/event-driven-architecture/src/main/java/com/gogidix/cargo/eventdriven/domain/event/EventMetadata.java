package com.gogidix.cargo.eventdriven.domain.event;

import java.util.HashMap;
import java.util.Map;

public class EventMetadata {
    private String schemaVersion;
    private String source;
    private String traceId;
    private Map<String, String> headers;

    public EventMetadata() { this.headers = new HashMap<>(); }
    public String getSchemaVersion() { return schemaVersion; }
    public void setSchemaVersion(String schemaVersion) { this.schemaVersion = schemaVersion; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }
    public void addHeader(String key, String value) { this.headers.put(key, value); }
}
