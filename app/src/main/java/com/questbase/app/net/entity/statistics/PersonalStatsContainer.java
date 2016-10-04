package com.questbase.app.net.entity.statistics;

import com.questbase.app.utils.Objects;

public class PersonalStatsContainer {
    public PersonalStatsDto stats;

    public PersonalStatsContainer(PersonalStatsDto stats) {
        this.stats = stats;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stats);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof PersonalStatsContainer)) {
            return false;
        }
        PersonalStatsContainer personalStatsContainer = (PersonalStatsContainer) object;
        return Objects.equal(stats, personalStatsContainer.stats);
    }
}