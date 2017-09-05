package com.wsccui.test1;
//package com.wsccui.test;
//
//import static core.util.BaseUtil.attachText;
//import static core.util.Config.LANDING_PAGE;
//import static core.util.Config.getOrderNumber;
//import static core.util.CustomAssertThat.assertThat;
//import static org.hamcrest.CoreMatchers.describedAs;
//import static org.hamcrest.CoreMatchers.is;
//
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import com.wsccui.CreateOrder;
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
//
//@Listeners(BaseListener.class)
//public class OrderStatusValidationTest extends BaseTest {
//
//	// title validation
//
//	private CreateOrder ordeStatusValidation;
//	// private final boolean DRY_RUN_STATUS = Config.getDryRunStatus();
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-002")
//	@Issues({ @Issue("Issue: CIR-002"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", enabled = true)
//	public void test_01_landingPage() throws InterruptedException {
//		ordeStatusValidation = new CreateOrder();
//
//		String actualLandingPage = ordeStatusValidation.landingPage().getLandingPage();
//
//		assertThat(actualLandingPage, describedAs("Expected titel " + Config.getProperty(LANDING_PAGE),
//				is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-002")
//	@Issues({ @Issue("Issue: CIR-002"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", enabled = true)
//	public void test_02_logIn() throws InterruptedException {
//
//		String actualLandingPage = ordeStatusValidation.logIn().getLandingPage();
//
//		assertThat(actualLandingPage, describedAs("Expected titel [" + Config.getProperty(LANDING_PAGE)+"]",
//				is(Config.getProperty(LANDING_PAGE))));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_02_logIn", enabled = true)
//	public void test_03_statusOrderValid() throws InterruptedException {
//
//		String actualOrderStatus = ordeStatusValidation.getOrderStatus(getOrderNumber());
//		String expectedOrderStatus = "In Process";
//		
//		attachText("Actual order status.", actualOrderStatus);
//
//		assertThat(actualOrderStatus,
//				describedAs("Expected order status [" + expectedOrderStatus+"]", is(expectedOrderStatus)));
//	}
//
//	@Features({ "Features: WCC-001" })
//	@Stories({ "Stories: CIR-098" })
//	@TestCaseId("TestCaseId: TC-001")
//	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
//	@Severity(SeverityLevel.BLOCKER)
//	@Test(groups = "regression", dependsOnMethods = "test_03_statusOrderValid", enabled = true)
//	public void test_04_logOut() throws InterruptedException {
//
//		String actualLandingPage = ordeStatusValidation.logOut().getLandingPage();
//
//		assertThat(actualLandingPage, describedAs("Expected landing page [" + Config.getProperty(LANDING_PAGE)+"]",
//				is(Config.getProperty(LANDING_PAGE))));
//	}
//}// END of TEST CLASS