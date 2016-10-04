package com.questbase.app.dao.personalresult.portfolio;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.questbase.app.net.entity.PortfolioDto;

import java.util.List;

public class DbFlowPortfolioDao implements PortfolioDao {
    @Override
    public void create(PortfolioDto portfolioDto) {
        new PortfolioModel(portfolioDto).saveOnDuplicateUpdate();
    }

    @Override
    public List<PortfolioDto> read() {
        return Stream.of(SQLite.select()
                .from(PortfolioModel.class).queryList())
                .map(PortfolioModel::toProjectExample)
                .collect(Collectors.toList());
    }

    @Override
    public void update(PortfolioDto portfolioDto) {
        create(portfolioDto);
    }

    @Override
    public void delete(PortfolioDto portfolioDto) {
        new PortfolioModel(portfolioDto).delete();
    }
}
