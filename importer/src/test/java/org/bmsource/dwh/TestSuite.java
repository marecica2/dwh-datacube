package org.bmsource.dwh;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SuiteDisplayName("API tests")
@SelectPackages("org.bmsource.dwh.*")
public class TestSuite {
}
