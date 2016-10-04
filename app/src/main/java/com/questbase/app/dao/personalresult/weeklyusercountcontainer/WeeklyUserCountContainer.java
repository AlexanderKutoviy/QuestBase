package com.questbase.app.dao.personalresult.weeklyusercountcontainer;

import com.questbase.app.utils.Objects;

import java.util.List;

public class WeeklyUserCountContainer {

    public String before;
    public List<String> days;
    public List<String> cnt;

    public WeeklyUserCountContainer(List<String> days, List<String> cnt, String before) {
        this.days = days;
        this.cnt = cnt;
        this.before = before;
    }

    @Override
    public int hashCode() {
        return Objects.hash(days, cnt, before);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof WeeklyUserCountContainer)) {
            return false;
        }
        WeeklyUserCountContainer weeklyUserCountContainer = (WeeklyUserCountContainer) object;
        return Objects.equal(days, weeklyUserCountContainer.days)
                && Objects.equal(cnt, weeklyUserCountContainer.cnt)
                && Objects.equal(before, weeklyUserCountContainer.before);
    }
}