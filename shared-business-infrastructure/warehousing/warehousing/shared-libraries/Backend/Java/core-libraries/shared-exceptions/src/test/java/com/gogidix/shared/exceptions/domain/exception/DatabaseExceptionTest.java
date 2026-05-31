package com.gogidix.shared.exceptions.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for DatabaseException
 */
@DisplayName("DatabaseException Tests")
class DatabaseExceptionTest {

    @Test
    @DisplayName("Test constructor with message only")
    void testConstructorWithMessage() {
        DatabaseException exception = new DatabaseException("Database error");

        assertEquals("Database error", exception.getMessage());
        assertEquals("DATABASE_ERROR", exception.getErrorCode());
        assertEquals(500, exception.getHttpStatus());
        assertEquals("DATABASE", exception.getCategory());
    }

    @Test
    @DisplayName("Test constructor with message and error code")
    void testConstructorWithMessageAndErrorCode() {
        DatabaseException exception = new DatabaseException(
            "Connection failed"
        );

        assertEquals("Connection failed", exception.getMessage());
        assertEquals("DATABASE_ERROR", exception.getErrorCode());
        assertEquals(500, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Test constructor with message and cause")
    void testConstructorWithMessageAndCause() {
        SQLException cause = new SQLException("Invalid SQL syntax");
        DatabaseException exception = new DatabaseException(
            "Query failed",
            cause
        );

        assertEquals("Query failed", exception.getMessage());
        assertEquals("DATABASE_ERROR", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Test is critical")
    void testIsCritical() {
        DatabaseException exception = new DatabaseException("DB error");
        assertTrue(exception.isCritical());
    }

    @Test
    @DisplayName("Test is not retryable")
    void testIsNotRetryable() {
        DatabaseException exception = new DatabaseException("DB error");
        assertFalse(exception.isRetryable());
    }

    @Test
    @DisplayName("Test can add database-specific context")
    void testCanAddDatabaseContext() {
        DatabaseException exception = new DatabaseException("Query failed");
        exception.addContext("table", "users");
        exception.addContext("query", "SELECT * FROM users");
        exception.addContext("executionTimeMs", 1500);

        assertEquals(3, exception.getContext().size());
    }

    @Test
    @DisplayName("Test can be thrown and caught")
    void testCanBeThrownAndCaught() {
        DatabaseException caught = assertThrows(DatabaseException.class, () -> {
            throw new DatabaseException("Connection pool exhausted");
        });

        assertEquals("Connection pool exhausted", caught.getMessage());
    }

    @Test
    @DisplayName("Test inherits from TechnicalException")
    void testInheritsFromTechnicalException() {
        DatabaseException exception = new DatabaseException("DB error");
        assertTrue(exception instanceof TechnicalException);
        assertTrue(exception instanceof BaseException);
    }

    @Test
    @DisplayName("Test with SQL exception as cause")
    void testWithSQLExceptionCause() {
        SQLException sqlEx = new SQLException("ORA-12154: TNS:could not resolve the connect identifier");
        DatabaseException exception = new DatabaseException("Database unavailable", sqlEx);

        assertEquals(sqlEx, exception.getCause());
        assertTrue(exception.getMessage().contains("Database unavailable"));
    }
}
