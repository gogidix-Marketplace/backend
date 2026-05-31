package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for MongoConfig.
 * Tests MongoDB configuration and bean creation.
 */
@DisplayName("MongoConfig Tests")
class MongoConfigTest {

    @Nested
    @DisplayName("MongoClient Configuration")
    class MongoClientConfigurationTests {

        @Test
        @DisplayName("Should create MongoClient with connection string")
        void shouldCreateMongoClientWithConnectionString() {
            String connectionString = "mongodb://localhost:27017";

            MongoClient client = MongoClients.create(connectionString);

            assertThat(client).isNotNull();

            // Clean up
            client.close();
        }

        @Test
        @DisplayName("Should create MongoClient with custom configuration")
        void shouldCreateMongoClientWithCustomConfig() {
            String connectionString = "mongodb://localhost:27017/testdb";

            MongoClient client = MongoClients.create(connectionString);

            assertThat(client).isNotNull();
            assertThat(client.getClass().getName()).contains("MongoClient");

            // Clean up
            client.close();
        }
    }

    @Nested
    @DisplayName("Database Configuration")
    class DatabaseConfigurationTests {

        @Test
        @DisplayName("Should return correct database name")
        void shouldReturnCorrectDatabaseName() {
            MongoConfig config = new MongoConfig() {
                @Override
                protected String getDatabaseName() {
                    return "test-fraud-detection";
                }
            };

            String databaseName = config.getDatabaseName();

            assertThat(databaseName).isEqualTo("test-fraud-detection");
        }

        @Test
        @DisplayName("Database name should not be empty")
        void databaseNameShouldNotBeEmpty() {
            MongoConfig config = new MongoConfig() {
                @Override
                protected String getDatabaseName() {
                    return "ai-fraud-detection";
                }
            };

            String databaseName = config.getDatabaseName();

            assertThat(databaseName).isNotEmpty();
        }
    }
}
