package com.questbase.app.controller.personalresult.projectexamples;

import com.annimon.stream.Stream;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.dao.personalresult.portfolio.PortfolioDao;
import com.questbase.app.net.entity.PortfolioDto;

import java.util.List;

public class DefaultPortfolioController implements PortfolioController {

    private final PortfolioDao portfolioDao;
    private final FilesController filesController;

    public DefaultPortfolioController(PortfolioDao portfolioDao, FilesController filesController) {
        this.portfolioDao = portfolioDao;
        this.filesController = filesController;
    }

    @Override
    public List<PortfolioDto> getPortfolio() {
        return portfolioDao.read();
    }

    @Override
    public void setPortfolio(List<PortfolioDto> portfolioDtos) {
        Stream.of(portfolioDtos).forEach(portfolioDto -> {
            portfolioDao.create(portfolioDto);
            filesController.saveProjectExamplesResources(portfolioDto.img);
        });
    }
}