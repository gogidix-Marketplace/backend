package com.gogidix.aiservices.aidatavalidation.domain;

import lombok.Value;
import lombok.Builder;
import lombok.NonNull;

import java.util.Map;

/**
 * Value object representing a data schema definition.
 */
@Value
@Builder
public class SchemaDefinition {

    @NonNull
    String schemaId;

    @NonNull
    String schemaName;

    @NonNull
    SchemaFormat format;

    @NonNull
    Map<String, Object> schemaDefinition;

    @NonNull
    String version;

    public enum SchemaFormat {
        JSON_SCHEMA,
        AVRO,
        PARQUET,
        PROTOBUF,
        CSV
    }

    public boolean isNewerThan(SchemaDefinition other) {
        if (other == null) {
            return true;
        }
        String[] thisParts = this.version.split("\\.");
        String[] otherParts = other.version.split("\\.");

        for (int i = 0; i < Math.max(thisParts.length, otherParts.length); i++) {
            int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
            int otherPart = i < otherParts.length ? Integer.parseInt(otherParts[i]) : 0;

            if (thisPart != otherPart) {
                return thisPart > otherPart;
            }
        }
        return false;
    }
}
