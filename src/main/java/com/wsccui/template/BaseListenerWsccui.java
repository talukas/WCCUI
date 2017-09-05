package com.wsccui.template;

import static com.wsccui.ConstatntWSCCUI.*;
import static core.util.wsccui.WebDriverFactory.getFileNameSysPart;
import static core.util.wsccui.WebDriverFactory.getUaInfo;
import static core.ThreadsStore.getDraftOrder;
import static core.ThreadsStore.getFailOrders;
import static core.ThreadsStore.getPassOrders;
import static core.ThreadsStore.getTestInfo;
import static core.ThreadsStore.getWebDriver;
//import static core.ThreadsStore.getWebDriver;
import static core.util.ObjectSupplier.$;
import org.openqa.selenium.logging.LogType;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import core.util.BaseUtil;
//import core.webdriver.template.BaseListener;
import core.webdriver.template.BasePage;

/**
 * @author Yurii Chukhrai on [Aug 2016]
 * @version - [1.0]
 * @file - [BaseListener.java]
 */
/* This Class provide Actions if something happened in Test Classes (PASS-FAIL-RUN) */
public class BaseListenerWsccui extends BaseUtil implements ITestListener, IInvokedMethodListener {

	@Override
	public void onTestFailure(ITestResult result) {
		
		$(BasePageWsccui.class).makeScreenCapture("Test_FAIL_"+getFileNameSysPart() + "_" + getDateTimeFormat("MM-dd-yyyy_HH-mm-ss"), getWebDriver());
		attachText("User Agent information", getUaInfo());
		attachText("Logs. BROWSER" , getWebDriver() != null ? getWebDriver().manage().logs().get(LogType.BROWSER).getAll().toString() : "Browser logs. WebDriver was [null].");
		uploadToList(getFailOrders(), getDraftOrder() != null ? getDraftOrder().toString() : "Fail orders. Draft Orders was [null].");
		attachTextCsv("FAIL tests data",  concatCollectionToString(getFailOrders()));
		attachText("Debug info", concatCollectionToString(getTestInfo()));
		attachText("HTML_SRC" , getWebDriver() != null ? getWebDriver().getPageSource() : "HTML_SRC. WebDriver was [null].");
		
		/* Clear Cookies if Test Fail */
		setCookiesInstance(null); 
		setUrlToken(null);
	}
	
	/* Methods of Interface 'ITestListener' */
	@Override
	public void onTestStart(ITestResult result) {
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		$(BasePage.class).makeScreenCapture("Test_PASS_"+getFileNameSysPart() + "_" + getDateTimeFormat("MM-dd-yyyy_HH-mm-ss"), getWebDriver());
		uploadToList(getPassOrders(), getDraftOrder() != null ? getDraftOrder().toString() : "Pass orders. Draft Orders was [null].");
		attachTextCsv("PASS tests data",  concatCollectionToString(getPassOrders()));
		attachText("Debug info", concatCollectionToString(getTestInfo()));
		attachText("Logs. BROWSER" , getWebDriver() != null ? getWebDriver().manage().logs().get(LogType.BROWSER).getAll().toString() : "Browser logs. WebDriver was [null].");
	}
	
	@Override
	public void onTestSkipped(ITestResult result) {
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {
	}

	@Override
	public void onFinish(ITestContext context) {
	}

	@Override
	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
	}

	@Override
	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
	}
	
}