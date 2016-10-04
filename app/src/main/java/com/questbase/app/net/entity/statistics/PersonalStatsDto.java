package com.questbase.app.net.entity.statistics;

import com.questbase.app.utils.Objects;

public class PersonalStatsDto {

    public PerFormPersonalStatsDto popular;
    public PerFormPersonalStatsDto fillRate;
    public PerFormPersonalStatsDto formsPerUser;

    public PersonalStatsDto(PerFormPersonalStatsDto popular, PerFormPersonalStatsDto fillRate, PerFormPersonalStatsDto formsPerUser) {
        this.popular = popular;
        this.fillRate = fillRate;
        this.formsPerUser = formsPerUser;
    }

    @Override
    public int hashCode() {
        return Objects.hash(popular, fillRate, formsPerUser);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof PersonalStatsDto)) {
            return false;
        }
        PersonalStatsDto personalStatsDto = (PersonalStatsDto) object;
        return Objects.equal(popular, personalStatsDto.popular)
                && Objects.equal(fillRate, personalStatsDto.fillRate)
                && Objects.equal(formsPerUser, personalStatsDto.formsPerUser);
    }
}