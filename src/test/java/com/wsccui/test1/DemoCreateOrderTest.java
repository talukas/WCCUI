package com.wsccui.test1;
//package core;
//
//import static core.util.Config.LANDING_PAGE;
//import static core.util.CustomAssertThat.assertThat;
//import static org.hamcrest.CoreMatchers.describedAs;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.notNullValue;
//
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.wcc.WSCCUIMainPage;
//
//import core.template.BaseListener;
//import core.template.BaseTest;
//import core.util.Config;
//import ru.yandex.qatools.allure.annotations.Features;
//import ru.yandex.qatools.allure.annotations.Issue;
//import ru.yandex.qatools.allure.annotations.Issues;
//import ru.yandex.qatools.allure.annotations.Severity;
//import ru.yandex.qatools.allure.annotations.Stories;
//import ru.yandex.qatools.allure.annotations.TestCaseId;
//import ru.yandex.qatools.allure.model.SeverityLevel;
//import static core.util.BaseUtil.attachText;
//
//
//@Listeners(BaseListener.class)
//public class DemoCreateOrderTest extends BaseTest {
//
//	// title validation
//
//	private WSCCUIMainPage createOrder;
//	
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", enabled = true)
//	public void test_01_landingPage() throws InterruptedException {
//		createOrder = new WSCCUIMainPage();
//
//		String actualLandingPage = createOrder.landingPage().getLandingPage();
//
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_01_landingPage", enabled = true)
//	public void test_02_logInPage() throws InterruptedException {
//
//		String actualLandingPage = createOrder.logIn().getLandingPage();
//
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_02_logInPage", enabled = true)
//	public void test_03_selectConcept() throws InterruptedException {
//
//		String actualLandingPage = createOrder.selectConcept().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_03_selectConcept", enabled = true)
//	public void test_04_selectCustomerCriteria() throws InterruptedException {
//
//		String actualLandingPage = createOrder.selectCustomerCriteria().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_04_selectCustomerCriteria", enabled = true)
//	public void test_05_enterItem() throws InterruptedException {
//
//		String actualLandingPage = createOrder.enterItem().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_05_enterItem", enabled = false)
//	public void test_06_chooseVariation() throws InterruptedException {
//
//		String actualLandingPage = createOrder.chooseVariation().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_06_chooseVariation", enabled = false)
//	public void test_07_setPersonalize() throws InterruptedException {
//
//		String actualLandingPage = createOrder.setPersonalize().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_05_enterItem", enabled = true)
//	public void test_08_setAdress() throws InterruptedException {
//
//		String actualLandingPage = createOrder.setAdress().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_08_setAdress", enabled = false)
//	public void test_09_changeAddress() throws InterruptedException {
//
//		String actulLandingPage = createOrder.changeAddress().getLandingPage();
//		
//		assertThat(actulLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_08_setAdress", enabled = false)
//	public void test_10_changeCarrierService() throws InterruptedException {
//
//		String actualLandingPage = createOrder.changeCarrierService().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//	
//	
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_08_setAdress", enabled = false)
//	public void test_11_setShipmentHolds() throws InterruptedException {
//
//		String actualLandingPage = createOrder.setShipmentHolds().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_08_setAdress", enabled = false)
//	public void test_12_setInstructions() throws InterruptedException {
//
//		String actualLandingPage = createOrder.setInstructions().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_08_setAdress", enabled = false)
//	public void test_13_setGiftOptions() throws InterruptedException {
//
//		String actualLandingPage = createOrder.setGiftOptions().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_08_setAdress", enabled = true)
//	public void test_14_setPaymentTypeAndPromo() throws InterruptedException {
//
//		String actualLandingPage = createOrder.setPaymentTypeAndPromo().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_14_setPaymentTypeAndPromo", enabled = true)
//	public void test_15_getOrderNumberInfo() throws InterruptedException {
//
//		String actualOrderNumber = createOrder.getOrderNumber();
//		
//		attachText("Actual Order number.", actualOrderNumber);
//		
//		assertThat(actualOrderNumber,
//				describedAs("Expected not null order number.", notNullValue()));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_15_getOrderNumberInfo", enabled = true)
//	public void test_16_logOut() throws InterruptedException {
//
//		String actualLandingPage = createOrder.logOut().getLandingPage();
//		
//		assertThat(actualLandingPage,
//				describedAs("Expected landing page" + Config.getProperty(LANDING_PAGE), is(Config.getProperty(LANDING_PAGE))));
//	}
//
//}// END of TEST CLASS