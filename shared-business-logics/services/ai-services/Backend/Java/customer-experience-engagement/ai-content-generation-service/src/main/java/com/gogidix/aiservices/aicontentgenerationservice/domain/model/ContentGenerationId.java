package com.gogidix.aiservices.aicontentgenerationservice.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique Content Generation identifier.
 */
public class ContentGenerationId {

    private final UUID value;

    public ContentGenerationId() {
        this.value = UUID.randomUUID();
    }

    public ContentGenerationId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("ContentGenerationId value cannot be null");
        }
        this.value = value;
    }

    public static ContentGenerationId randomId() {
        return new ContentGenerationId(UUID.randomUUID());
    }

    public static ContentGenerationId fromString(String uuid) {
        try {
            return new ContentGenerationId(UUID.fromString(uuid));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid UUID string: " + uuid, e);
        }
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentGenerationId that = (ContentGenerationId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
