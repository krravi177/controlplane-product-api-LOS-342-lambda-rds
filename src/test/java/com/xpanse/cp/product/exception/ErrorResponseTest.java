package com.xpanse.cp.product.exception;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testNoArgsConstructor() {
        ErrorResponse errorResponse = new ErrorResponse();

        assertNotNull(errorResponse);
        assertNull(errorResponse.getErrorTime());
        assertNull(errorResponse.getErrorMessage());
        assertEquals(0, errorResponse.getStatusCode());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime errorTime = LocalDateTime.now();
        int statusCode = 404;
        String errorMessage = "Not Found";

        ErrorResponse errorResponse = new ErrorResponse(errorTime, statusCode, errorMessage);

        assertNotNull(errorResponse);
        assertEquals(errorTime, errorResponse.getErrorTime());
        assertEquals(statusCode, errorResponse.getStatusCode());
        assertEquals(errorMessage, errorResponse.getErrorMessage());
    }

    @Test
    void testTwoArgsConstructor() {
        LocalDateTime errorTime = LocalDateTime.now();
        String errorMessage = "Bad Request";

        ErrorResponse errorResponse = new ErrorResponse(errorTime, errorMessage);

        assertNotNull(errorResponse);
        assertEquals(errorTime, errorResponse.getErrorTime());
        assertEquals(errorMessage, errorResponse.getErrorMessage());
        assertEquals(0, errorResponse.getStatusCode());
    }

    @Test
    void testThreeArgsConstructor() {
        LocalDateTime errorTime = LocalDateTime.now();
        String errorMessage = "Server Error";
        int statusCode = 500;

        ErrorResponse errorResponse = new ErrorResponse(errorTime, errorMessage, statusCode);

        assertNotNull(errorResponse);
        assertEquals(errorTime, errorResponse.getErrorTime());
        assertEquals(errorMessage, errorResponse.getErrorMessage());
        assertEquals(statusCode, errorResponse.getStatusCode());
    }

    @Test
    void testSettersAndGetters() {
        ErrorResponse errorResponse = new ErrorResponse();
        LocalDateTime errorTime = LocalDateTime.now();
        int statusCode = 400;
        String errorMessage = "Bad Request";

        errorResponse.setErrorTime(errorTime);
        errorResponse.setStatusCode(statusCode);
        errorResponse.setErrorMessage(errorMessage);

        assertEquals(errorTime, errorResponse.getErrorTime());
        assertEquals(statusCode, errorResponse.getStatusCode());
        assertEquals(errorMessage, errorResponse.getErrorMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime errorTime = LocalDateTime.now();
        int statusCode = 404;
        String errorMessage = "Not Found";

        ErrorResponse errorResponse1 = new ErrorResponse(errorTime, statusCode, errorMessage);
        ErrorResponse errorResponse2 = new ErrorResponse(errorTime, statusCode, errorMessage);
        ErrorResponse errorResponse3 = new ErrorResponse(errorTime, 500, "Server Error");

        // Test equals
        assertTrue(errorResponse1.equals(errorResponse2));
        assertFalse(errorResponse1.equals(errorResponse3));
        assertFalse(errorResponse1.equals(null));
        assertTrue(errorResponse1.equals(errorResponse1));

        // Test hashCode
        assertEquals(errorResponse1.hashCode(), errorResponse2.hashCode());
        assertNotEquals(errorResponse1.hashCode(), errorResponse3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime errorTime = LocalDateTime.now();
        int statusCode = 404;
        String errorMessage = "Not Found";

        ErrorResponse errorResponse = new ErrorResponse(errorTime, statusCode, errorMessage);
        String toString = errorResponse.toString();

        // Verify toString contains all fields
        assertTrue(toString.contains(String.valueOf(errorTime)));
        assertTrue(toString.contains(String.valueOf(statusCode)));
        assertTrue(toString.contains(errorMessage));
    }

    @Test
    void testNullFields() {
        ErrorResponse errorResponse = new ErrorResponse(null, null);

        assertNull(errorResponse.getErrorTime());
        assertNull(errorResponse.getErrorMessage());
        assertEquals(0, errorResponse.getStatusCode());
    }
}