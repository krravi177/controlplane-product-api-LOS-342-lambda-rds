package com.xpanse.cp.product.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class InvalidRequestExceptionTest {

    @Test
    void constructor_WithMessage_SetsMessage() {
        // Arrange
        String errorMessage = "Invalid request: Missing required field";

        // Act
        InvalidRequestException exception = new InvalidRequestException(errorMessage);

        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void constructor_WithNullMessage_AcceptsNull() {
        // Act
        InvalidRequestException exception = new InvalidRequestException(null);

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    void inheritance_IsRuntimeException() {
        // Act
        InvalidRequestException exception = new InvalidRequestException("test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Missing required field: name",
            "Invalid email format",
            "Field length exceeds maximum",
            ""  // Empty string case
    })
    void constructor_WithDifferentMessages_SetsCorrectly(String message) {
        // Act
        InvalidRequestException exception = new InvalidRequestException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void serialization_MaintainsExceptionState() throws IOException, ClassNotFoundException {
        // Arrange
        String originalMessage = "Test serialization message";
        InvalidRequestException originalException = new InvalidRequestException(originalMessage);

        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(originalException);

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        InvalidRequestException deserializedException = (InvalidRequestException) ois.readObject();

        // Assert
        assertEquals(originalMessage, deserializedException.getMessage());
        assertEquals(originalException.getClass(), deserializedException.getClass());
    }

    @Test
    void exceptionHandling_CanBeCaughtAsRuntimeException() {
        // Arrange
        String errorMessage = "Test error message";
        RuntimeException caughtException = null;

        // Act
        try {
            throw new InvalidRequestException(errorMessage);
        } catch (RuntimeException ex) {
            caughtException = ex;
        }

        // Assert
        assertNotNull(caughtException);
        assertTrue(caughtException instanceof InvalidRequestException);
        assertEquals(errorMessage, caughtException.getMessage());
    }

    @Test
    void stackTrace_ContainsCorrectInformation() {
        // Arrange
        InvalidRequestException exception = new InvalidRequestException("Test message");

        // Act
        StackTraceElement[] stackTrace = exception.getStackTrace();

        // Assert
        assertTrue(stackTrace.length > 0);
        assertEquals(getClass().getName(), stackTrace[0].getClassName());
    }

    @Test
    void throwException_VerifyExceptionBehavior() {
        // Arrange
        String errorMessage = "Test error message";

        // Act & Assert
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            throw new InvalidRequestException(errorMessage);
        });
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void cause_IsNullByDefault() {
        // Act
        InvalidRequestException exception = new InvalidRequestException("Test message");

        // Assert
        assertNull(exception.getCause());
    }

    @Test
    void toString_ContainsClassNameAndMessage() {
        // Arrange
        String errorMessage = "Test error message";
        InvalidRequestException exception = new InvalidRequestException(errorMessage);

        // Act
        String toString = exception.toString();

        // Assert
        assertTrue(toString.contains(InvalidRequestException.class.getName()));
        assertTrue(toString.contains(errorMessage));
    }
}