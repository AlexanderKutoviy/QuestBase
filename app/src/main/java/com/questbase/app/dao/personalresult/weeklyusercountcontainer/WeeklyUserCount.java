package com.questbase.app.dao.personalresult.weeklyusercountcontainer;

import com.questbase.app.utils.Objects;

public class WeeklyUserCount {
    public String day;
    public String cnt;

    public WeeklyUserCount(String days, String cnt) {
        this.day = days;
        this.cnt = cnt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, cnt);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof WeeklyUserCount)) {
            return false;
        }
        WeeklyUserCount weeks = (WeeklyUserCount) object;
        return Objects.equal(day, weeks.day)
                && Objects.equal(cnt, weeks.cnt);
    }
}