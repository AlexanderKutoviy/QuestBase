package com.questbase.functionaltestssuite.dao;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.dao.personalresult.portfolio.DbFlowPortfolioDao;
import com.questbase.app.dao.personalresult.portfolio.PortfolioDao;
import com.questbase.app.net.entity.PortfolioDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DbFlowPortfolioDaoTests {

    private final int AMOUNT = 10;
    private final int UPDATE_DELETE_FORM_ID = 3;

    @Test
    public void createReadPortfolioTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        PortfolioDao portfolioDao = new DbFlowPortfolioDao();

        for (PortfolioDto portfolioDto : initPortfolio()) {
            portfolioDao.create(portfolioDto);
        }

        assertEquals(initPortfolio(), portfolioDao.read());

        FlowManager.destroy();
    }

    @Test
    public void createUpdateReadPortfolioTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        PortfolioDao portfolioDao = new DbFlowPortfolioDao();
        List<PortfolioDto> portfolioFromServer = initPortfolio();

        for (PortfolioDto portfolioDto : portfolioFromServer) {
            portfolioDao.create(portfolioDto);
        }

        assertEquals(portfolioFromServer, portfolioDao.read());

        PortfolioDto portfolioForUpdate = new PortfolioDto.Builder(UPDATE_DELETE_FORM_ID)
                .slug(String.valueOf(UPDATE_DELETE_FORM_ID + 10))
                .img(String.valueOf(UPDATE_DELETE_FORM_ID + 10))
                .research(String.valueOf(UPDATE_DELETE_FORM_ID + 10))
                .audience(String.valueOf(UPDATE_DELETE_FORM_ID + 10))
                .description(String.valueOf(UPDATE_DELETE_FORM_ID + 10))
                .expertise(String.valueOf(UPDATE_DELETE_FORM_ID + 10))
                .build();

        portfolioFromServer.add(UPDATE_DELETE_FORM_ID, portfolioForUpdate);
        portfolioFromServer.remove(UPDATE_DELETE_FORM_ID + 1);
        portfolioDao.update(portfolioForUpdate);

        assertEquals(portfolioFromServer, portfolioDao.read());

        FlowManager.destroy();
    }

    @Test
    public void createDeleteReadPortfolioTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        PortfolioDao portfolioDao = new DbFlowPortfolioDao();
        List<PortfolioDto> portfolioFromServer = initPortfolio();

        for (PortfolioDto portfolioDto : portfolioFromServer) {
            portfolioDao.create(portfolioDto);
        }

        assertEquals(portfolioFromServer, portfolioDao.read());

        PortfolioDto portfolioForDelete = new PortfolioDto.Builder(UPDATE_DELETE_FORM_ID)
                .slug(String.valueOf(UPDATE_DELETE_FORM_ID))
                .img(String.valueOf(UPDATE_DELETE_FORM_ID))
                .research(String.valueOf(UPDATE_DELETE_FORM_ID))
                .audience(String.valueOf(UPDATE_DELETE_FORM_ID))
                .description(String.valueOf(UPDATE_DELETE_FORM_ID))
                .expertise(String.valueOf(UPDATE_DELETE_FORM_ID))
                .build();

        portfolioFromServer.remove(portfolioForDelete);
        portfolioDao.delete(portfolioForDelete);

        assertEquals(portfolioFromServer, portfolioDao.read());

        FlowManager.destroy();
    }

    private List<PortfolioDto> initPortfolio() {
        List<PortfolioDto> portfolioDtos = new ArrayList<>(AMOUNT);
        for (int i = 0; i < AMOUNT; i++) {
            portfolioDtos.add(new PortfolioDto.Builder(i)
                    .slug(String.valueOf(i))
                    .img(String.valueOf(i))
                    .research(String.valueOf(i))
                    .audience(String.valueOf(i))
                    .description(String.valueOf(i))
                    .expertise(String.valueOf(i))
                    .build());
        }
        return portfolioDtos;
    }
}