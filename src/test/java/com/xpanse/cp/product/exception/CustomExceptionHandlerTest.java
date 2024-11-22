package com.xpanse.cp.product.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomExceptionHandlerTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private CustomExceptionHandler exceptionHandler;

    private LocalDateTime beforeTest;
    private MethodArgumentNotValidException methodArgumentNotValidException;
    private HttpMessageNotReadableException httpMessageNotReadableException;

    @BeforeEach
    void setUp() {

        beforeTest = LocalDateTime.now();
    }

    @Test
    void handleResourceNotFoundExceptionTest() {
        // Arrange
        String errorMessage = "Product not found with id: 123";
        ProductNotFoundException exception = new ProductNotFoundException(errorMessage);

        // Act
        ResponseEntity<?> responseEntity = exceptionHandler.handleResourceNotFoundException(exception);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getErrorMessage());
        //verify(logger).error(eq("ResourceNotFoundException Exception occurred: {}"), eq(errorMessage));
    }

    @Test
    void handleInvalidRequestExceptionTest() {
        // Arrange
        String errorMessage = "Invalid request: Missing required field";
        InvalidRequestException exception = new InvalidRequestException(errorMessage);

        // Act
        ResponseEntity<?> responseEntity = exceptionHandler.handleInvalidRequestException(exception);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getErrorMessage());
        //verify(logger).error(eq("InvalidRequestException Exception occurred: | {} "), eq(errorMessage));
    }

    @Test
    void handleEnumInvalidRequestExceptionTest() {
        // Arrange
        String errorMessage = "Exception occurred while validating enum value";
        InvalidRequestException exception = new InvalidRequestException(errorMessage);

        // Act
        ResponseEntity<?> responseEntity = exceptionHandler.handleInvalidRequestException(exception);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getErrorMessage());
        //verify(logger).error(eq("InvalidRequestException Exception occurred: | {} "), eq(errorMessage));
    }

    @Test
    void handleDuplicateProductExceptionTest() {
        // Arrange
        String errorMessage = "Product already exists with identifier: TEST-001";
        DuplicateProductException exception = new DuplicateProductException(errorMessage);

        // Act
        ResponseEntity<?> responseEntity = exceptionHandler.handleDuplicateProductException(exception);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(errorResponse);
        assertEquals(errorMessage, errorResponse.getErrorMessage());
    }

    @Test
    void handleValidationExceptionsTest() throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod("handleValidationExceptionsTest");
        MethodParameter parameter = new MethodParameter(method, -1);

        // Act
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "Validation failed message");
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        methodArgumentNotValidException = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<?> responseEntity = exceptionHandler.handleValidationExceptions(methodArgumentNotValidException);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();

        // Assert
        assertNotNull(responseEntity);
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void handleEnumExceptionsTest() {
        httpMessageNotReadableException = new HttpMessageNotReadableException("Enum exception");

        // Act
        ResponseEntity<?> responseEntity = exceptionHandler.handleEnumValidationExceptions(httpMessageNotReadableException);
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();

        // Assert
        assertNotNull(responseEntity);
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    void constructor_CreatesInstance() {
        // Act
        CustomExceptionHandler handler = new CustomExceptionHandler();

        // Assert
        assertNotNull(handler);
    }

    @Test
    void verifyAnnotations() {
        // Assert
        assertTrue(CustomExceptionHandler.class.isAnnotationPresent(RestControllerAdvice.class));

        try {
            assertTrue(CustomExceptionHandler.class
                    .getMethod("handleResourceNotFoundException", ProductNotFoundException.class)
                    .isAnnotationPresent(ExceptionHandler.class));

            assertTrue(CustomExceptionHandler.class
                    .getMethod("handleInvalidRequestException", InvalidRequestException.class)
                    .isAnnotationPresent(ExceptionHandler.class));

            assertTrue(CustomExceptionHandler.class
                    .getMethod("handleDuplicateProductException", DuplicateProductException.class)
                    .isAnnotationPresent(ExceptionHandler.class));

        } catch (NoSuchMethodException e) {
            fail("Required exception handler methods not found");
        }
    }
}