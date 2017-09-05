package com.wsccui.test;

//import static org.hamcrest.core.IsNot.not;
//import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import com.wsccui.template.BaseListenerWsccui;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import static core.GlobalConstatnt.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.wsccui.exception.InputParametrNullException;
import com.wsccui.exception.InvalidBalanceInquiryException;
import com.wsccui.funcsuite.BalanceInquiry;
import com.wsccui.template.BIBaseTest;
import static core.util.BaseConfig.getProperty;
import core.util.BaseUtil;
import static core.util.BaseUtil.conCatString;
import static core.ThreadsStore.setDraftOrderContainer;
import static core.util.ObjectSupplier.$;
import core.util.Override;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.TestCaseId;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.MatcherAssert.assertThat;

@Listeners(BaseListenerWsccui.class)
public class BalanceInquiryTest extends BIBaseTest {

	@Parameter("Test case title")
	private String testCaseTitle;

	@Parameter("QA Complete ID")
	private String qacID;

	@DataProvider(name = "test")
	public Iterator<String[]> data() throws IOException {

		return $(BaseUtil.class).getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "WBI");
	}

	@Override(id = 2)
	@Description("Test: Check Balance Inquiry in WSCCUI")
	@Features({"Smoke"})
	@TestCaseId("17244")
	@Test(groups = "Smoke", dataProvider = "test", enabled = true)
	
	public void balanceInquiryTest01(String... paramtrs) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException, InputParametrNullException, InvalidBalanceInquiryException {

		testCaseTitle = paramtrs[2];
		qacID = paramtrs[0];
		setDraftOrderContainer(conCatString(paramtrs));

		boolean isBalanceCorrectActual = false;
		BalanceInquiry balanceInquiry = new BalanceInquiry();
		isBalanceCorrectActual = balanceInquiry.getBalanceSvcCard(paramtrs[133], paramtrs[134], paramtrs[135]);

		//7777085043683254:70676411 -> 1
		//7777085043679788:60610890 -> 0

		assertThat(isBalanceCorrectActual, describedAs("Expected balance popup menu without any Errors message. Card type ["+paramtrs[133]+"], number ["+(paramtrs[134] != null && !paramtrs[134].isEmpty() ? paramtrs[134] : paramtrs[135])+"]", is(true)));
	}
}// END of TEST CLASS