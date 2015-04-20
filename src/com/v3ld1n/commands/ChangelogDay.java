package com.v3ld1n.commands;

import java.util.List;

public class ChangelogDay {
    private String day;
    private List<Change> changes;
    
    public ChangelogDay(String day, List<Change> changes) {
        this.day = day;
        this.changes = changes;
    }

    public String getDay() {
        return day;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public void addChange(Change change) {
        changes.add(change);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((day == null) ? 0 : day.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChangelogDay other = (ChangelogDay) obj;
        if (day == null) {
            if (other.day != null)
                return false;
        } else if (!day.equals(other.day))
            return false;
        return true;
    }
}