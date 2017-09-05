package qac.wsccui.smoke;

//import static org.hamcrest.core.IsNot.not;
//import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import com.wsccui.template.BaseListenerWsccui;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
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
import com.wsccui.funcsuite.CreateOrder;
import com.wsccui.template.BaseTest;
import static core.util.BaseConfig.getEnv;
import static core.util.BaseConfig.getProperty;
import core.util.BaseUtil;
import static core.util.BaseUtil.attachText;
import static core.util.BaseUtil.conCatString;
import static core.util.BaseUtil.uploadToList;
import static core.util.CustomAssertThat.assertThat;
import static core.ThreadsStore.getDraftOrder;
import static core.ThreadsStore.getTransFileOrders;
import static core.ThreadsStore.setDraftOrderContainer;
import static core.util.ObjectSupplier.$;
import core.util.Override;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.TestCaseId;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Listeners(BaseListenerWsccui.class)
public class SmokeTest extends BaseTest {

	@Parameter("Test case title")
	private String testCaseTitle;

	@Parameter("QA Complete ID")
	private String qacID;

	@DataProvider(name = "tc17244")
	public Iterator<String[]> dataTc17244() throws IOException {

		return $(BaseUtil.class).getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "", "17244");
	}

	@Override(id = 2)
	@Description("Validate whether the Balance enquiry system is working as expected or not")
	@Features({"Smoke"})
	@TestCaseId("17244")
	@Test(groups = "Smoke", dataProvider = "tc17244", enabled = true)
	public void balanceInquiryTest01(String... paramtrs) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException, InputParametrNullException, InvalidBalanceInquiryException {

		testCaseTitle = paramtrs[2];
		qacID = paramtrs[0];
		setDraftOrderContainer(conCatString(paramtrs));

		boolean isBalanceCorrectActual = false;
		BalanceInquiry balanceInquiry = new BalanceInquiry();
		isBalanceCorrectActual = balanceInquiry.getBalanceSvcCard(paramtrs[133], paramtrs[134], paramtrs[135]);

		//7777085043683254:70676411 -> 1
		//7777085043679788:60610890 -> 0
		assertThat(isBalanceCorrectActual, describedAs("Expected balance popup menu without any Errors message. Card ["+paramtrs[134]+"/"+paramtrs[135]+"]", is(true)));
	}
	
	
	@DataProvider(name = "tc19269")
	public Iterator<String[]> dataTc19269() throws IOException {
		
		return $(BaseUtil.class).getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "", "19269");
	}

	@Override(id = 2)
	@Description("Order Create from WCC with DC SKUs having Onhand>0 and use Creditcard as Payment Type. Line-1 : DC SKU, Line-2 : DS SKU, Line-3 : DC FN Item, Line-4 : DC Pack SKU.")
	@Features({"Smoke"})
	@TestCaseId("19269")
	@Test(dataProvider = "tc19269", groups = "Smoke", enabled = true)
	public void createOrderTest01(String... paramtrs) throws NumberFormatException, SocketException, UnknownHostException, Exception {

		//boolean sterlingStatusActual = false;
		testCaseTitle = paramtrs[2];
		qacID = paramtrs[0];
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
	
	@DataProvider(name = "tc9599")
	public Iterator<String[]> dataTc9599() throws IOException {
		
		return $(BaseUtil.class).getTestSuiteDataProvider(getProperty("testDataPath"), SPLIT_BY_CSV, "", "9599");
	}

	@Override(id = 2)
	@Description("Create a Business order from WCC with DC FN items, shipped to WEST zone and payment type as Credit Card(MASTER)+SVC Perform Shipment and POD")
	@Features({"Smoke"})
	@TestCaseId("9599")
	@Test(dataProvider = "tc9599", groups = "Smoke", enabled = true)
	public void createOrderTest02(String... paramtrs) throws NumberFormatException, SocketException, UnknownHostException, Exception {

		//boolean sterlingStatusActual = false;
		testCaseTitle = paramtrs[2];
		qacID = paramtrs[0];
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