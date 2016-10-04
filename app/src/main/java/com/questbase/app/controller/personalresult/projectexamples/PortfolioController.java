package com.questbase.app.controller.personalresult.projectexamples;

import com.questbase.app.net.entity.PortfolioDto;

import java.util.List;

public interface PortfolioController {

    List<PortfolioDto> getPortfolio();

    void setPortfolio(List<PortfolioDto> portfolioDtos);
}