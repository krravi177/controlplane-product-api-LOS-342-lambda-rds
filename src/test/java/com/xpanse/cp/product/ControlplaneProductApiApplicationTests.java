package com.xpanse.cp.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Controlplane Product API Application Tests")
class ControlplaneProductApiApplicationTests {

    /*@Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Application Context Should Load Successfully")
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Nested
    @DisplayName("Application Configuration Tests")
    class ApplicationConfigurationTests {

        @Test
        @DisplayName("Application Class Should Have SpringBootApplication Annotation")
        void shouldHaveSpringBootApplicationAnnotation() {
            assertTrue(ControlplaneProductApiApplication.class.isAnnotationPresent(SpringBootApplication.class),
                    "Main class should be annotated with @SpringBootApplication");
        }

        @Test
        @DisplayName("Main Method Should Execute Without Exceptions")
        void mainMethodShouldExecuteWithoutException() {
            assertDoesNotThrow(() ->
                            ControlplaneProductApiApplication.main(new String[]{}),
                    "Main method should execute without throwing exceptions"
            );
        }
    }

    @Nested
    @DisplayName("Application Environment Tests")
    class ApplicationEnvironmentTests {

        @Test
        @DisplayName("Active Profile Should Be Test")
        void shouldHaveTestProfile() {
            assertArrayEquals(
                    new String[]{"test"},
                    applicationContext.getEnvironment().getActiveProfiles(),
                    "Active profile should be 'test'"
            );
        }
    }*/
}