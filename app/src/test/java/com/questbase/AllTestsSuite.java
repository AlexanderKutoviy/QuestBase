package com.questbase;

import com.questbase.functionaltestssuite.FunctionalTestsSuite;
import com.questbase.unittestssuite.UnitTestsSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({UnitTestsSuite.class, FunctionalTestsSuite.class})
public class AllTestsSuite {
}