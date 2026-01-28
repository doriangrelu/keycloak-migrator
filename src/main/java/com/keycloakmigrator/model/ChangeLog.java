package com.keycloakmigrator.model;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Root element representing a changelog file containing multiple changesets.
 */
@XmlRootElement(name = "changelog", namespace = "http://keycloak-migrator.com/changelog")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangeLog {

    @XmlElement(name = "changeset")
    private List<Changeset> changesets = new ArrayList<>();

    @XmlElement(name = "include")
    private List<Include> includes = new ArrayList<>();

    public List<Changeset> getChangesets() {
        return changesets;
    }

    public void setChangesets(List<Changeset> changesets) {
        this.changesets = changesets;
    }

    public List<Include> getIncludes() {
        return includes;
    }

    public void setIncludes(List<Include> includes) {
        this.includes = includes;
    }

    /**
     * Get all changesets sorted by version number.
     */
    public List<Changeset> getSortedChangesets() {
        return changesets.stream()
                .sorted((a, b) -> Integer.compare(a.getVersion(), b.getVersion()))
                .toList();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Include {
        @XmlAttribute(required = true)
        private String file;

        @XmlAttribute
        private Boolean relativeToChangelogFile = true;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public Boolean getRelativeToChangelogFile() {
            return relativeToChangelogFile;
        }

        public void setRelativeToChangelogFile(Boolean relativeToChangelogFile) {
            this.relativeToChangelogFile = relativeToChangelogFile;
        }
    }
}
