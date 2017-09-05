package com.wsccui.test1;

import java.io.IOException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import core.util.Override;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.TestCaseId;

public class TestCaseIDTest {
	
	final String[] tmp = new String[]{"1","2"};
	
	@Parameter("It was Test Case ID")
	//@TestCaseId("Test Suite Name")
	private String testCaseID;
	
	@DataProvider(name = "test", parallel = true)
	public Object[][] data1() throws IOException {
	return new Object[][]{
		{"Test Case Name Bla", " 001"},
		{"Test Case Name Bla", " 002"}};
}
	
	@Override(id = 1)
	@TestCaseId("Test Suite ID: 094")
	@Test(dataProvider = "test", groups = "regression", enabled = true)
  public void f(String tmp1, String tmp2) {
		testCaseID = tmp2;
		
  }


}
