package com.questbase.app.dao.personalresult.portfolio;

import com.questbase.app.net.entity.PortfolioDto;

import java.util.List;

public interface PortfolioDao {

    void create(PortfolioDto portfolioDto);

    List<PortfolioDto> read();

    void update(PortfolioDto portfolioDto);

    void delete(PortfolioDto portfolioDto);
}