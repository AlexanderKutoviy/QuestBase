package com.questbase.functionaltestssuite.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({DbFlowProfileDaoTests.class,
        DbFlowFormDaoTests.class,
        DbFlowCategoryDaoTests.class,
        DbFlowPortfolioDaoTests.class,
        DbFlowPayoutEventDaoTests.class,
        DbFlowTransactionDaoTest.class,
        DbFlowTestSessionDaoTests.class})
public class DaoSuite {
}
