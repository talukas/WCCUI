package com.wsccui.test;

import static org.hamcrest.CoreMatchers.describedAs;
import com.wsccui.template.BaseListenerWsccui;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Iterator;
import static core.GlobalConstatnt.*;
import static com.wsccui.util.Util.conCatString;
import static core.ThreadsStore.getDraftOrder;
import static core.ThreadsStore.setDraftOrderContainer;
import static core.util.BaseConfig.getProperty;
import static core.util.CustomAssertThat.assertThat;
import static core.util.ObjectSupplier.$;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.wsccui.funcsuite.ReturnsRefundsReplacement;
import com.wsccui.template.BaseTest;
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

@Listeners(BaseListenerWsccui.class)
public class ReturnsRefundsReplacementTest extends BaseTest{
	
	@Parameter("Test case name")
	private String testCaseName;
	
	@DataProvider(name = "test", parallel = true)
	public Iterator<String[]> data() throws IOException {
		return $(ReturnsRefundsReplacement.class).getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "RRR");}
	
	@Override(id = 2)
	@Description("Test: Provide [Refund & Return] orders to credit, eGift cards or cash.")
	@Features({ "Features: WCC-002" })
	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
	@Severity(SeverityLevel.BLOCKER)
	@Stories({"Stories: CIR-002"})
	@TestCaseId("TestCaseId: TC-002")
	@Test(dataProvider = "test", groups = "regression", enabled = true)
  public void createReturnRefundTest01(String... paramtrs) throws InterruptedException, SQLException, KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException {
		
// assertThat(DRY_RUN_STATUS, describedAs("Expected FAIL for @Test - DRY RUN", is(false)));

		boolean sterlingValidation = false;
		testCaseName = paramtrs[2];
		setDraftOrderContainer(conCatString(paramtrs));
		
		ReturnsRefundsReplacement returnsRefundsReplacement = new ReturnsRefundsReplacement();
		 
		String returnOrder = returnsRefundsReplacement
		 .searchOrder(paramtrs[61])
		 .fillReasonReturn(paramtrs[62], paramtrs[63], paramtrs[64], paramtrs[65], paramtrs[66])
		 .setAddressOpt()
		 .setRefundReplcOpt(paramtrs[66], paramtrs[67], paramtrs[68], paramtrs[69])
		 .getReturnNumber();

		 assertThat(returnOrder, describedAs("Expected Not 'Null' and 12 digits in the relesed order number", notNullValue()));
		 getDraftOrder().append(", Return Number: "+returnOrder);
		 
		 /* FOR VALIDATION in Sterling. Order number */
		 sterlingValidation = returnsRefundsReplacement.refundReplacementSterlingValidation(returnOrder, paramtrs[66], paramtrs[61], paramtrs[62]);
		 
		 assertThat(sterlingValidation, describedAs("Expected correct status 'true' for Refund/Replacement order in Sterling", is(true)));	 
		 getDraftOrder().append(", Refund/Replacement status in Sterling: " + sterlingValidation);
		 
	}
}
