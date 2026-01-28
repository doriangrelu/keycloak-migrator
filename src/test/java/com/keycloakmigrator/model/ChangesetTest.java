package com.keycloakmigrator.model;

import com.keycloakmigrator.model.operations.CreateRealmOperation;
import com.keycloakmigrator.model.operations.Operation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Changeset}.
 */
class ChangesetTest {

    @Test
    void getId_shouldReturnVersionAndAuthor() {
        // Given
        final Changeset changeset = new Changeset();
        changeset.setVersion(5);
        changeset.setAuthor("john");

        // When
        final String id = changeset.getId();

        // Then
        assertEquals("5:john", id);
    }

    @Test
    void getFailOnError_shouldDefaultToTrue() {
        // Given
        final Changeset changeset = new Changeset();

        // Then
        assertTrue(changeset.getFailOnError());
    }

    @Test
    void setFailOnError_shouldOverrideDefault() {
        // Given
        final Changeset changeset = new Changeset();

        // When
        changeset.setFailOnError(false);

        // Then
        assertFalse(changeset.getFailOnError());
    }

    @Test
    void getOperations_shouldReturnEmptyListByDefault() {
        // Given
        final Changeset changeset = new Changeset();

        // Then
        assertNotNull(changeset.getOperations());
        assertTrue(changeset.getOperations().isEmpty());
    }

    @Test
    void setOperations_shouldStoreOperations() {
        // Given
        final Changeset changeset = new Changeset();
        final CreateRealmOperation op = new CreateRealmOperation();
        op.setName("test-realm");

        // When
        changeset.setOperations(List.of(op));

        // Then
        assertEquals(1, changeset.getOperations().size());
        assertSame(op, changeset.getOperations().get(0));
    }

    @Test
    void toString_shouldContainVersionAndAuthor() {
        // Given
        final Changeset changeset = new Changeset();
        changeset.setVersion(10);
        changeset.setAuthor("admin");
        changeset.setOperations(List.of(new CreateRealmOperation()));

        // When
        final String result = changeset.toString();

        // Then
        assertTrue(result.contains("version=10"));
        assertTrue(result.contains("author='admin'"));
        assertTrue(result.contains("operations=1"));
    }

    @Test
    void setComment_shouldStoreComment() {
        // Given
        final Changeset changeset = new Changeset();

        // When
        changeset.setComment("This is a test comment");

        // Then
        assertEquals("This is a test comment", changeset.getComment());
    }

    @Test
    void setContext_shouldStoreContext() {
        // Given
        final Changeset changeset = new Changeset();

        // When
        changeset.setContext("production");

        // Then
        assertEquals("production", changeset.getContext());
    }
}
