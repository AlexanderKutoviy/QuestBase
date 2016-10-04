package com.questbase.app.dao.personalresult.personalstats;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.questbase.app.db.RespoBaseModel;
import com.questbase.app.db.RespoDatabase;
import com.questbase.app.net.entity.statistics.PerFormPersonalStatsDto;

@Table(database = RespoDatabase.class)
public class PerFormPersonalStatsModel extends RespoBaseModel {

    @PrimaryKey
    @Column
    public String chartType;

    @Column
    public float average;

    @Column
    public float mine;

    public PerFormPersonalStatsModel() {
    }

    public PerFormPersonalStatsModel(String type, PerFormPersonalStatsDto perFormPersonalStatsDto) {
        chartType = type;
        average = perFormPersonalStatsDto.average;
        mine = perFormPersonalStatsDto.mine;
    }

    public PerFormPersonalStatsDto toPerFormPersonalStatsDto() {
        return new PerFormPersonalStatsDto(average, mine);
    }
}