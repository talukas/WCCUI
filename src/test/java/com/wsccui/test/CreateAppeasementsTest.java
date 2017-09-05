package com.wsccui.test;

import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import static core.GlobalConstatnt.*;
import static com.wsccui.util.Util.attachText;
import static com.wsccui.util.Util.conCatString;
import static core.ThreadsStore.getDraftOrder;
import static core.ThreadsStore.setDraftOrderContainer;
import static core.util.BaseConfig.getProperty;
import static core.util.CustomAssertThat.assertThat;
import static core.util.ObjectSupplier.$;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.wsccui.funcsuite.CreateAppeasements;
import com.wsccui.funcsuite.CreateOrder;
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
import ru.yandex.qatools.allure.model.SeverityLevel;
import com.wsccui.template.BaseListenerWsccui;

@Listeners(BaseListenerWsccui.class)
public class CreateAppeasementsTest extends BaseTest {
	
	@Parameter("Test case name")
	private String testCaseName;

	@DataProvider(name = "test", parallel = true)
	public Iterator<String[]> data() throws IOException {
		return $(ReturnsRefundsReplacement.class).getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "AP");//93
	}

	@Override(id = 2)
	@Description( "Test: Provide [Appeasements] to orders." )
	@Features("Search order")
	@Issues({ @Issue("Issue: CIR-001"), @Issue("Issue: CIR-002") })
	@Severity( SeverityLevel.BLOCKER )
	@Stories("Stories: CIR-002")
	//@TestCaseId( "TestCaseId: TC-002" )
	@Test(dataProvider = "test", groups = "regression", enabled = true)
	public void createAppeasementsTet01(String... paramtrs) throws NumberFormatException, SocketException, UnknownHostException, Exception {
		boolean sterlingValidation;
		testCaseName = paramtrs[2];
		setDraftOrderContainer(conCatString(paramtrs));

		CreateOrder createOrder = new CreateOrder();
		CreateAppeasements createAppeasements = new CreateAppeasements();

		/* Create order */
		createOrder.createDraftOrder(paramtrs[11])
		.selectCustomerCriteria(paramtrs[35], paramtrs[17], paramtrs[18], paramtrs[19], paramtrs[32], paramtrs[20], paramtrs[21],
				paramtrs[22], paramtrs[30], paramtrs[31])
		.enterItem(paramtrs[10], paramtrs[12], paramtrs[13])
		.setCustomProperties(paramtrs[23], paramtrs[24], paramtrs[25])
		.setBillToAdress(paramtrs[14], paramtrs[15], paramtrs[16], paramtrs[22], paramtrs[17], paramtrs[18],
				paramtrs[19], paramtrs[20], paramtrs[21])
		.setGiftOptions(paramtrs[31])
		.changeCarrierService(paramtrs[10])
				// .setShipmentHolds()
				// .setInstructions()
		
		.changeAddress(paramtrs[32])
		.setPaymentTypeAndPromo(paramtrs[26], paramtrs[27], paramtrs[28], paramtrs[29], paramtrs[30]);
		
		String actualOrderNumber = createOrder.getOrderNumber();
		
		attachText("Actual Order number.", actualOrderNumber);
		assertThat(actualOrderNumber, describedAs("Expected not null order number.", notNullValue()));
		getDraftOrder().append(", Order number: "+actualOrderNumber);
		
		/* FOR VALIDATION in Sterling. Order number */
		sterlingValidation = createOrder.isOrderValidationSterling(actualOrderNumber);
		assertThat(sterlingValidation, describedAs("Expected correct status 'true' order in Sterling", is(true)));
		getDraftOrder().append(", Available in Sterling [" + sterlingValidation + "]");

		sterlingValidation = createAppeasements
				.createDraftAppeasements(actualOrderNumber, paramtrs[85], paramtrs[86], paramtrs[87])
				.setAppeasementOptions(paramtrs[88], paramtrs[89], paramtrs[90], paramtrs[91], paramtrs[85])
				.getOrderInvoices(actualOrderNumber, paramtrs[85]);

		assertThat(sterlingValidation, describedAs("Expected correct status 'true' appeasement in Sterling", is(true)));
		//draftOrders.append(", Available in Sterling [" + SterlingValidation + "]");
	}
} //END OF CLASASS