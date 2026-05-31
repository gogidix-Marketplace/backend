package com.gogidix.aiservices.aidatavalidation.domain;

import org.junit.jupiter.api.*;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class SchemaDefinitionTest {
    @Test
    void isNewerThanNull() {
        var s = SchemaDefinition.builder()
            .schemaId("id").schemaName("name").format(SchemaDefinition.SchemaFormat.JSON_SCHEMA)
            .schemaDefinition(Map.of()).version("1.0.0").build();
        assertThat(s.isNewerThan(null)).isTrue();
    }

    @Test
    void isNewerThanOlder() {
        var v2 = SchemaDefinition.builder()
            .schemaId("id").schemaName("name").format(SchemaDefinition.SchemaFormat.JSON_SCHEMA)
            .schemaDefinition(Map.of()).version("2.0.0").build();
        var v1 = SchemaDefinition.builder()
            .schemaId("id").schemaName("name").format(SchemaDefinition.SchemaFormat.CSV)
            .schemaDefinition(Map.of()).version("1.0.0").build();
        assertThat(v2.isNewerThan(v1)).isTrue();
    }

    @Test
    void isNotNewerThanSame() {
        var s = SchemaDefinition.builder()
            .schemaId("id").schemaName("name").format(SchemaDefinition.SchemaFormat.AVRO)
            .schemaDefinition(Map.of()).version("1.0.0").build();
        assertThat(s.isNewerThan(s)).isFalse();
    }

    @Test
    void schemaFormatEnum() {
        assertThat(SchemaDefinition.SchemaFormat.values()).hasSize(5);
    }
}
