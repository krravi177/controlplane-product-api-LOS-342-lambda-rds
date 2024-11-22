package com.xpanse.cp.product.exception;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    private static final String ERROR_MESSAGE = "Product not found with ID: 123";

    @Test
    void constructor_WithMessage_SetsMessage() {
        // Act
        ProductNotFoundException exception = new ProductNotFoundException(ERROR_MESSAGE);

        // Assert
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void exceptionInheritance_IsRuntimeException() {
        // Act
        ProductNotFoundException exception = new ProductNotFoundException(ERROR_MESSAGE);

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void serialization_SerializeAndDeserialize_MaintainsState() throws IOException, ClassNotFoundException {
        // Arrange
        ProductNotFoundException originalException = new ProductNotFoundException(ERROR_MESSAGE);

        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(originalException);
        oos.close();

        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ProductNotFoundException deserializedException = (ProductNotFoundException) ois.readObject();
        ois.close();

        // Assert
        assertEquals(originalException.getMessage(), deserializedException.getMessage());
        assertEquals(originalException.getClass(), deserializedException.getClass());
    }

    @Test
    void constructor_WithNullMessage_AcceptsNull() {
        // Act
        ProductNotFoundException exception = new ProductNotFoundException(null);

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    void constructor_WithEmptyMessage_AcceptsEmptyString() {
        // Arrange
        String emptyMessage = "";

        // Act
        ProductNotFoundException exception = new ProductNotFoundException(emptyMessage);

        // Assert
        assertEquals(emptyMessage, exception.getMessage());
    }

    @Test
    void stackTrace_ContainsClassName() {
        // Act
        ProductNotFoundException exception = new ProductNotFoundException(ERROR_MESSAGE);

        // Assert
        assertTrue(exception.getStackTrace()[0].getClassName()
                .contains("ProductNotFoundExceptionTest"));
    }

    @Test
    void throwException_CatchAsRuntimeException_CanBeCaught() {
        // Arrange
        RuntimeException caughtException = null;

        // Act
        try {
            throw new ProductNotFoundException(ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            caughtException = ex;
        }

        // Assert
        assertNotNull(caughtException);
        assertTrue(caughtException instanceof ProductNotFoundException);
        assertEquals(ERROR_MESSAGE, caughtException.getMessage());
    }


    @Test
    void throwException_VerifyExceptionBehavior() {
        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            throw new ProductNotFoundException(ERROR_MESSAGE);
        });
    }
}