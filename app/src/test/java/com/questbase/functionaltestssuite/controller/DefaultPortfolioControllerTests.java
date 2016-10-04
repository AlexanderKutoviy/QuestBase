package com.questbase.functionaltestssuite.controller;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.questbase.app.BuildConfig;
import com.questbase.app.controller.files.FilesController;
import com.questbase.app.controller.personalresult.projectexamples.DefaultPortfolioController;
import com.questbase.app.controller.personalresult.projectexamples.PortfolioController;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 19, manifest = "src/main/AndroidManifest.xml")
public class DefaultPortfolioControllerTests {

    private final int AMOUNT = 10;

    @Test
    public void controllerSetGetTest() {
        new FlowConfig.Builder(RuntimeEnvironment.application.getApplicationContext()).build();
        PortfolioController portfolioController = initController();

        portfolioController.setPortfolio(initPortfolio());

        assertEquals(initPortfolio(), portfolioController.getPortfolio());

        FlowManager.destroy();
    }

    private DefaultPortfolioController initController() {
        PortfolioDao dao = new DbFlowPortfolioDao();
        FilesController filesController = mock(FilesController.class);
        doNothing().when(filesController).saveProjectExamplesResources(any(String.class));
        return new DefaultPortfolioController(dao, filesController);
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
