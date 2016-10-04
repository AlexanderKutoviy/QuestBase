package com.questbase.functionaltestssuite.controller;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({DefaultCategoryControllerTests.class,
        DefaultProfileControllerTests.class,
        TestSessionControllerTests.class,
        DefaultPortfolioControllerTests.class,
        DefaultTransactionControllerTests.class})
public class ControllerSuite {
}