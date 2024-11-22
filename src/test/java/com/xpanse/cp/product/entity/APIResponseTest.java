package com.xpanse.cp.product.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class APIResponseTest {

    @Test
    void testNoArgsConstructor() {
        // Act
        APIResponse response = new APIResponse();

        // Assert
        assertNotNull(response);
        assertNull(response.getMessage());
        assertNull(response.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        APIResponse response = new APIResponse();
        String message = "Operation successful";
        String status = "SUCCESS";

        // Act
        response.setMessage(message);
        response.setStatus(status);

        // Assert
        assertEquals(message, response.getMessage());
        assertEquals(status, response.getStatus());
    }

    @Test
    void testToString() {
        // Arrange
        APIResponse response = new APIResponse();
        response.setMessage("Test message");
        response.setStatus("SUCCESS");

        // Act
        String toString = response.toString();

        // Assert
        assertTrue(toString.contains("message=Test message"));
        assertTrue(toString.contains("status=SUCCESS"));
    }

    @Test
    void testEqualsWithNull() {
        // Arrange
        APIResponse response = new APIResponse();
        response.setMessage("Test message");
        response.setStatus("SUCCESS");

        // Assert
        assertNotEquals(null, response);
    }

    @Test
    void testEqualsWithDifferentClass() {
        // Arrange
        APIResponse response = new APIResponse();
        response.setMessage("Test message");
        response.setStatus("SUCCESS");
        Object differentClass = new Object();

        // Assert
        assertNotEquals(differentClass, response);
    }

    @Test
    void testEqualsWithNullFields() {
        // Arrange
        APIResponse response1 = new APIResponse();
        APIResponse response2 = new APIResponse();

        // Assert
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testEqualsWithMixedNullFields() {
        // Arrange
        APIResponse response1 = new APIResponse();
        response1.setMessage("Test message");

        APIResponse response2 = new APIResponse();
        response2.setMessage("Test message");

        // Assert
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testSetNullValues() {
        // Arrange
        APIResponse response = new APIResponse();
        response.setMessage("Test message");
        response.setStatus("SUCCESS");

        // Act
        response.setMessage(null);
        response.setStatus(null);

        // Assert
        assertNull(response.getMessage());
        assertNull(response.getStatus());
    }

    @Test
    void testChainedSetters() {
        // Arrange & Act
        APIResponse response = new APIResponse();
        response.setMessage("Test message");
        response.setStatus("Success");

        // Assert
        assertEquals("Test message", response.getMessage());
        assertEquals("Success", response.getStatus());
    }
}