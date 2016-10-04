package com.questbase.app.dao.personalresult.personalstats;

import com.annimon.stream.Optional;
import com.questbase.app.net.entity.statistics.PerFormPersonalStatsDto;
import com.questbase.app.net.entity.statistics.PersonalStatsDto;

public interface PerFormPersonalStatsDao {

    void setStats(String type, PerFormPersonalStatsDto perFormPersonalStatsDto);

    Optional<PersonalStatsDto> getStats();
}