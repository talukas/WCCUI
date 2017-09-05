package com.wsccui.test;

import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import static core.GlobalConstatnt.*;
import static core.ThreadsStore.getDraftOrder;
import static core.ThreadsStore.setDraftOrderContainer;
import static core.util.BaseConfig.getProperty;
import static core.util.CustomAssertThat.assertThat;
import static core.util.ObjectSupplier.$;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.wsccui.funcsuite.SearchOrder;
import com.wsccui.template.BaseTest;
import static com.wsccui.util.Util.*;
import core.util.Override;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Issue;
import ru.yandex.qatools.allure.annotations.Issues;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;
import ru.yandex.qatools.allure.model.SeverityLevel;
import com.wsccui.template.BaseListenerWsccui;

@Listeners(BaseListenerWsccui.class)
public class SearchOrderTest extends BaseTest {
	
	@Parameter("Test case name")
	private String testCaseName;
	
	@DataProvider(name = "test", parallel = true)
	public Iterator<String[]> data() throws IOException {
		return $(SearchOrder.class).getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "WOS");}

	@Override(id = 2)
	@Description("Test: Search order in WSCCUI")
	@Features({ "Search order" })
	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
	@Severity(SeverityLevel.BLOCKER)
	@Stories({"Stories: CIR-098"})
	@TestCaseId("TestCaseId: TC-001")
	@Test(dataProvider = "test", enabled = true)
	public void searchOrderTest01(String... paramtrs) throws InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		
		//boolean SterlingValidation = false; 
		testCaseName = paramtrs[2];
		setDraftOrderContainer(conCatString(paramtrs));
		
		SearchOrder searchOrder = new SearchOrder();
		int howManyRecords = searchOrder.getFoundRecords(paramtrs[4], paramtrs[5], paramtrs[6], paramtrs[7], paramtrs[8], paramtrs[3]);

		attachText("Was found records", Integer.toString(howManyRecords));
		assertThat(howManyRecords, describedAs("Expected Not '0' order records", is(not(0))));
		
		getDraftOrder().append(", Records: " + howManyRecords);
		
		 /* FOR VALIDATION in Sterling. Order number */
		// SterlingValidation = $(SearchOrder.class).refundReplacementSterlingValidation(returnOrder, paramtrs[65], paramtrs[60], paramtrs[61]);
		 
		 // assertThat(SterlingValidation, describedAs("Expected correct status 'true' for Refund/Replacement order in Sterling", is(true)));	 
		// draftOrders.append(", Refund/Replacement status in Sterling: " + SterlingValidation);
	}
}// END of TEST CLASS