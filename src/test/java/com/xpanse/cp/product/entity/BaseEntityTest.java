package com.xpanse.cp.product.entity;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import static org.junit.jupiter.api.Assertions.*;

class BaseEntityTest {

    // Concrete implementation for testing abstract class
    private static class TestEntity extends BaseEntity {
        // Empty implementation for testing
    }

    @Test
    void testMappedSuperclassAnnotation() {
        // Assert
        assertTrue(BaseEntity.class.isAnnotationPresent(MappedSuperclass.class));
    }

    @Test
    void testGetterSetterCreatedDate() {
        // Arrange
        TestEntity entity = new TestEntity();
        LocalDateTime now = LocalDateTime.now();

        // Act
        entity.setCreatedDate(now);

        // Assert
        assertEquals(now, entity.getCreatedDate());
    }

    @Test
    void testGetterSetterUpdatedDate() {
        // Arrange
        TestEntity entity = new TestEntity();
        LocalDateTime now = LocalDateTime.now();

        // Act
        entity.setUpdatedDate(now);

        // Assert
        assertEquals(now, entity.getUpdatedDate());
    }

    @Test
    void testPrePersistMethod() {
        // Arrange
        TestEntity entity = new TestEntity();

        // Act
        entity.prePersist();
        LocalDateTime currentTime = LocalDateTime.now();

        // Assert
        assertNotNull(entity.getCreatedDate());
        // Verify the created date is within 1 second of current time
        assertTrue(ChronoUnit.SECONDS.between(entity.getCreatedDate(), currentTime) < 1);
        assertNull(entity.getUpdatedDate());
    }

    @Test
    void testPreUpdateMethod() {
        // Arrange
        TestEntity entity = new TestEntity();

        // Act
        entity.preUpdate();
        LocalDateTime currentTime = LocalDateTime.now();

        // Assert
        assertNotNull(entity.getUpdatedDate());
        // Verify the updated date is within 1 second of current time
        assertTrue(ChronoUnit.SECONDS.between(entity.getUpdatedDate(), currentTime) < 1);
    }

    @Test
    void testPrePersistAnnotation() throws NoSuchMethodException {
        // Assert
        assertTrue(BaseEntity.class.getMethod("prePersist").isAnnotationPresent(PrePersist.class));
    }

    @Test
    void testPreUpdateAnnotation() throws NoSuchMethodException {
        // Assert
        assertTrue(BaseEntity.class.getMethod("preUpdate").isAnnotationPresent(PreUpdate.class));
    }

    @Test
    void testInitialState() {
        // Arrange & Act
        TestEntity entity = new TestEntity();

        // Assert
        assertNull(entity.getCreatedDate());
        assertNull(entity.getUpdatedDate());
    }

    @Test
    void testUpdateSequence() {
        // Arrange
        TestEntity entity = new TestEntity();

        // Act
        entity.prePersist(); // Simulating entity creation
        LocalDateTime createdDate = entity.getCreatedDate();

        // Simulate some delay
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        entity.preUpdate(); // Simulating entity update

        // Assert
        assertNotNull(createdDate);
        assertNotNull(entity.getUpdatedDate());
        assertTrue(entity.getUpdatedDate().isAfter(createdDate));
    }

    @Test
    void testMultipleUpdates() {
        // Arrange
        TestEntity entity = new TestEntity();
        entity.preUpdate();
        LocalDateTime firstUpdate = entity.getUpdatedDate();

        // Simulate some delay
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act
        entity.preUpdate();
        LocalDateTime secondUpdate = entity.getUpdatedDate();

        // Assert
        assertNotNull(firstUpdate);
        assertNotNull(secondUpdate);
        assertTrue(secondUpdate.isAfter(firstUpdate));
    }

    @Test
    void testAbstractClass() {
        // Assert
        assertTrue(java.lang.reflect.Modifier.isAbstract(BaseEntity.class.getModifiers()));
    }

}