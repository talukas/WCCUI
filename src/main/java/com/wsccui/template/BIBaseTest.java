package com.wsccui.template;

import static core.GlobalConstatnt.*;
import static core.util.BaseUtil.getDateTimeFormat;
import static core.util.BaseUtil.uploadToList;
import static core.util.BaseUtil.wResultFile;
import static core.ThreadsStore.*;
import static core.dtc.AS400Constants.TRANS_FILE_SCHEMA;
import static core.util.BaseConfig.DRY_RUN_STATUS;
import static core.util.BaseConfig.ENV_NAME;
import static core.util.BaseConfig.createAs400JdbcDriver;
import static core.util.BaseConfig.getDriverType;
import static core.util.BaseConfig.getEnv;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import core.util.Override;

/**
 * @author Yurii Chukhrai on [Aug 2016]
 * @version - [1.0]
 * @file - [BaseTest.java]
 */

public class BIBaseTest implements ITest {

	static final Logger LOG = Logger.getLogger(BIBaseTest.class.getName());

	private String testName = "";
	private String testSuiteName;

	@BeforeSuite(alwaysRun = true)
	public void startTest(final ITestContext testContext) throws SocketException, UnknownHostException, Exception {
		
		testSuiteName = testContext.getCurrentXmlTest().getSuite().getName();
		
		uploadToList(getTransFileOrders(), TRANS_FILE_SCHEMA);
		setAs400JdbcConnect(createAs400JdbcDriver(getEnv()));
		//setSterlingJdbcConnect(createSterlingJdbcDriver(getEnv()));
		//setTibcoJdbcConnect(createTibcoJdbcDriver(getEnv()));
	}

	private void setTestName(String newTestName) {
		testName = newTestName;
	}

	@Override
	public String getTestName() {
		return testName;
	}

	/* Before, After Method */
	@BeforeMethod(alwaysRun = true)
	public void setUpMethod(Method method, Object[] parametrs) throws IOException, KeyManagementException, NoSuchAlgorithmException {

		setTestName(method.getName());
		Override newName = method.getAnnotation(Override.class);

		String testCaseId = ((String[]) parametrs[0])[newName.id()];
		setTestName(testCaseId);
		
		setTestInfoContainer();
		
		if (!DRY_RUN_STATUS) {
			/* Check Host (WSCCUI QA & MOU) live or NOT, here */
			//assertThat($(Util.class).getRespondeCode(getProperty("start_url")), describedAs("Expected HTTP Status code [200] - OK", allOf(is(not(0)), is(200))));
			setWebDriver(getDriverType());
		}	
	}

	@AfterMethod(alwaysRun = true)
	public void tearDownMethod() throws InterruptedException {

		if (getWebDriver() != null) {
			//getWebDriver().close();
			getWebDriver().quit();
			Thread.sleep(3_000);
		}
		int count = 0;
		while(true){
			
			if(count > 10){
				LOG.error("Can't Close WebDriver");
				//
				//((RemoteWebDriver)getWebDriver()).getSessionId();
				break;
			}
			else if(((RemoteWebDriver)getWebDriver()).getSessionId() == null){
				break;
				}
			else{
				LOG.warn("WebDriver was not closed ["+ ((RemoteWebDriver)getWebDriver()).getSessionId() +"]");
				((RemoteWebDriver)getWebDriver()).resetInputState();
				getWebDriver().quit();
				count++;
				Thread.sleep(3_000);
			}
		}
		
		getWebDriverContainer().remove();
		cleanTestInfoContainer();
		cleanDraftOrderContainer();
	}
	
	@AfterSuite(alwaysRun = true)
	public void liteReport() throws SQLException {

		if (getSterlingJdbcConnect() != null) {
			getSterlingJdbcConnect().close();
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Sterling JDBC Connection - was normaly closed.");
		}
		
		if (getTibcoJdbcConnect() != null) {
			getTibcoJdbcConnect().close();
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"TIBCO JDBC Connection - was normaly closed.");
		}
		
		if(getAs400JdbcConnect() != null){
			getAs400JdbcConnect().close();
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"AS400 JDBC Connection - was normaly closed.");
		}
		
		getWebDriverContainer().remove();
		
		
		String nameFilePass = ENV_NAME.toUpperCase() + "_"
				+ testSuiteName.replaceAll(CHARS_NOT_ALLOWED, "").replaceAll("[^\\p{ASCII}]", "") + "_PASS_"
				+ getDateTimeFormat("MM-dd-yyyy_HH-mm-ss");
		
		String nameFileFail = ENV_NAME.toUpperCase() + "_"
				+ testSuiteName.replaceAll(CHARS_NOT_ALLOWED, "").replaceAll("[^\\p{ASCII}]", "") + "_FAIL_"
				+ getDateTimeFormat("MM-dd-yyyy_HH-mm-ss");

		wResultFile(getPassOrders(), "./target/wsccui-out-csv/", "pass/", nameFilePass);
		wResultFile(getFailOrders(), "./target/wsccui-out-csv/", "fal/", nameFileFail);
		//wResultFile(getTransFileOrders(), "./target/wsccui-out-csv/", "", "wsccuiTransFile");
	}

}// END of CLASS
