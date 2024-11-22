package com.xpanse.cp.product.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateProductExceptionTest {

    @Test
    void constructor_WithMessage_SetsMessage() {
        // Arrange
        String errorMessage = "Product with ID 'TEST-001' already exists";

        // Act
        DuplicateProductException exception = new DuplicateProductException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Duplicate product found with ID: PRD-001",
            "Product already exists: TEST-123",
            "Duplicate entry: SKU-999",
            ""  // Empty string case
    })
    void constructor_WithVariousMessages_SetsCorrectly(String message) {
        // Act
        DuplicateProductException exception = new DuplicateProductException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void serialization_PreservesExceptionState() throws IOException, ClassNotFoundException {
        // Arrange
        String originalMessage = "Original duplicate product message";
        DuplicateProductException originalException = new DuplicateProductException(originalMessage);

        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(originalException);
        }

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DuplicateProductException deserializedException;
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            deserializedException = (DuplicateProductException) ois.readObject();
        }

        // Assert
        assertEquals(originalMessage, deserializedException.getMessage());
        assertEquals(originalException.getClass(), deserializedException.getClass());
    }

    @Test
    void constructor_WithNullMessage_AcceptsNull() {
        // Act
        DuplicateProductException exception = new DuplicateProductException(null);

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    void catchException_AsRuntimeException_Succeeds() {
        // Arrange
        String errorMessage = "Test duplicate product";
        RuntimeException caughtException = null;

        // Act
        try {
            throw new DuplicateProductException(errorMessage);
        } catch (RuntimeException ex) {
            caughtException = ex;
        }

        // Assert
        assertNotNull(caughtException);
        assertTrue(caughtException instanceof DuplicateProductException);
        assertEquals(errorMessage, caughtException.getMessage());
    }

    @Test
    void stackTrace_ContainsExpectedInformation() {
        // Arrange & Act
        DuplicateProductException exception = new DuplicateProductException("Test message");
        StackTraceElement[] stackTrace = exception.getStackTrace();

        // Assert
        assertTrue(stackTrace.length > 0);
        assertEquals(getClass().getName(), stackTrace[0].getClassName());
    }

    @Test
    void throwException_VerifyBehavior() {
        // Arrange
        String errorMessage = "Test duplicate product";

        // Act & Assert
        DuplicateProductException exception = assertThrows(DuplicateProductException.class, () -> {
            throw new DuplicateProductException(errorMessage);
        });
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void toString_ContainsClassAndMessage() {
        // Arrange
        String errorMessage = "Test duplicate product";
        DuplicateProductException exception = new DuplicateProductException(errorMessage);

        // Act
        String result = exception.toString();

        // Assert
        assertTrue(result.contains(DuplicateProductException.class.getName()));
        assertTrue(result.contains(errorMessage));
    }

    @Test
    void exceptionChaining_HasNullCauseByDefault() {
        // Act
        DuplicateProductException exception = new DuplicateProductException("Test message");

        // Assert
        assertNull(exception.getCause());
    }

    @Test
    void multipleInstances_WorkIndependently() {
        // Arrange
        String message1 = "First duplicate";
        String message2 = "Second duplicate";

        // Act
        DuplicateProductException exception1 = new DuplicateProductException(message1);
        DuplicateProductException exception2 = new DuplicateProductException(message2);

        // Assert
        assertNotEquals(exception1.getMessage(), exception2.getMessage());
        assertEquals(message1, exception1.getMessage());
        assertEquals(message2, exception2.getMessage());
    }
}