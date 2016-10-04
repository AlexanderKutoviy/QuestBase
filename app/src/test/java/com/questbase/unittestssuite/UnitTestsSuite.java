package com.questbase.unittestssuite;

import com.questbase.unittestssuite.controller.ControllerSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ControllerSuite.class})
public class UnitTestsSuite {
}