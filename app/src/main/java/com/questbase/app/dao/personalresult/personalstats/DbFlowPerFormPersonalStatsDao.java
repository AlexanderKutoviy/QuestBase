package com.questbase.app.dao.personalresult.personalstats;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.net.entity.statistics.PerFormPersonalStatsDto;
import com.questbase.app.net.entity.statistics.PersonalStatsDto;

import java.util.Arrays;
import java.util.List;

public class DbFlowPerFormPersonalStatsDao implements PerFormPersonalStatsDao {

    private final String POPULAR = "popular";
    private final String FILL_RATE = "fillRate";
    private final String FORMS_PER_USER = "formsPerUser";

    @Override
    public void setStats(String type, PerFormPersonalStatsDto perFormPersonalStatsDto) {
        new PerFormPersonalStatsModel(type, perFormPersonalStatsDto).saveOnDuplicateUpdate();
    }

    @Override
    public Optional<PersonalStatsDto> getStats() {
        List<PerFormPersonalStatsDto> statsDtos = Stream.of(Arrays.asList(POPULAR, FILL_RATE, FORMS_PER_USER))
                .map(type ->
                        SQLite.select()
                                .from(PerFormPersonalStatsModel.class)
                                .where(PerFormPersonalStatsModel_Table.chartType.eq(type))
                                .querySingle().toPerFormPersonalStatsDto())
                .collect(Collectors.toList());
        return Optional.ofNullable(new PersonalStatsDto(statsDtos.get(0), statsDtos.get(1), statsDtos.get(2)));
    }
}