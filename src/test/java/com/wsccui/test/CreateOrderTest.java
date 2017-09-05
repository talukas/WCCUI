package com.wsccui.test;

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
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.wsccui.funcsuite.CreateOrder;
import com.wsccui.template.BaseTest;
import core.util.Override;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import com.wsccui.template.BaseListenerWsccui;

@Listeners(BaseListenerWsccui.class)
public class CreateOrderTest extends BaseTest {

	@Parameter("Test case title")
	private String testCaseTitle;

	@Parameter("QA Complete ID")
	private String qacID;

	@DataProvider(name = "test")
	public Iterator<String[]> data() throws IOException {
		CreateOrder createOrder = new CreateOrder();

		return createOrder.getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "WCO");
	}

	@Override(id = 2)
	@Description("Test: Create Order in WSCCUI")
	@Features({ "Create Order" })
	@Test(dataProvider = "test", groups = "regression", enabled = true)
	public void createOrderTest01(String... paramtrs) throws NumberFormatException, SocketException, UnknownHostException, Exception {

		//boolean sterlingStatusActual = false;
		testCaseTitle = paramtrs[2];
		qacID = paramtrs[0];
		setDraftOrderContainer(conCatString(paramtrs));

		CreateOrder createOrder = new CreateOrder();

		createOrder.createDraftOrder(paramtrs[14])
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
		getDraftOrder().append(", Order number: " + actualOrderNumber);

		/* FOR VALIDATION in Sterling. Order number */
		// sterlingStatusActual = createOrder.isOrderValidationSterling(actualOrderNumber);
		// assertThat(sterlingStatusActual, describedAs("Expected correct status 'true' order in Sterling", is(true)));
		// draftOrders.append(", Available in Sterling [" + sterlingStatusActual + "]");

		createOrder.handleResolveHolds(actualOrderNumber);
		
		uploadToList(getTransFileOrders(), "wsccui,"+ paramtrs[0] +","+ paramtrs[2] +","+ getEnv().toUpperCase() +","+ paramtrs[14] +","+ actualOrderNumber+",STR_CDM.ORDERPUBLISH");
	}
}// END of TEST CLASS