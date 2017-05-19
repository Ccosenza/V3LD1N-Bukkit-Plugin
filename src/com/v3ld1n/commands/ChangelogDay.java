package com.v3ld1n.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.TimeUtil;

public class ChangelogDay {
    private String day;
    private List<Change> changes;
    
    public ChangelogDay(String day, List<Change> changes) {
        this.day = day;
        this.changes = changes;
    }

    public String getDate() {
        return day;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public void addChange(Change change) {
        changes.add(change);
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("MM-dd-yyyy");
    }

    public static ChangelogDay today() {
        List<ChangelogDay> clds = V3LD1N.getChangelogDays();
        ChangelogDay compare = new ChangelogDay(todayDate(), new ArrayList<Change>());
        ChangelogDay today = null;
        if (clds.contains(compare)) {
            today = clds.get(clds.indexOf(compare));
        }
        return today;
    }

    public static String todayDate() {
        return getDateFormat().format(TimeUtil.getTime());
    }

    public static ChangelogDay findDay(String day) {
        List<ChangelogDay> clds = V3LD1N.getChangelogDays();
        ChangelogDay compare = new ChangelogDay(day, new ArrayList<Change>());
        ChangelogDay cld = null;
        if (clds.contains(compare)) {
            cld = clds.get(clds.indexOf(compare));
        }
        return cld;
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