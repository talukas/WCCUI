package com.wsccui.funcsuite;

import static core.GlobalConstatnt.*;
import static core.ThreadsStore.getTestInfo;
import static core.ThreadsStore.getWebDriver;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import core.db.sterling.SterlingDB;
import com.wsccui.template.BasePageWsccui;
import ru.yandex.qatools.allure.annotations.Step;

public class ReturnsRefundsReplacement extends BasePageWsccui {
	static final Logger LOG = Logger.getLogger(ReturnsRefundsReplacement.class.getName());
	// private final String msgScreenTitle = "Unknown Screen, expected [";
	// private final By orderNumberSearchField =
	// By.xpath("//label[contains(text(),'Order
	// Number:')]/following::div[1]/div[1]/div[1]/input[1]");
	// private boolean orderWasFound;

	@Step("Search order with status [Delivered] or [Shipped] for order number [{1}]")
	public ReturnsRefundsReplacement searchOrder(final String orderNumber) throws InterruptedException, KeyManagementException, NoSuchAlgorithmException {

		final By orderNumberSearchField = By
				.xpath("//label[contains(text(),'Order Number:')]/following::div[1]/div[1]/div[1]/input[1]");

		//final By searchBtn = By.xpath("(//span[contains(@id,'Button') and @data-dojo-attach-point='containerNode' and normalize-space(text())='Search'])[2]");

		// final By eMailSearchField = By
		// .xpath("//label[contains(text(),'Email
		// address:')]/following::div[1]/div[1]/div[1]/input[1]");
		// final By LastNameSearchField = By
		// .xpath("//label[contains(text(),'Last
		// Name:')]/following::div[1]/div[1]/div[1]/input[1]");
		// final By PostalCodeSearchField = By
		// .xpath("//label[contains(text(),'Postal
		// Code:')]/following::div[1]/div[1]/div[1]/input[1]");

		// final By findOrderBtn = By.xpath("//span[contains(text(),'Find
		// Order')]");
		// uploadToList(testInfoArrayText, "Test case [" + testCaseName + "]");

		logIn();

		/* Pat Type -> Credit card */
		// 1 -> Cash
		// 3 -> American Express-> 371449635398431
		// 5 -> Visa -> 4788250000028291
		// 7 -> MasterCard -> 5454545454545454
		// 8 -> 5856373196899715 - ?
		// 9 -> DISCOVERY -> 6011000995500000

		String orderNumberClean = null;

		/* Search use order number */
		if (orderNumber != null && !orderNumber.isEmpty()) {
			uploadToList(getTestInfo(), "Orer number [" + orderNumber + "]");
			// setText(orderNumberSearchField, orderNumber + Keys.ENTER);
			orderNumberClean = replaceStringRegex("([\\s'])", orderNumber, "");
			setText(orderNumberSearchField, orderNumberClean + Keys.ENTER);
			//click(searchBtn);

			// waitProcessing();
			// 961951700502

			// What status?
			if (isOrderStatusCorrect()) {

				makeScreenCapture("WSCCUI: order number [" + orderNumberClean + "] was found.", getWebDriver());
			} else {
				// Error msg
				String msg = "Unknown order status";
				LOG.error(msg);
				// status = false;
				throw new NoSuchElementException(msg);
			}
		}

		else {
			String msg = "Empty order number [" + orderNumberClean + "]";
			LOG.error(msg);
			// status = false;
			throw new NoSuchElementException(msg);
		}

		// /* Search use Email */
		// else if (eMail != null && !eMail.isEmpty()) {
		// setText(eMailSearchField, eMail + Keys.ENTER);
		// // waitProcessing();
		// // How many records?
		// // assertThat(howManRecods(), is(not(0)));
		// }
		//
		// /* Search use Last name of customer */
		// else if (lastName != null && !lastName.isEmpty()) {
		// setText(LastNameSearchField, lastName + Keys.ENTER);
		// // waitProcessing();
		// // How many records?
		// // assertThat(howManRecods(), is(not(0)));
		// }
		//
		/* Search use ZIP code */
		// else if (postalCode != null && !postalCode.isEmpty()) {
		// setText(PostalCodeSearchField, postalCode + Keys.ENTER);
		// // waitProcessing();
		// // How many records?
		// // assertThat(howManRecods(), is(not(0)));
		// }
		return this;
	}

	@Step("Fill the reasons of returns. Item bundles [{0}], Reason [{1}], subReason [{2}]. ")
	public ReturnsRefundsReplacement fillReasonReturn(final String bundlesItems, final String returnReason,
			final String returnSubReason, final String merchRecptPolic, final String returnActTyp)
			throws InterruptedException {

		final By createReturns = By.xpath("//a[contains(.,'Create Return/Replacement')]");
		final By nextButton = By.xpath("(//span[contains(text(),'Next')])[2]");
		final By saveBtn = By.xpath("//div/span[not(contains(@class,'Hidden'))]/span/span/span[text()='Save']");

		final By orderSummTitle = By.xpath("//span[contains(@class,'screenTitle') and text()='Order Summary']");
		final By addItemsReturnSubTitle = By
				.xpath("//span[contains(@class,'screenSubTitle') and text()='Add Items to Return']");

		if (findElement(orderSummTitle) != null) {
			click(createReturns);
			// waitProcessing();

			findElement(addItemsReturnSubTitle);

			// interation
			// selectItem("47719", "1", "NOT SATISFIED", "Did not like - Size",
			// "Return is required", "Refund");
			// add iteration how many correct Items in Bundle
			List<String> bandleList = Arrays.asList(bundlesItems.split(SPLIT_BY_BUNDLES));

			for (String bandle : bandleList) {

				selectItem(bandle, returnReason, returnSubReason, merchRecptPolic, returnActTyp);

			}

			click(saveBtn);
			// waitProcessing();

			click(nextButton);
			// waitProcessing();
		}

		return this;
	}

	@Step("Fill the Address options.")
	public ReturnsRefundsReplacement setAddressOpt() throws InterruptedException {

		final By nextButton = By.xpath("(//span[contains(text(),'Next')])[2]");
		final By addAddressSubTitle = By.xpath("//span[contains(@class,'screenSubTitle') and text()='Add Address']");
		// Return Addresses
		// Return Method

		if (findElement(addAddressSubTitle) != null) {

			makeScreenCapture("WSCCUI: Return Addresses Options.", getWebDriver());
			click(nextButton);
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was entered [Addresses Options].");
		}

		return this;
	}

	private final By ConfBtn = By.xpath("//span[contains(@id,'Button') and normalize-space(text())='Confirm']");

	@Step("Select [Refund] or [Replacement] scenario.")
	public ReturnsRefundsReplacement setRefundReplcOpt(final String returnActTyp, final String refundTransPolc,
			final String refundAmountTo, String storeNumber) throws InterruptedException {

		final By nextButton = By.xpath("(//span[contains(text(),'Next')])[2]");
		final By storeNumberInput = By
				.xpath("//label[contains(.,'Store Number')]/following::div[1]/div[1]/div[1]/input[1]");
		final By replacementSpan = By.xpath("//span[text()='Add Products to Replacement']");

		// Replacement action
		if (returnActTyp.equalsIgnoreCase("Replacement") || returnActTyp.toUpperCase().contains("REPLAC")) {
			// setReplcOpt(bundlesItems, storeNumber, orderType);
			// enterItems(bundlesItems, storeNumber, orderType);
			if (findElement(replacementSpan) != null) {

				// Store Number
				if (storeNumber != null && !storeNumber.isEmpty()) {

					setText(storeNumberInput, storeNumber);
				}

				click(nextButton);
				//setGiftOption("");
				click(ConfBtn);

				makeScreenCapture("WSCCUI: Replacement Summary.", getWebDriver());
				LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was entered [Replacement Options].");
			}
		}

		// Refaund action
		else if (returnActTyp.equalsIgnoreCase("Refund")) {
			setRefundOpt(refundTransPolc, refundAmountTo);
		}

		return this;
	}

	@Step("Set refund options. Refund Transfer Policy [{0}], refund expected amount to [{1}]")
	private ReturnsRefundsReplacement setRefundOpt(final String refundTransPolc, final String refundAmountTo)
			throws InterruptedException {
		// final By ConfBtn = By.xpath("//span[contains(@id,'Button') and
		// normalize-space(text())='Confirm']");
		// = By.xpath("//span[contains(text(),'Confirm')]");
		final By refundOpt = By.xpath("//span[contains(@id,'TitlePane') and normalize-space(text())='Refund Options']");
		final By refundTransPolcSelect = By.xpath(
				"//div/label[text()='Refund Transfer Policy']/parent::div/following-sibling::div/div/div/input[contains(@class,'ArrowButtonInner')]");
		final By refundTransPolcType = By
				.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
						+ refundTransPolc.trim().toUpperCase()
						+ "' and @role='option' and contains(@id,'FilteringSelect')]");
		// By.xpath("//div[contains(@id,'FilteringSelect') and @role='option'
		// and normalize-space(text())='"+ refundTransPolc + "']");

		final By refundExpectTenderSelect = By.xpath(
				"//div/label[text()='Refund Expected Amount To']/parent::div/following-sibling::div/div/div/input[contains(@class,'ArrowButtonInner')]");

		final By refundAmountToType = By
				.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
						+ refundAmountTo.trim().toUpperCase()
						+ "' and @role='option' and contains(@id,'FilteringSelect')]");
		// By.xpath("//div[contains(@id,'FilteringSelect') and @role='option'
		// and normalize-space(text())='"+ refundAmountTo + "']");

		final By overrideRefundTender = By
				.xpath("//label[text()='Override Refund Tender']/parent::div/preceding-sibling::div[last()]/input");
		// = By.xpath("//td[2]/div/div/div[2]/div/div/div[3]/div/input");
		// "//label[text()='Override Refund
		// Tender']/parent::div/preceding-sibling::div[last()]/input";
		final By payConfSubTitle = By
				.xpath("//span[contains(@class,'screenSubTitle') and text()='Payment Confirmation']");

		if (findElement(refundOpt) != null && findElement(payConfSubTitle) != null) {

			// Refund Options
			click(refundTransPolcSelect);
			/*
			 * Refund Transfer Policy: 1) Credit/Replace on Receipt 2)
			 * Credit/Replace now
			 */
			click(refundTransPolcType);

			if (refundAmountTo != null && !refundAmountTo.isEmpty()) {

				/*
				 * If checkbox [Override Refund Tender] exist on the page then
				 * ..
				 */
				if (findElements(overrideRefundTender) != 0
						&& getValueAttribute(overrideRefundTender, "aria-checked").equalsIgnoreCase("false")) {
					// Override Refund Tender
					click(overrideRefundTender);
					// click(By.xpath("//td[2]/div/div/div[2]/div/div/div[3]/div/input"));
				}

				// Refund Expected Amount To
				click(refundExpectTenderSelect);

				/*
				 * Refund Expected Amount To: 1) Credit Card 2) Merchandise Card
				 * 3) Gift Card 4) Refund Check
				 */
				click(refundAmountToType);
			}

			makeScreenCapture("WSCCUI: Selected Refund Options.", getWebDriver());

			click(ConfBtn);
			// waitProcessing();

			makeScreenCapture("WSCCUI: Return Summary.", getWebDriver());
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was entered [Refaund Options].");
		}

		return this;
	}

	final By replcNumber = By.xpath(
			"//div[contains(@id,'ContentPane') and contains(@class,'detailsPrimaryInfoPanel') and @role='region']//a[string-length(normalize-space(text()))=12 and normalize-space(@title)='Click on the link to view the replacement order']");

	@Step("Get return number from summary page.")
	public String getReturnNumber() throws InterruptedException {
		final By retunNumber = By.xpath(
				"//span[contains(@id,'widgets_Label') and @title='Click on the link to see the return order details ']");
		final By retrnSumTitle = By.xpath("//span[contains(@class,'screenTitle') and text()='Return Summary']");

		// = By.xpath("//span[text()='Replacement Order:']/following::a[1]");

		String returnOrder = null;
		// get Returns number
		// ([A-Z]\d{9})
		// String returnOrder = regExp("([A-Z]\\d{9})", getSRC());

		if (findElement(retrnSumTitle) != null) {

			returnOrder = getText(retunNumber);

			// if (findElements(replcNumber) == 1) {
			//
			// String replacementOrdNum = getText(replcNumber);
			// attachText("Replacement Order number.", replacementOrdNum);
			// uploadToList(testInfoArrayText, "Replacement order number [" +
			// replacementOrdNum + "]");
			// LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was found replacement order number [" +
			// replacementOrdNum + "]");
			// }

			uploadToList(getTestInfo(), "Return order number [" + returnOrder + "]");
			attachText("Return Order number.", returnOrder);
			makeScreenCapture("WSCCUI: Return order number.", getWebDriver());

			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Return order number is [" + returnOrder + "]");
		}
		return returnOrder;
	}

	@Step("Return/Replacement Number validation in Sterling.")
	public boolean refundReplacementSterlingValidation(final String RETURN_NUMBER, final String RETURN_ACT_TYPE,
			final String ORDER_NUMBER, final String BUNDLES) throws SQLException, InterruptedException, ClassNotFoundException {
		boolean status = false;
		int count = 0;
		long start;
		// final By replcNumber = By.xpath("//span[text()='Replacement
		// Order:']/following::a[1]");

		// Parsing SKU
		// List<String> bandleList =
		// Arrays.asList(BUNDLES.split(SPLIT_BY_BUNDLES));
		List<String> bandleList = Arrays.asList(replaceStringRegex("([\\s'])", BUNDLES, "").split(SPLIT_BY_BUNDLES));

		if (RETURN_ACT_TYPE.equalsIgnoreCase("Refund")) {

			for (String bandle : bandleList) {
				// String category = null;
				String sku = null;
				String skuQty = null;
				String[] item = bandle.split(SPLIT_BY_BUNDLE);

				sku = item[0];
				skuQty = item[1];

				// 1
				// boolean status = (new
				// SterlingDB()).isOrderNumberExistSterling(ORDER_NUMBER);
				status = (new SterlingDB().isRefundNumberExistSterling(RETURN_NUMBER, ORDER_NUMBER, sku, skuQty,
						"ORDER_NO"));
				count = 0;

				start = System.currentTimeMillis();
				while (!status) {
					if (count > TIME_SLEEP_MULTIPLIER_5) {
						break;
					} else {
						Thread.sleep(TIME_SLEEP_1000 * TIME_SLEEP_MULTIPLIER_10);
						status = (new SterlingDB().isRefundNumberExistSterling(RETURN_NUMBER, ORDER_NUMBER, sku,
								skuQty, "ORDER_NO"));
						count++;
					}
				}

				String msg = "Refund Order [" + RETURN_NUMBER + "] exist in Sterling [" + status + "]. Iteration ["
						+ count + "]. Duration [" + (System.currentTimeMillis() - start) / 1000 + "] sec.";
				LOG.info("TID["+Thread.currentThread().getId()+"] "+msg);
				uploadToList(getTestInfo(), msg);
				// 2
				// status = (new
				// SterlingDB().isRefundNumberExistSterling(RETURN_NUMBER,
				// ORDER_NUMBER, sku, sku_qty, "ORDER_NO"));

				// if(!status){
				// break;
				// }
			}

		}

		// else if(RETURN_ACT_TYPE.equalsIgnoreCase("Replacement")){
		else if (RETURN_ACT_TYPE.equalsIgnoreCase("Replacement") || RETURN_ACT_TYPE.toUpperCase().contains("REPLAC")) {

			// System.out.println(findElements(replcNumber));

			// String replacementOrdNum = getText(replcNumber);
			// attachText("Replacement Order number.", replacementOrdNum);
			// uploadToList(testInfoArrayText, "Replacement order number [" +
			// replacementOrdNum + "]");
			// LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was found replacement order number [" +
			// replacementOrdNum + "]");

			String replacementOrdNum = getText(replcNumber);
			attachText("Replacement Order number.", replacementOrdNum);
			uploadToList(getTestInfo(), "Replacement order number [" + replacementOrdNum + "]");
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was found replacement order number [" + replacementOrdNum + "]");

			for (String bandle : bandleList) {
				// String category = null;
				String sku = null;
				String skuQty = null;
				String[] item = bandle.split(SPLIT_BY_BUNDLE);

				sku = item[0];
				skuQty = item[1];
				// 1
				status = (new SterlingDB().isReplacementNumberExistSterling(replacementOrdNum, ORDER_NUMBER, sku,
						skuQty, "ORDER_NO"));
				count = 0;

				start = System.currentTimeMillis();
				while (!status) {
					if (count > TIME_SLEEP_MULTIPLIER_5) {
						break;
					} else {
						Thread.sleep(TIME_SLEEP_1000 * TIME_SLEEP_MULTIPLIER_10);
						status = (new SterlingDB().isReplacementNumberExistSterling(replacementOrdNum, ORDER_NUMBER,
								sku, skuQty, "ORDER_NO"));
						count++;
					}
				}

				String msg = "Refund Order exist in Sterling [" + status + "]. Iteration [" + count + "]. Duration ["
						+ (System.currentTimeMillis() - start) / 1000 + "] sec.";
				LOG.info("TID["+Thread.currentThread().getId()+"] "+msg);
				uploadToList(getTestInfo(), msg);
				// 2
				// status = (new
				// SterlingDB().isReplacementNumberExistSterling(replacementOrdNum,
				// ORDER_NUMBER, sku, sku_qty, "ORDER_NO"));

				// if(!status){
				// break;
				// }
			}
		}

		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Refund/Replacement validation in Sterling [" + status + "]");
		return status;
	}

	// @Step("Return Number validation in Sterling.")
	// public boolean returnSterlingValidation(final String RETURN_NUMBER, final
	// String ORDER_NUMBER, final String SKU, final String SKU_QTY, final String
	// GET_FIELD) throws SQLException{
	//
	// return (new SterlingDB().isRefundNumberExistSterling(RETURN_NUMBER,
	// ORDER_NUMBER, SKU, SKU_QTY, "ORDER_NO"));
	// }
	//
	// @Step("Replacement Order Number validation in Sterling.")
	// public boolean replacementSterlingValidation(final String RETURN_NUMBER,
	// final String ORDER_NUMBER, final String SKU, final String SKU_QTY, final
	// String GET_FIELD) throws SQLException{
	//
	// return (new SterlingDB().isRefundNumberExistSterling(RETURN_NUMBER,
	// ORDER_NUMBER, SKU, SKU_QTY, "ORDER_NO"));
	// }

	// LIMITME
	// private int howManRecods() {
	// /* How many orders was found */
	// String pageSource = getSRC();
	// // Records
	// String howManyOrders = cleanStringRegex("[.,]",
	// regExp("([0-9,]+)(?:\\sRecords)", pageSource));
	// LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was found [" + howManyOrders + "] records");
	//
	// makeScreenCapture("WSCCUI: Order Search.");
	// // assertThat(howManRecods(), is(not(0)));
	//
	// int records = Integer.parseInt(howManyOrders);
	//
	// if (records != 0) {
	// return records;
	// }
	//
	// else {
	// return 0;
	// }
	// // return null;
	// }

	private boolean isOrderStatusCorrect() throws InterruptedException {

		String statusOrd; // Delivered or Shipped or Partially Draft Return
							// Created
		boolean status = false;
		final By orderStatus = By.xpath("//td[2]/div/span/div/span");

		statusOrd = getText(orderStatus);
		uploadToList(getTestInfo(), "Status of found order [" + statusOrd + "]");

		if (statusOrd != null && !statusOrd.isEmpty()) {

			if (statusOrd.equalsIgnoreCase("Delivered") || statusOrd.equalsIgnoreCase("Shipped")
					|| statusOrd.equalsIgnoreCase("Invoiced")

					// Only for debugging PartiallyDraftReturnCreated
					|| statusOrd.equalsIgnoreCase("Partially Invoiced")
					|| statusOrd.equalsIgnoreCase("Partially Draft Return Created")
					|| statusOrd.equalsIgnoreCase("Partially Return Created")
					|| statusOrd.equalsIgnoreCase("Partially Return Received")
					|| statusOrd.equalsIgnoreCase("Draft Return Order In Process")
					|| statusOrd.equalsIgnoreCase("Partially Order Delivered")) {

				status = true;
				LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was found order [" + status + "], status [" + statusOrd + "].");

			} else {
				LOG.error("Was not found order status [Delivered] or [Shipped], but was [" + statusOrd + "].");
			}
		}

		return status;
	}

	private boolean selectItem(final String bandle, final String returnReason, final String returnSubReason,
			final String merchRecptPolic, final String returnActTyp) throws InterruptedException {

		boolean status = false;
		final By qtyReturn = By.xpath("//label[contains(text(),'Quantity:')]/following::div[1]/div[1]/div[1]/input[1]");
		final By returnReasonSelect = By.xpath(
				"//div/label[text()='Return Reason:']/parent::div/following-sibling::div/div/div/input[contains(@class,'ArrowButtonInner')]");

		final By reason = By
				.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
						+ returnReason.trim().toUpperCase()
						+ "' and @role='option' and contains(@id,'FilteringSelect')]");
		// By.xpath("//div[contains(@id,'FilteringSelect') and @role='option'
		// and normalize-space(text())='"+ returnReason.toUpperCase() + "']");

		// div[translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"+
		// returnReason.toLowwerCase() +"' and @role='option' and
		// contains(@id,'FilteringSelect')]

		// div[contains(@id,'FilteringSelect') and @role='option' and
		// normalize-space(text())='']
		// div[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"+returnReason.toUpperCase()+"')
		// and @role='option' and @id='FilteringSelect']
		// div/div[contains(translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'not
		// satisfied') and @role='option' and @id='FilteringSelect']

		// label[contains(translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'return
		// reason:') and @dojoattachpoint='compLabelNode']

		// label[translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='quantity:'
		// and @dojoattachpoint='compLabelNode']

		// label for="idx_form_NumberTextBox_0"
		// dojoattachpoint="compLabelNode">Quantity:</label>
		// label[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'quantity:')
		// and @dojoattachpoint='compLabelNode']
		// label[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'return
		// reason:') and @dojoattachpoint='compLabelNode']

		// label[contains(translate(.,'"+orderStat+" orders',
		// '"+orderStatLowCs+" orders'), '"+orderStatLowCs+"
		// orders')]/preceding::div[1]/input
		// label[contains(translate(.,'Draft orders', 'draft orders'), 'draft
		// orders')]

		final By subReasonSelect = By.xpath(
				"//div/label[text()='Return Sub Reason']/parent::div/following-sibling::div/div/div/input[contains(@class,'ArrowButtonInner')]");

		// div[translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"+
		// returnReason.toLowwerCase() +"' and @role='option' and
		// contains(@id,'FilteringSelect')]
		final By subReason = By
				.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
						+ returnSubReason.trim().toUpperCase()
						+ "' and @role='option' and contains(@id,'FilteringSelect')]");
		// By.xpath("//div[contains(@id,'FilteringSelect') and @role='option'
		// and normalize-space(text())='"+ returnSubReason + "']");

		final By returnActTypeSelect = By.xpath(
				"//div/label[text()='Return Action Type']/parent::div/following-sibling::div/div/div/input[contains(@class,'ArrowButtonInner')]");

		// final By returnActType =
		// By.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"+
		// returnActTyp.trim().toUpperCase()+ "' and @role='option' and
		// contains(@id,'FilteringSelect')]");

		final By returnActType = By
				.xpath("//div[contains(translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
						+ returnActTyp.trim().toUpperCase()
						+ "') and @role='option' and contains(@id,'FilteringSelect')]");
		// By.xpath("//div[contains(@id,'FilteringSelect') and @role='option'
		// and normalize-space(text())='"+ returnActTyp + "']");

		final By merchReceiptPolicySelect = By.xpath(
				"//div/label[text()='Merchandise Receipt Policy']/parent::div/following-sibling::div/div/div/input[contains(@class,'ArrowButtonInner')]");

		final By merchReceiptPolicy = By
				.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
						+ merchRecptPolic.trim().toUpperCase()
						+ "' and @role='option' and contains(@id,'FilteringSelect')]");
		// By.xpath("//div[contains(@id,'FilteringSelect') and @role='option'
		// and normalize-space(text())='"+ merchRecptPolic + "']");

		final By OverridePolicViolatnChbx = By
				.xpath("(//label[contains(text(),'Override Policy Violation')]/../preceding-sibling::div/input[1])[1]");

		// final By saveBtn =
		// By.xpath("//div/span[not(contains(@class,'Hidden'))]/span/span/span[text()='Save']");

		final By confirmationDialog = By.xpath("(//span[contains(@id,'Button') and normalize-space(text())='Ok'])[2]");
		// = By.xpath("//span[contains(text(),'Confirmation')]");

		final By confBtn = By.xpath("//span[contains(text(),'Ok')]");

		// String category = null;
		String sku = null;
		String qty = null;
		// String[] item = bandle.split(SPLIT_BY_BUNDLE);
		String[] item = replaceStringRegex("(\\s')", bandle, "").split(SPLIT_BY_BUNDLE);

		sku = item[0];
		qty = item[1];

		final By itemSelect = By
				.xpath("//div[3]/div/div/div/table/tbody/tr/td/div/div/span[contains(text(),'" + sku + "')]");

		String msg = "Could not find Item, SKU [" + sku + "] on the page.";


		// Thread.sleep(3000L); // REPLACE TO EXPLICITY

		// if(findElements(itemSelect) == 0){
		// waitUntilElementNotPresent(processingLocator);
		// }
		// else if (findElements(itemSelect) != 0) {
		// TESTME
		try {
			findElement(itemSelect);
		} catch (Exception e) {
			LOG.warn(msg);
			waitUntilElementNotPresent(processingLocator);
		}

		if (findElements(itemSelect) != 0) {

			click(itemSelect);

			// Quantity
			setText(qtyReturn, qty + Keys.TAB);

			// Return Reason:
			click(returnReasonSelect);
			click(reason);

			// Return Sub Reason
			click(subReasonSelect);
			click(subReason);

			// Merchandise Receipt Policy
			click(merchReceiptPolicySelect);
			click(merchReceiptPolicy);

			// Return Action Type
			click(returnActTypeSelect);
			click(returnActType);

			// click(saveBtn);
			// Override Policy Violation
			click(OverridePolicViolatnChbx);

			if (findElements(confirmationDialog) != 0) {

				click(confBtn);
				// waitProcessing();

				LOG.info("TID["+Thread.currentThread().getId()+"] "+"WSCCUI: Add Items [" + sku + "] to return, qty [" + qty + "].");
			}

			makeScreenCapture("WSCCUI: Add Items [" + sku + "] to return, qty [" + qty + "].", getWebDriver());
			status = true;
		}

		else {
			// Error
			LOG.error(msg);
			throw new IllegalStateException(msg);
		}

		return status;
	}
}// END of CLASS
