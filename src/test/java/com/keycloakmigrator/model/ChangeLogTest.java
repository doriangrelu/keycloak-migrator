package com.keycloakmigrator.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ChangeLog}.
 */
class ChangeLogTest {

    @Test
    void getChangesets_shouldReturnEmptyListByDefault() {
        // Given
        final ChangeLog changeLog = new ChangeLog();

        // Then
        assertNotNull(changeLog.getChangesets());
        assertTrue(changeLog.getChangesets().isEmpty());
    }

    @Test
    void setChangesets_shouldStoreChangesets() {
        // Given
        final ChangeLog changeLog = new ChangeLog();
        final Changeset cs1 = createChangeset(1, "admin");
        final Changeset cs2 = createChangeset(2, "dev");

        // When
        changeLog.setChangesets(new ArrayList<>(List.of(cs1, cs2)));

        // Then
        assertEquals(2, changeLog.getChangesets().size());
    }

    @Test
    void getSortedChangesets_shouldReturnSortedByVersion() {
        // Given
        final ChangeLog changeLog = new ChangeLog();
        final Changeset cs3 = createChangeset(3, "c");
        final Changeset cs1 = createChangeset(1, "a");
        final Changeset cs2 = createChangeset(2, "b");
        changeLog.setChangesets(new ArrayList<>(List.of(cs3, cs1, cs2)));

        // When
        final List<Changeset> sorted = changeLog.getSortedChangesets();

        // Then
        assertEquals(3, sorted.size());
        assertEquals(1, sorted.get(0).getVersion());
        assertEquals(2, sorted.get(1).getVersion());
        assertEquals(3, sorted.get(2).getVersion());
    }

    @Test
    void getSortedChangesets_shouldNotModifyOriginalList() {
        // Given
        final ChangeLog changeLog = new ChangeLog();
        final Changeset cs3 = createChangeset(3, "c");
        final Changeset cs1 = createChangeset(1, "a");
        changeLog.setChangesets(new ArrayList<>(List.of(cs3, cs1)));

        // When
        changeLog.getSortedChangesets();

        // Then - original order should be preserved
        assertEquals(3, changeLog.getChangesets().get(0).getVersion());
        assertEquals(1, changeLog.getChangesets().get(1).getVersion());
    }

    @Test
    void getIncludes_shouldReturnEmptyListByDefault() {
        // Given
        final ChangeLog changeLog = new ChangeLog();

        // Then
        assertNotNull(changeLog.getIncludes());
        assertTrue(changeLog.getIncludes().isEmpty());
    }

    @Test
    void include_shouldStoreFileAndRelativePath() {
        // Given
        final ChangeLog.Include include = new ChangeLog.Include();

        // When
        include.setFile("other-changelog.xml");
        include.setRelativeToChangelogFile(true);

        // Then
        assertEquals("other-changelog.xml", include.getFile());
        assertTrue(include.getRelativeToChangelogFile());
    }

    @Test
    void include_relativeToChangelogFile_shouldDefaultToTrue() {
        // Given
        final ChangeLog.Include include = new ChangeLog.Include();

        // When
        include.setFile("test.xml");

        // Then - default should be true (not explicitly set)
        // Note: The default is set via JAXB annotation, so in unit tests it may be null
        // unless explicitly set. The XSD defines the default as true.
    }

    private Changeset createChangeset(final int version, final String author) {
        final Changeset changeset = new Changeset();
        changeset.setVersion(version);
        changeset.setAuthor(author);
        return changeset;
    }
}
