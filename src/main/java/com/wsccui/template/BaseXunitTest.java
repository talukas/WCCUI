package com.wsccui.template;

import static core.GlobalConstatnt.*;
import static core.util.BaseUtil.getDateTimeFormat;
import static core.util.BaseUtil.wResultFile;
import static core.util.BaseConfig.DRY_RUN_STATUS;
import static core.util.BaseConfig.ENV_NAME;
//import static core.util.BaseConfig.createJdbcDriver;
import static core.util.BaseConfig.getDriverType;
import static core.util.BaseConfig.getProperty;
//import static core.util.WebDriverFactory.createDriver;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import core.util.Override;


/**
 * @author Yurii Chukhrai on [Aug 2016]
 * @version - [1.0]
 * @file - [BaseTest.java]
 */

public class BaseXunitTest implements ITest 
{

	static final Logger LOG = Logger.getLogger(BaseXunitTest.class.getName());

	/* Static WebDriver Runner for multiThread */
	//private static final ThreadLocal<WebDriver> WEB_DRIVER_CONTAINER = new ThreadLocal<WebDriver>();
	/* Static WebDriver Runner for multiThread */
	// private static final ThreadLocal<String> TEST_SUITE_NAME = new
	// ThreadLocal<String>();
	//private static Connection ORACLE_JDBC;
	
	private static final ThreadLocal<String> TEST_NAME_CONTAINER = new ThreadLocal<String>();
	//private String testName = "";
	private String testSuiteName;

//	public static Connection getJdbcConnect() {
//		return ORACLE_JDBC;
//	}

	// @Step("Call WebDriver from 'Container'")
//	public static WebDriver getDriverContainer() {
//
//		return WEB_DRIVER_CONTAINER.get();
//	}

	// @Step("Call name test suite from 'Container'")
	// public static String getTestSuiteContainer() {
	//
	// return TEST_SUITE_NAME.get();
	// }

	@BeforeTest(alwaysRun = true)
	public void startTest(final ITestContext testContext) throws SocketException, UnknownHostException, Exception {
		// TEST_SUITE_NAME.set(testContext.getName());
		testSuiteName = testContext.getCurrentXmlTest().getSuite().getName();
		//ORACLE_JDBC = createJdbcDriver();
		// it prints "Check name test"
	}

	@AfterTest(alwaysRun = true)
	public void downTest() throws SQLException {
		
//		if (ORACLE_JDBC != null) {
//			ORACLE_JDBC.close();
//			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Oracle JDBC Connection - was normaly closed.");
//		}
	}

	private  void setTestName(String newTestName) {
		//testName = newTestName;
		TEST_NAME_CONTAINER.set(newTestName);
	}

	@Override
	public  String getTestName() {
		return TEST_NAME_CONTAINER.get();
	}

	/* Before, After Method */
	@BeforeMethod(alwaysRun = true)
	// @Step("Run method [@BeforeMethod]")
	public synchronized void setUpMethod(Method method, Object[] parametrs) throws IOException {

		setTestName(method.getName());
		Override newName = method.getAnnotation(Override.class);

		String testCaseId = ((String[]) parametrs[0])[newName.id()];
		setTestName(testCaseId);
		
		//	System.out.println("Run setUpMethod");
		//	if (!DRY_RUN_STATUS) {
		//	WEB_DRIVER_CONTAINER.set(createDriver(getDriverType()));
		//	}
		
	}

	@AfterMethod(alwaysRun = true)
	public void tearDownMethod() {

//		if (WEB_DRIVER_CONTAINER.get() != null) {
//			WEB_DRIVER_CONTAINER.get().close();
//			WEB_DRIVER_CONTAINER.get().quit();
//
//			WEB_DRIVER_CONTAINER.remove();
//		}
	}
	
	@AfterSuite(alwaysRun = true)
	public void liteReport() {

//		String nameFilePass = ENV_NAME.toUpperCase() + "_"
//				+ testSuiteName.replaceAll(CHARS_NOT_ALLOWED, "").replaceAll("\u00a0", "") + "_PASS_"
//				+ getTimeStamp("MM-dd-yyyy_HH-mm-ss");
//		
//		String nameFileFail = ENV_NAME.toUpperCase() + "_"
//				+ testSuiteName.replaceAll(CHARS_NOT_ALLOWED, "").replaceAll("\u00a0", "") + "_FAIL_"
//				+ getTimeStamp("MM-dd-yyyy_HH-mm-ss");
//
//		wResultFile(passOrders, getProperty("resultPath") + "PASS/", nameFilePass);
//		wResultFile(failOrders, getProperty("resultPath") + "FAIL/", nameFileFail);
	}

}// END of CLASS
