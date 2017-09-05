package com.wsccui.test;

import static core.GlobalConstatnt.*;
import static com.wsccui.util.Util.*;
import static core.ThreadsStore.getDraftOrder;
import static core.ThreadsStore.setDraftOrderContainer;
import static core.util.BaseConfig.getProperty;
import static core.util.BaseUtil.conCatString;
import static core.util.CustomAssertThat.assertThat;
import static core.util.ObjectSupplier.$;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.wsccui.funcsuite.CreateAlert;
import com.wsccui.funcsuite.CreateOrder;
import com.wsccui.template.BaseTest;
import core.util.Override;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Issue;
import ru.yandex.qatools.allure.annotations.Issues;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import com.wsccui.template.BaseListenerWsccui;

@Listeners(BaseListenerWsccui.class)
public class CreateAlertTest extends BaseTest {

	@Parameter("Test case name")
	private String testCaseName;

	@DataProvider(name = "test", parallel = true)
	public Iterator<String[]> data() throws IOException {

		return $(CreateAlert.class).getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "WAM");
	}

	@Override(id = 2)
	@Description("Test: Create Alert in WSCCUI")
	@Features("Features: WCC-001" )
	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
	@Severity(SeverityLevel.BLOCKER)
	@Stories("Stories: CIR-098")
	//@TestCaseId("TestCaseId: TC-001")
	@Test(dataProvider = "test", groups = "regression", enabled = true)
	public void createAlertTest01(String... paramtrs) throws NumberFormatException, SocketException, UnknownHostException, Exception {

		boolean sterlingValidation = false;
		testCaseName = paramtrs[2];
		setDraftOrderContainer(conCatString(paramtrs));
		
		final CreateOrder createOrder = new CreateOrder();
		
		/* Check Host (WSCCUI QA & MOU) live or NOT, here */
		//assertThat(createOrder.getRespondeCode(getProperty("start_url")), describedAs("Expected HTTP Status code [200] - OK", allOf(is(not(0)), is(200))));
		
		createOrder
		.createDraftOrder(paramtrs[14])
		.selectCustomerCriteria(paramtrs[43], paramtrs[17], paramtrs[18], paramtrs[19], paramtrs[32], paramtrs[20], paramtrs[21],
				paramtrs[22], paramtrs[30], paramtrs[31])
		.enterItem(paramtrs[13], paramtrs[15], paramtrs[16])
		.setCustomProperties(paramtrs[33], paramtrs[34], paramtrs[35])
		.setBillToAdress(paramtrs[17], paramtrs[18], paramtrs[19], paramtrs[32], paramtrs[20], paramtrs[21],
				paramtrs[22], paramtrs[30], paramtrs[31])
		.setGiftOptions(paramtrs[41])
		.changeCarrierService(paramtrs[13])
				// .setShipmentHolds()
				// .setInstructions()
		
		.changeAddress(paramtrs[42])
		.setPaymentTypeAndPromo(paramtrs[36], paramtrs[37], paramtrs[39], paramtrs[38], paramtrs[40]);
		
		String actualOrderNumber = createOrder.getOrderNumber();

		attachText("Actual Order number.", actualOrderNumber);
		assertThat(actualOrderNumber, describedAs("Expected not null order number.", notNullValue()));

		// draftOrders.append(", Order number: "+actualOrderNumber);

		/* FOR VALIDATION in Sterling. Order number */
		sterlingValidation = createOrder.isOrderValidationSterling(actualOrderNumber);
		assertThat(sterlingValidation, describedAs("Expected correct status 'true' order in Sterling", is(true)));
		// draftOrders.append(", Available in Sterling [" + SterlingValidation +
		// "]");

		final CreateAlert createAlert = new CreateAlert();

		String alertIDactual = createAlert
				.createDraftAlert(paramtrs[85], paramtrs[86])
				.enterAlertOptions(paramtrs[87], paramtrs[88]); 

		attachText("Actual Alert ID.", alertIDactual);
		getDraftOrder().append(", Alert ID: " + alertIDactual);
		assertThat(alertIDactual, describedAs("Expected not null order number.", notNullValue()));
		

		/* FOR VALIDATION in Sterling. Test case ID, Order number */
		sterlingValidation = createAlert.alertValidationSterling(actualOrderNumber, paramtrs[85], alertIDactual,
				paramtrs[88]);
		
		assertThat(sterlingValidation, describedAs("Expected correct status 'true' for order in Sterling", is(true)));
		getDraftOrder().append(", Alert status in the sterling: " + sterlingValidation);
	}
}// END of TEST CLASS