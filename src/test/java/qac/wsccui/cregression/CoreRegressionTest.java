package qac.wsccui.cregression;

import static core.GlobalConstatnt.*;
import static com.wsccui.util.Util.*;
import static core.ThreadsStore.getDraftOrder;
import static core.ThreadsStore.getTransFileOrders;
import static core.ThreadsStore.setDraftOrderContainer;
import static core.util.BaseConfig.getEnv;
import static core.util.BaseConfig.getProperty;
import static core.util.BaseUtil.conCatString;
import static core.util.BaseUtil.uploadToList;
import static core.util.CustomAssertThat.assertThat;
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

import com.wsccui.funcsuite.CreateAppeasements;
import com.wsccui.funcsuite.CreateOrder;
import com.wsccui.template.BaseTest;
import core.util.Override;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.TestCaseId;
import com.wsccui.template.BaseListenerWsccui;

@Listeners(BaseListenerWsccui.class)
public class CoreRegressionTest extends BaseTest {

	@Parameter("Test case title")
	private String testCaseTitle;

	@Parameter("QA Complete ID")
	private String qacID;

	@DataProvider(name = "test9311")
	public Iterator<String[]> dataTc9311() throws IOException {
		CreateOrder createOrder = new CreateOrder();

		return createOrder.getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "", "9311");
	}

	@Override(id = 2)
	@Description("Verify whether CSR is able to Waive MONO/PZ charges on the order line and Verify whether credit memo is getting issued on the order")
	@Features({"Core Regression"})
	@TestCaseId("9311")
	@Test(dataProvider = "test9311", groups = {"Regression"}, enabled = true)
	public void coreRegressionTest9311(String... paramtrs) throws NumberFormatException, SocketException, UnknownHostException, Exception {

		//boolean sterlingStatusActual = false;
		testCaseTitle = paramtrs[2];
		qacID = paramtrs[0];
		boolean sterlingValidation;
		
		setDraftOrderContainer(conCatString(paramtrs));

		CreateOrder createOrder = new CreateOrder();

		createOrder.createDraftOrder(paramtrs[14])
				.selectCustomerCriteria(paramtrs[35], paramtrs[17], paramtrs[18], paramtrs[19], paramtrs[32], paramtrs[20], paramtrs[21],
						paramtrs[22], paramtrs[30], paramtrs[31])
				.enterItem(paramtrs[13], paramtrs[15], paramtrs[16])
				.setCustomProperties(paramtrs[33], paramtrs[34], paramtrs[43])
				.setBillToAdress(paramtrs[17], paramtrs[18], paramtrs[19], paramtrs[32], paramtrs[20], paramtrs[21],
						paramtrs[22], paramtrs[30], paramtrs[31])
				.setGiftOptions(paramtrs[41])
				.changeCarrierService(paramtrs[13])
				// .setShipmentHolds()
				// .setInstructions()

				.changeAddress(paramtrs[42])
				.setPaymentTypeAndPromo(paramtrs[36], paramtrs[37], paramtrs[39], paramtrs[38], paramtrs[40]);

		final String actualOrderNumber = createOrder.getOrderNumber();

		
		assertThat(actualOrderNumber, describedAs("Expected not null order number.", notNullValue()));
		attachText("Actual Order number.", actualOrderNumber);
		getDraftOrder().append(", Order number: " + actualOrderNumber);

		/* FOR VALIDATION in Sterling. Order number */
		// sterlingStatusActual = createOrder.isOrderValidationSterling(actualOrderNumber);
		// assertThat(sterlingStatusActual, describedAs("Expected correct status 'true' order in Sterling", is(true)));
		// draftOrders.append(", Available in Sterling [" + sterlingStatusActual + "]");

		createOrder.handleResolveHolds(actualOrderNumber);
		//assertThat(createOrder.isCcAuthorizedSterling(actualOrderNumber), describedAs("Payment Services. Expected correct CC Authorization Response status 'TRUE' in Sterling", is(true)));
		
		//uploadToList(getTransFileOrders(), "wsccui,"+ paramtrs[0] +","+ paramtrs[2] +","+ getEnv().toUpperCase() +","+ paramtrs[14] +","+ actualOrderNumber+",STR_CDM.ORDERPUBLISH");

		CreateAppeasements createAppeasements = new CreateAppeasements();
		sterlingValidation = createAppeasements
				.createDraftAppeasements(actualOrderNumber, paramtrs[93], paramtrs[94], paramtrs[95])
				.setAppeasementOptions(paramtrs[96], paramtrs[97], paramtrs[98], paramtrs[99], paramtrs[93])
				.getOrderInvoices(actualOrderNumber, paramtrs[93]);

		assertThat(sterlingValidation, describedAs("Expected correct status 'true' appeasement in Sterling", is(true)));
		
	}
}// END of TEST CLASS