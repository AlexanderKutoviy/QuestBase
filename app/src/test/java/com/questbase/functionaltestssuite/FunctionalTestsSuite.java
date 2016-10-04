package com.questbase.functionaltestssuite;

import com.questbase.functionaltestssuite.controller.ControllerSuite;
import com.questbase.functionaltestssuite.dao.DaoSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({DaoSuite.class, ControllerSuite.class})
public class FunctionalTestsSuite {
}
