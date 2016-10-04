package com.questbase.app.net.entity.statistics;

import com.questbase.app.utils.Objects;

public class PerFormPersonalStatsDto {

    public float average;
    public float mine;

    public PerFormPersonalStatsDto(float average, float mine) {
        this.average = average;
        this.mine = mine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(average, mine);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof PerFormPersonalStatsDto)) {
            return false;
        }
        PerFormPersonalStatsDto perFormPersonalStatsDto = (PerFormPersonalStatsDto) object;
        return Objects.equal(average, perFormPersonalStatsDto.average)
                && Objects.equal(mine, perFormPersonalStatsDto.mine);
    }
}