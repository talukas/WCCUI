package com.wsccui.funcsuite;

import static com.wsccui.ConstatntWSCCUI.*;
import static core.GlobalConstatnt.*;
import static core.ThreadsStore.getTestInfo;
import static core.ThreadsStore.getWebDriver;
import static core.util.BaseConfig.LANDING_PAGE;
import static core.util.CustomAssertThat.assertThat;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.is;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import core.db.dtc.AS400DB;
import core.db.sterling.SterlingDB;
import core.exception.NullTaxesAs400Exceptions;
import static core.util.ObjectSupplier.$;
import com.wsccui.exception.UnknowTypeCreditCardException;
import com.wsccui.interfaces.WccuiFunctional;
import com.wsccui.pom.SummaryPage;
import com.wsccui.template.BasePageWsccui;
import com.wsccui.util.Util;
import static core.util.BaseConfig.getProperty;
import ru.yandex.qatools.allure.annotations.Step;

public class CreateOrder extends BasePageWsccui implements WccuiFunctional {

	static final Logger LOG = Logger.getLogger(CreateOrder.class.getName());
	private SummaryPage summaryPage = new SummaryPage();
	private double orderTotal;
	private boolean isCustomerID;
	private double taxcesRateBillingTo;
	private String draftOrderNumber;
	
	/* Constructor */
	// Read properties not implement yet
	// public WSCCUIMainPage() {
	//
	// }
	
	private final By linkCreateOrder = By.xpath("//a[contains(text(),'Create Order')]");
	private final By linkL_ContWithUnregCust = By.xpath("//a[contains(text(),'Continue With Unregistered Customer')]");
	private final By conceptArrow = By.xpath("//div[2]/div/div[2]/div[2]/div/div/input");

	/* Search Order */
	private final By linkSearchOrder = By.xpath("//a[contains(text(),'Order Search')]");
	private final By inputSearchOrder = By.xpath("//td[1]/div/div[2]/div[2]/div[1]/div/input");
	private final By founOrdeNumber = By.xpath("//td[1]/span");
	private final By founStOrder = By.xpath("//td[2]/div/span/div/span");

	/* Create order */
	@Step("WSCCUI. Select concept ...")
	@Override
	public CreateOrder createDraftOrder(final String CONCEPT) throws InterruptedException, KeyManagementException, NoSuchAlgorithmException {

		/* Open start page */
		logIn();

		click(linkCreateOrder);

		{
			selectVisibleTextFromDropDownList(conceptArrow, selectConcept(CONCEPT));
			makeScreenCapture("Select concept page.", getWebDriver());
			click(By.xpath("//span[contains(text(),'Apply')]"));
		}
		return this;
	}

	@Step("WSCCUI. CDM Response. Select Customer criteria ...")
	@Override
	public CreateOrder selectCustomerCriteria(final String customerID, final String firstName, final String lastName, final String middleName,
			final String eMail, final String address_1, final String address_2, final String postalCode,
			final String dayTelephone, final String eveningTelephone) throws InterruptedException {

		final By customerIdInputField = By.xpath("//div[contains(@id,'isccs_editors') and contains(@class,'dijitVisible')]//label[normalize-space(text())='Customer ID:']/following::input[position()=1 and contains(@id,'TextBox') and contains(@class,'InputInner')]");
		final By searchBtn = By.xpath("//div[@class='isccsCenter']//span[contains(@id,'Button') and normalize-space(text())='Search']");
		final By cmdAppErrorMsg = By.xpath("//div[@class='messageTitles' and contains(text(),'CDM application is not available.')]");
		final By noItemsDisplay = By.xpath("//div[ contains(@class,'dijitContentPaneSingleChild') and contains(@class,'idxContentPane') and not(contains(@class,'scHidden')) and @uid='pnlListContainer']//div[@class='gridxBodyEmpty' and @role='alert' and normalize-space(text())='No items to display']");
		final By customerIdFoundLabel = By.xpath("//span[@class='blockComponent' and normalize-space(text())='982124096']");
		
		final By addEditShippingButton = By.xpath("//div[contains(@id,'isccs_editors') and contains(@class,'dijitVisible')]//span[@data-ibmsc-uid='lblAddresType' and normalize-space(text())='Ship To']/following::a[position()=1 and @data-ibmsc-uid='lnkEditAddress' and (normalize-space(text())='Edit' or normalize-space(text())='Add')]");
		
		
		if(customerID != null && !customerID.trim().isEmpty()){
			/**
			 * CDM Response.
			 * 
			 * Customer ID:
			 * QA - 986422649,
			 * MOU - 985915100,
			 * ITG - 986312568.
			 * 
			 **/
			
			this.isCustomerID = true;
			
			setText(customerIdInputField, customerID);
			click(searchBtn);
			
			// No items to display
			assertThat(waitElementS(noItemsDisplay, ""),
					describedAs("CDM. CDM Response. Expected to find customer ID ["+customerID+"], not [No items to display]", is(0)));
			
			// Error
			// CDM application is not available. Please proceed as Unregistered customer
			assertThat(waitElementS(cmdAppErrorMsg, ""),
					describedAs("CDM. CDM Response. Expected not visible message. Application is NOT responde.. Customer ID ["+customerID+"]", is(0)));
			
			// Customer was found
			assertThat(waitElementS(customerIdFoundLabel, ""),
					describedAs("CDM. CDM Response. Expected found customer ID ["+customerID+"]", is(1)));
			
			makeScreenCapture("Customer ID details", getWebDriver());
			
			/* Click on Billing To link */
			click(addEditBillingButton);

			addEditAddressInfo(firstName, lastName, middleName, eMail, address_1, address_2, postalCode, dayTelephone,
					eveningTelephone);
			
			makeScreenCapture("Billing to page.", getWebDriver());
	
			/* Click on Shipping To link */
			click(addEditShippingButton);

			addEditAddressInfo(firstName, lastName, middleName, eMail, address_1, address_2, postalCode, dayTelephone,
					eveningTelephone);
			
			makeScreenCapture("Shipping to page.", getWebDriver());
			
			// LAST step: click - Next Btn
			click(nextButton);
		}
		else{
			makeScreenCapture("Select user type.", getWebDriver());
			click(linkL_ContWithUnregCust);	
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Customer ID was NOT provided.");
		}
		
		
		return this;
	}


	private final By nextButton = By.xpath("(//span[contains(text(),'Next')])[2]");
	private final By draftOrderNumberLabel = By.xpath("//span[@class='tabLabel' and @role='tab' and @aria-selected='true' and contains(text(),'Draft Order')]");

	@Step("WSCCUI. Processing Items ...")
	@Override
	public CreateOrder enterItem(final String bundlesItems, final String storeNumber, final String orderType)
			throws NumberFormatException, SocketException, UnknownHostException, Exception {
		/* Fetch draft order number */

		String draftOrderNumber = getText(draftOrderNumberLabel).replaceAll("(\\D+)", "");
		//(\\D+)(?:[\\d]+)
		this.draftOrderNumber = (draftOrderNumber != null && !draftOrderNumber.isEmpty() ? draftOrderNumber : "N/A");
		uploadToList(getTestInfo(), "Draft Order: ["+ this.draftOrderNumber +"]");
		orderTotal = enterItems(bundlesItems, storeNumber, orderType);
		uploadToList(getTestInfo(), "Order total: ["+ orderTotal +"]");
		attachText("Cookies", getAllCookies());

		return this;
	}

	private final By addEditBillingButton = By.xpath("//div[contains(@id,'isccs_editors') and contains(@class,'dijitVisible')]//span[@data-ibmsc-uid='lblAddresType' and normalize-space(text())='Bill To']/following::a[position()=1 and @data-ibmsc-uid='lnkEditAddress' and (normalize-space(text())='Edit' or normalize-space(text())='Add')]");
			//By.xpath("//div[2]/span/div/div[1]/div[1]/div[1]/div[2]/div/div/a");

	@Step("WSCCUI. Enter order address ...")
	@Override
	public CreateOrder setBillToAdress(final String firstName, final String lastName, final String middleName,
			final String eMail,
			final String address_1, final String address_2, final String postalCode, final String dayTelephone,
			final String eveningTelephone) throws InterruptedException, ClassNotFoundException, SQLException, NullTaxesAs400Exceptions {

		/* Skip all actions if was use customer ID */
		if(!isCustomerID){
		/* Click on Billing To link */
		click(addEditBillingButton);
		
		//Fetch Taxes
		taxcesRateBillingTo = $(AS400DB.class).getTax(postalCode); 
		
		addEditAddressInfo(firstName, lastName, middleName, eMail, address_1, address_2, postalCode, dayTelephone,
				eveningTelephone);

		makeScreenCapture("Billing to page.", getWebDriver());
		click(nextButton);
		}
		else{
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Fill the customer info (address) was skipped.");
		}
		
		return this;
	}

	@Step("WSCCUI. Set custom properties ...")
	public CreateOrder setCustomProperties(final String customerPreference, final String customerType,
			final String customerID) {

		final By custPrefSelect = By.xpath(
				"//label[normalize-space(text())='Customer Preference:']/following::input[position()=1 and contains(@class,'ArrowButtonInner') and @role='presentation']");
		final By customerTypeSelect = By.xpath(
				"//label[contains(normalize-space(text()),'Customer Type:')]/following::input[position()=1 and contains(@class,'InputField') and @role='presentation']	");
		final By EmpOrBussinesIdInput = By.xpath(
				"//div[contains(@id,'TextBox') and not(contains(@class,'scHidden')) and contains(@widgetid,'TextBox')]//label[normalize-space(text())='ID:']/following::input[position()=1 and contains(@id,'TextBox') and contains(@class,'InputInner') and not(@disabled)]");
		
		/* Skip all actions if was use customer ID */
		if(!isCustomerID){
		
		if (customerPreference != null && !customerPreference.isEmpty()) {

			selectVisibleTextFromDropDownList(custPrefSelect, customerPreference);
		}

		// Customer Preference:
		// Default value 'Consumer'
		// Phone
		if (customerType != null && !customerType.isEmpty() && !customerType.equalsIgnoreCase("Consumer")
				&& customerID != null && !customerID.isEmpty()) {

			selectVisibleTextFromDropDownList(customerTypeSelect, customerType);
			setText(EmpOrBussinesIdInput, customerID);
		}
	}
	else{
		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Fill the customer info was skipped.");
	}
		
		return this;
	}

	private final By addPaymentType = By.xpath("//td[1]/div/div[1]/span[2]/span");

	private final By amountTotal = By.xpath(
			"//label[@dojoattachpoint='compLabelNode' and normalize-space(text())='Amount to pay:']/following::span[1]");
	// = By.xpath("//td[1]/div/div[1]/div[2]/div[2]/div[1]/div/span");
	private final By paidOrder = By
			.xpath("//span[contains(normalize-space(text()),'Sufficient funds have been provided')]");
	// By.xpath("//span[normalize-space(text())='Sufficient funds have been
	// provided.']");
	private final By confirmButton = By.xpath("//div[4]/span[3]/span");


//				/* Pat Type -> Credit card */
//				// 1 -> Cash
//				// 3 -> American Express-> 371449635398431
//				// 5 -> Visa -> 4788250000028291
//				// 7 -> MasterCard -> 5454545454545454
//				// 8 -> 5856373196899715 - ?
//				// 9 -> DISCOVERY -> 6011000995500000

	@Step("WSCCUI. Set payment type ...")
	@Override
	public CreateOrder setPaymentTypeAndPromo(final String creditCardInfo, final String giftCardInfo,
			final String loyaltyRewardsCert, final String promCode, final String cashInfo) throws InterruptedException, UnknowTypeCreditCardException {
		
		boolean isUseCredirCard = false;
		boolean isUseGiftCard = false;
		boolean isUseCash = false;
		boolean isUseCashEsends = false;
		boolean isUseDueBill = false;
		boolean isUseLoyaltyRewardsCert = false;
		boolean isUsePromoCode = false;
		int howManyPayments = 0; 

		// For order must be no less than a 1 payment
		// Type
		Double totalAmount;
		Double dueBill = Double.valueOf(0.0);
		Double cash = Double.valueOf(0.0);
		DecimalFormat df = new DecimalFormat("#########.00");

		
		
		//old
		String amountDueRegexSeparation = "(?:\\$)([0-9,\\s]+)?\\.(\\d{2})";
		String totalAmountRegex = TOTAL_AMOUNT_PAYMENT;
				//"(?:\\$)([0-9,\\s]+?\\.\\d{2})";
		
		String dollarsAmount = null;
		String centsAmount = null;
		
		String creditCardNumber = null;
		String creditCardExpire = null;

		String giftCardNumber = null;
		String giftCardPIN = null;

		if (creditCardInfo != null && !creditCardInfo.isEmpty() && creditCardInfo.contains(SPLIT_BY_BUNDLE)) {

			creditCardNumber = creditCardInfo.split(SPLIT_BY_BUNDLE)[0];
			creditCardExpire = creditCardInfo.split(SPLIT_BY_BUNDLE)[1];
			isUseCredirCard = true;

			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was parsed 'Credit card' info [" + creditCardInfo + "]");
		} else {
			LOG.warn(WRN_MSG + "Credit card]");
		}

		if (giftCardInfo != null && !giftCardInfo.isEmpty() && giftCardInfo.contains(SPLIT_BY_BUNDLE)) {
			giftCardNumber = giftCardInfo.split(SPLIT_BY_BUNDLE)[0];
			giftCardPIN = giftCardInfo.split(SPLIT_BY_BUNDLE)[1];
			isUseGiftCard = true;

			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was parsed 'Gift card' info [" + giftCardInfo + "]");
		} else {
			LOG.warn(WRN_MSG + "Gift card]");
		}

		if (cashInfo != null && !cashInfo.isEmpty() && cashInfo.contains(SPLIT_BY_BUNDLE)) {
			isUseDueBill = $(Util.class).isTrueOrFalse(cashInfo.split(SPLIT_BY_BUNDLE)[0]);
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was parsed 'Due bill' [" + isUseDueBill + "]");
			if (isUseDueBill) {
				howManyPayments++;
			}

			isUseCash = $(Util.class).isTrueOrFalse(cashInfo.split(SPLIT_BY_BUNDLE)[1]);
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was parsed 'Cash' [" + isUseCash + "]");
			if (isUseCash) {
				howManyPayments++;
			}

			isUseCashEsends = $(Util.class).isTrueOrFalse(cashInfo.split(SPLIT_BY_BUNDLE)[2]);
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was parsed 'Cash Esends' [" + isUseCashEsends + "]");
			if (isUseCashEsends) {
				howManyPayments++;
			}
		} else {
			LOG.warn(WRN_MSG + "CASH]");
		}

		if (loyaltyRewardsCert != null && !loyaltyRewardsCert.isEmpty()) {
			isUseLoyaltyRewardsCert = true;
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was parsed 'Loyalty Rewards Certificate' info [" + loyaltyRewardsCert + "]");
		} else {
			LOG.warn(WRN_MSG + "Loyalty Rewards Certificate]");
		}

		if (promCode != null && !promCode.isEmpty()) {
			isUsePromoCode = true;
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was parsed 'Promotion code' info [" + loyaltyRewardsCert + "]");
		} else {
			LOG.warn(WRN_MSG + "Promotion Code]");
		}

		waitUntilElementPresent(processingLocator, TIME_WAIT_MIN_5, TIME_SLEEP_500);

		/* 01. Enter Promo code */
		if (isUsePromoCode) {

			setText(By.xpath(".//label[contains(text(),'Promotion code:')]/following::div[1]/div[1]/div[1]/input"),
					promCode);
			click(By.xpath("//span[contains(text(),'Apply')]"));

			makeScreenCapture("Enter Promotion code", getWebDriver());
		}
		else{
			LOG.warn("Promo code will not entered (Empty or incorrect)");
		}

		/*02. Enter GiftCard*/
		if (findElements(paidOrder) == 0 && isUseGiftCard) {
			
			addEgiftCard(giftCardNumber, giftCardPIN);
		}
		
		
		/*03. Enter Loyalty Rewards Certificate*/
		if (findElements(paidOrder) == 0 && isUseLoyaltyRewardsCert) {
			
			addLRC(loyaltyRewardsCert);
		}
		
		/*04. Enter Due Bill*/
		if (findElements(paidOrder) == 0 && isUseDueBill) {
			
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Total amount: [" + regExp(amountDueRegexSeparation, getText(amountTotal), 0) + "]");
			
			/*Parse actual total amount from the Page*/
			dollarsAmount = replaceStringRegex("(,)", regExp(amountDueRegexSeparation, getText(amountTotal), 1), "");
			centsAmount = regExp(amountDueRegexSeparation, getText(amountTotal), 2);
			
			totalAmount = Double.parseDouble(replaceStringRegex("(,)", regExp(totalAmountRegex, getText(amountTotal), 1), ""))/(double)howManyPayments;
		
			dueBill = totalAmount;
			addDueBill(df.format(totalAmount).toString());
			howManyPayments--;
		}
		
		/*05. Enter Cash Esends*/
		if (findElements(paidOrder) == 0 && isUseCashEsends) {
			
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Total amount: [" + regExp(amountDueRegexSeparation, getText(amountTotal), 0) + "]");
			
			/*Parse actual total amount from the Page*/
			dollarsAmount = replaceStringRegex("(,)", regExp(amountDueRegexSeparation, getText(amountTotal), 1), "");
			centsAmount = regExp(amountDueRegexSeparation, getText(amountTotal), 2);
			
			totalAmount = Double.parseDouble(replaceStringRegex("(,)", regExp(totalAmountRegex, getText(amountTotal), 1), ""))/(double)howManyPayments;
		
			cash += totalAmount;
			
			addCashEsends(df.format(totalAmount).toString());
			howManyPayments--;
		}
		
		
		/*06. Enter Cash*/
		if (findElements(paidOrder) == 0 && isUseCash) {
			
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Total amount: [" + regExp(amountDueRegexSeparation, getText(amountTotal), 0) + "]");
			
			/*Parse actual total amount from the Page*/
			dollarsAmount = replaceStringRegex("(,)", regExp(amountDueRegexSeparation, getText(amountTotal), 1), "");
			centsAmount = regExp(amountDueRegexSeparation, getText(amountTotal), 2);
			
			totalAmount = Double.parseDouble(replaceStringRegex("(,)", regExp(totalAmountRegex, getText(amountTotal), 1), ""))/(double)howManyPayments;
		
			cash += totalAmount;
			addCash(df.format(totalAmount).toString());
			howManyPayments--;
		}
		
		/*07. Enter Credit Card*/
		if (findElements(paidOrder) == 0 && isUseCredirCard) {
			
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Total amount: [" + regExp(amountDueRegexSeparation, getText(amountTotal), 0) + "]");
			
			final String[] expiredParsed =  $(Util.class).monthYearParsExpire(creditCardExpire);
			final String monthCcExpire = expiredParsed[0];
			final String yearCcExpire = expiredParsed[1];
			String creditCardNumClean = replaceStringRegex("([\\s'])", creditCardNumber, "");
			
			/*Parse actual total amount from the Page*/
			dollarsAmount = replaceStringRegex("(,)", regExp(amountDueRegexSeparation, getText(amountTotal), 1), "");
			centsAmount = regExp(amountDueRegexSeparation, getText(amountTotal), 2);
			
			if(isTotalAmountCorrect("5", dollarsAmount + "." + centsAmount)){
				addCreditCard(creditCardNumClean, monthCcExpire, yearCcExpire);
			}
			else{
				dueBill += Double.parseDouble("0."+centsAmount);
				addDueBill(df.format(dueBill).toString());
				addCreditCard(creditCardNumClean, monthCcExpire, yearCcExpire);
			}
		}
		
		/*Last check*/
		if (findElements(paidOrder) == 0 ) {
			//|| findElements(amountTotal) != 0
			//LOG.info("TID["+Thread.currentThread().getId()+"] "+"Total amount: [" + regExp(amountDueRegexSearation, getText(amountTotal), 0) + "]");
			
			if(isUseCash){
				totalAmount = Double.parseDouble(replaceStringRegex("(,)", regExp(totalAmountRegex, getText(amountTotal), 1), ""))/(double)howManyPayments;
				cash += totalAmount;
				addCash(df.format(cash).toString());
			}
			else{
				addCash(replaceStringRegex("(,)", regExp(totalAmountRegex, getText(amountTotal), 1), ""));
			}
		}

		/* Pricing Validation */
		{
			// Subtotal
			final By subtotalPriceLabel = By.xpath("//div[contains(@id,'ContentPane') and @uid='contentSalesOrderTotal']//label[normalize-space(text())='Subtotal:']/following::span[position()=1 and @class='scDataValue' and contains(text(),'$')]");
			final double subtotalPriceActual = Double.parseDouble(getText(subtotalPriceLabel).replaceAll(CHARS_NOT_ALLOWED, ""));
			uploadToList(getTestInfo(), "Subtotal actual ["+ subtotalPriceActual +"]");
			assertThat(subtotalPriceActual, describedAs("Subtotal actual price", is(orderTotal)));
			
			/* ============================= */
			// Surcharges
			//final By SurchargesLabel = By.xpath("//div[contains(@id,'ContentPane') and @uid='contentSalesOrderTotal']//label[normalize-space(text())='Surcharges:']/following::span[position()=1 and @class='scDataValue' and contains(text(),'$')]");
			//final String SurchargesPriceActual = getText(SurchargesLabel).replaceAll(CHARS_NOT_ALLOWED, "");
			//TODO
			//assertThat(SubtotalPriceActual, describedAs("Subtotal price", is(orderTotal)));
			/* ============================= */
			
			// Discounts
			//final By DiscountsLabel = By.xpath("//div[contains(@id,'ContentPane') and @uid='contentSalesOrderTotal']//label[normalize-space(text())='Discounts:']/following::span[position()=1 and @class='scDataValue' and contains(text(),'$')]");
			//final String DiscountsPriceActual = getText(SurchargesLabel).replaceAll(CHARS_NOT_ALLOWED, "");
			//TODO
			//assertThat(SubtotalPriceActual, describedAs("Subtotal price", is(orderTotal)));
			/* ============================= */
			
			// Shipping charges
			final By shippingChargesLabel = By.xpath("//div[contains(@id,'ContentPane') and @uid='contentSalesOrderTotal']//label[normalize-space(text())='Shipping charges:']/following::span[position()=1 and @class='scDataValue' and contains(text(),'$')]");
			double shippingChargesPriceActual = Double.parseDouble(getText(shippingChargesLabel).replaceAll(CHARS_NOT_ALLOWED, ""));
			uploadToList(getTestInfo(), "Shipping charges actual ["+ shippingChargesPriceActual +"]");
			//+assertThat(subtotalPriceActual, describedAs("Shipping charges", is(getShipCharges(orderTotal))));
			
			// Taxes
			
			// Total
			final By totalLabel = By.xpath("//div[contains(@id,'ContentPane') and @uid='contentSalesOrderTotal']//label[normalize-space(text())='Shipping charges:']/following::span[position()=1 and @class='scDataValue' and contains(text(),'$')]");
			final String totalPriceActual = getText(totalLabel).replaceAll(CHARS_NOT_ALLOWED, "");
			//assertThat(totalPriceActual, describedAs("Subtotal price", is(getShipCharges(orderTotal))));
			//assertThat(totalPriceActual, describedAs("Taxes", allOf(not(),is())));
			makeScreenCapture("Total amount validation.", getWebDriver());
		}
		
		
		click(confirmButton);
		makeScreenCapture("Payment Confirmation. Confirmation.", getWebDriver());
		
		return this;
	}

	@Step("WSCCUI. Get Order number ...")
	@Override
	public String getOrderNumber() throws InterruptedException {

		return findOrderInSRC(ORDER_NUMBER_REGEX);
	}

	private final By userMenu = By.xpath("//li[5]/a");
	private final By logOut = By.xpath("//td[contains(.,'Logout')]");

	@Step("WSCCUI. Log out ...")
	@Override
	public CreateOrder logOut() throws InterruptedException {

		// click(applyButton, "Easy Click.");
		{
			click(userMenu);
			makeScreenCapture("Logout. Before alert.", getWebDriver());
			click(logOut);
			// alertAccept();
		}

		getTitle(getProperty(LANDING_PAGE));
		makeScreenCapture("Logout.", getWebDriver());

		return this;
	}

	@Override
	public CreateOrder setOrderSearchCriteria() {
		return this;
	}

	@Step("WSCCUI. Get order [{0}] status ...")
	@Override
	public String getOrderStatus(final String orderNumber) throws InterruptedException {
		String founOrderNumber;
		String founStatusOrder = null;

		{
			click(linkSearchOrder);
			setText(inputSearchOrder, orderNumber + Keys.ENTER);
		}

		// What was found - assert
		{
			makeScreenCapture("Actual order what was found.", getWebDriver());
			founOrderNumber = getText(founOrdeNumber);
			assertThat(orderNumber,
					describedAs("Expected titel " + getProperty(LANDING_PAGE), is(founOrderNumber)));

			founStatusOrder = getText(founStOrder);
		}

		return founStatusOrder;
	}

	@Override
	public CreateOrder chooseVariation() {

		return this;
	}

	final By fullfilmetSubTitle = By
			.xpath("//span[contains(@class,'screenSubTitle') and normalize-space(text())='Fulfillment Summary']");

	@Step("WSCCUI. Enter Gift options ...")
	@Override
	public CreateOrder setGiftOptions(final String giftOptions) throws InterruptedException {

		findElement(fullfilmetSubTitle);

		if (giftOptions != null && !giftOptions.isEmpty()) {

			setGiftOption(giftOptions);

			/* 12. Validate page */
			findElement(fullfilmetSubTitle);
		} else {
			LOG.warn("Can't provide enter Gift options, it's [Empty]");
		}

		return this;
	}

	@Step("WSCCUI. Change Carrier Service ...")
	@Override
	public CreateOrder changeCarrierService(final String bundlesItems) throws InterruptedException {

		findElement(fullfilmetSubTitle);
		String[] bandleList = bundlesItems.split(SPLIT_BY_BUNDLES);
		int countLine = 1;
		for (String item : bandleList) {

			setCarrierService(item, countLine);

			/* Next Line */
			countLine++;
		}

		findElement(fullfilmetSubTitle);

		return this;
	}

	@Step("WSCCUI. Add new [Ship To] address ...")
	@Override
	public CreateOrder changeAddress(final String bundlesLines) throws InterruptedException {

		By lineNumberChkBox;
		By changeAdrBtn;
		final By unavailableOrderLines = By.xpath(
				"//span[contains(@id,'Dialog') and @role='heading' and normalize-space(text())='Unavailable Order Lines']");
		final By acceptUnavailableOrderLines = By
				.xpath("//span[contains(@id,'Button') and normalize-space(text())='Apply to Available Lines']");

		findElement(fullfilmetSubTitle);

		if (bundlesLines != null && !bundlesLines.isEmpty()) {
			String[] bandleList = bundlesLines.split("&");
			String[] item = null;

			for (String newShipTo : bandleList) {

				/**
				 * [0] - #Line, [1] - First name, [2] - Last name, [3] - Middle
				 * name, [4] - eMail, [5] - address_1, [6] - address_2, [7] -
				 * DayTel, [8] - EvenTel, [9] - ZIP,
				 */

				item = newShipTo.split(SPLIT_BY_BUNDLE);

				if (item.length == 10
				) {

					lineNumberChkBox = By
							.xpath("//div[contains(@id,'FulfillmentSummaryDetailsScreen') and contains(@class,'dijitVisible') and contains(@widgetid,'FulfillmentSummaryDetailsScreen')]//td[contains(@class,'gridxCell') and @role='gridcell' and not(contains(@style,'display: none'))]//span[normalize-space(text())='"
									+ Integer.parseInt(item[0]) + "']");


					/* 1. Select (click) Order Line to change Ship To */
					click(lineNumberChkBox);

					changeAdrBtn = By
							.xpath("//div[contains(@id,'OrderEditor') and contains(@class,'dijitVisible')]//div[contains(@id,'FulfillmentSummaryDetailsScreen') and contains(@class,'dijitVisible') and contains(@widgetid,'FulfillmentSummaryDetailsScreen')]//td[contains(@class,'gridxCell') and @role='gridcell' and not(contains(@style,'display: none'))]//span[normalize-space(text())='"
									+ Integer.parseInt(item[0])
									+ "']/preceding::span[contains(@id,'Button') and normalize-space(text())='Change Address'][1]");
					
					/* 2. Click on the Button [Change Address] */
					click(changeAdrBtn);

					/* 3. Fill all fields [Contact Information], [Address] */
					addEditAddressInfo(item[1], item[2], item[3], item[4], item[5], item[6], item[9], item[7], item[8]);

				} else {

					LOG.warn("Can't add new [Ship To] address. Wrong qty of artifacts. Expected [10], but was ["
							+ item.length + "] - [" + newShipTo + "]");
				}
			}

			findElement(fullfilmetSubTitle);

		} else {
			LOG.warn("Can't add new [Ship To] address. It's [null]");
		}

		/* Handle Unavailable order lines */
		if (findElements(unavailableOrderLines) != 0) {
			LOG.warn("Was found 'Unavailable Order Lines' popup.");
			makeScreenCapture("Handle: 'Unavailable Order Lines' popup", getWebDriver());
			click(acceptUnavailableOrderLines);
		}

		/* Only this method has Action press button [NEXT] - Important! */
		click(nextButton);

		return this;
	}

	@Step("WSCCUI. Enter Instructions ...")
	@Override
	public CreateOrder setInstructions() {

		/**
		 * Instruction Type : Instruction code : Instruction Text
		 */
		String tmp = "";

		return this;
	}

	@Override
	public CreateOrder setShipmentHolds() {

		return this;
	}



	@Step("WSCCUI. Check 'Resolve Holds' and handle for order number [{0}] ...")
	public void handleResolveHolds(final String ORDER_NUMBER) {

		summaryPage.resolveHolds(ORDER_NUMBER);
	}
	
	
	
	
	private Boolean landingPage;

	/* Local Methods for service */
	// LIMITME
	private boolean setCarrierService(final String ITEM, int countLine) throws InterruptedException {
		boolean status = false;
		final By carrierSecriceSelect = By.xpath(
				"//div[contains(@id,'Dialog')]//div[not(contains(@class,'scHidden')) and contains(@id,'FilteringSelect')]//input[contains(@class,'ArrowButtonInner')]");
		final By applyBtn = By.xpath(
				"//div[contains(@id,'Dialog')]//span[contains(@id,'form_Button') and normalize-space(text())='Apply']");

		final By messageConfirmation = By
				.xpath("//div[@class='messageTitles' and contains(text(),'Order updated successfully.')]");

		By carrierServiceBtn;

		String sku = null;
		String lineNumber;
		String carrierService = null;

		final String[] item = ITEM.split(SPLIT_BY_BUNDLE);

		if (item.length == 4) {


			sku = replaceStringRegex(" ", item[0], "");
			lineNumber = Integer.toString(countLine);
			carrierServiceBtn = By.xpath(
					"//span[normalize-space(text())='Regular']/following::span[contains(@id,'Button') and normalize-space(text())='Change Carrier Service']");

			carrierService = replaceStringRegex(" ", item[3], "");
			By lineNumberChkBox;

			if (lineNumber != null && !lineNumber.isEmpty() && carrierService != null && !carrierService.isEmpty()
					&& carrierService.equalsIgnoreCase("RUSH")) {

				// 1. Click to need line
				lineNumberChkBox= By.xpath(
								"//div[contains(@id,'FulfillmentSummaryDetailsScreen') and contains(@class,'dijitVisible') and contains(@widgetid,'FulfillmentSummaryDetailsScreen')]//td[contains(@class,'gridxCell') and @role='gridcell' and not(contains(@style,'display: none'))]//span[normalize-space(text())='"
										+ lineNumber + "']");

				click(lineNumberChkBox);
				click(carrierServiceBtn);

				// 2. On PopUp window Select Carrier Service
				// getValueAttribute(carrierSecriceSelect, "value");
				// click(carrierSecriceSelect);
				// click(rushCarrierService);

				selectVisibleTextFromDropDownList(carrierSecriceSelect, "Rush");

				/* Make */
				makeScreenCapture("Carrier service [Rush]. SKU [" + sku + "]. Line# [" + lineNumber + "]", getWebDriver());

				// 3. Apply Btn
				click(applyBtn);

				// 4. Confirmation
				findElement(messageConfirmation);
			}
		} else {
			LOG.warn("Was not entered data for carrier service. Item [" + ITEM + "]");
		}
		return status;
	}

	private final By amountValueInputField = By.xpath("//div[4]/div/div/div[4]/div[2]/div/div/input");

	// Payment type arrow
	private final By listPaymentType = By.xpath("//div[3]/div[3]/div[2]/div/div/input");
	private final By amoutToChargeField = By.xpath("//div[4]/div/div[1]/div[4]/div[2]/div[1]/div/input[1]");
	private final By applyButton = By.xpath(
			"//div[contains(@id,'Dialog') and contains(@class,'Dialog') and contains(@style,'position: absolute') and contains(@style,'opacity: 1')]//span[contains(@id,'Button') and contains(@class,'ButtonText') and normalize-space(text())='Apply']");

	// Due Bill - enter
	private void addDueBill(final String valueTotal) throws InterruptedException {

		uploadToList(getTestInfo(), "Total for Due Bill [" + valueTotal + "]$");
		callPayDialog(true);

		selectVisibleTextFromDropDownList(listPaymentType, "Due Bill");

		setText(amountValueInputField, valueTotal + Keys.TAB);
		makeScreenCapture("Enter Due Bill, cash.", getWebDriver());
		waitUntilElementNotPresent(applyButton);
		click(applyButton);

		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was added Due bill [" + valueTotal + "]");
	}

	private final By ccNumInputField = By.xpath("//input[@id='ssdcsDataToTokenize']");
	private final By ccMonthInputField = By.xpath("//div[4]/div/div[2]/div[7]/div[2]/div/div/input");
	private final By ccYearhInputField = By.xpath("//div[2]/div[8]/div[2]/div/div/input");
	private final By ccFrame = By.xpath("//iframe[starts-with(@id,'padssIframe')]");
	private final By typeCreditCard = By.xpath(
			"//label[contains(text(),'Card type:')]/parent::div[1]/following-sibling::div[1]/div[1]/div[2]/input[1]");

	// Due Bill - enter
	private void addCreditCard(final String ccNumber, final String ccMonth, final String ccYear)
			throws InterruptedException, UnknowTypeCreditCardException {

		callPayDialog(false);

		{

			selectVisibleTextFromDropDownList(listPaymentType, "Credit Card");// paymentTypeCC
			switchToFrame(ccFrame);
			setText(ccNumInputField, ccNumber + Keys.TAB + Keys.TAB);
			switchToDefaultFrame();

			String typeCC = getValueAttribute(typeCreditCard, "value");
			if (typeCC == null || typeCC.isEmpty()) {

				// error messages
				//throw new IllegalStateException(String.format("Could not find type of credit card"));
				throw new UnknowTypeCreditCardException("Can NOT find type of Credit Card, PO ["+ draftOrderNumber +"]");
				// Enter credit card

			}

			// Select month
			// Credit Card Exp Month: 5-May
			{
				selectVisibleTextFromDropDownList(ccMonthInputField, ccMonth);
			}

			// Select year
			// Credit Card Exp Year: 2018
			{
				selectVisibleTextFromDropDownList(ccYearhInputField, ccYear);
			}

			switchToDefaultFrame();
			findElements(applyButton);
			scrollToWebElement(applyButton);
			waitUntilElementNotPresent(applyButton);
			makeScreenCapture("Credit card information.", getWebDriver());
			click(applyButton);

			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was added CC card [" + ccNumber + "], exp [" + ccMonth + " / " + ccYear + "]");
		}
	}

	private final By cardNumberInput = By.xpath("//div[4]/div/div[2]/div[3]/div[2]/div/div/input");
	private final By pinNumberInput = By.xpath("//div[4]/div/div[2]/div[4]/div[2]/div/div/input");

	private void addEgiftCard(final String otherCards, final String pinCard) throws InterruptedException {


		callPayDialog(true);
		selectVisibleTextFromDropDownList(listPaymentType, "Gift Card/Merchandise Card/E-Gift Card");
		setText(cardNumberInput, otherCards + Keys.TAB);
		setText(pinNumberInput, pinCard + Keys.TAB);
		/* Enter amount from GiftCard ~ 1$ */

		makeScreenCapture("Payment Confirmation. Enter GiftCard/MerchandiseCard/EGiftCard' Number.", getWebDriver());
		waitUntilElementNotPresent(applyButton);
		click(applyButton);
		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was added eGiftCard [" + otherCards + "], PIN[" + pinCard + "]");
	}

	private void addCash(final String totalAmount) throws InterruptedException {

		uploadToList(getTestInfo(), "Total amount [" + totalAmount + "]$");

		callPayDialog(true);
		{
			selectVisibleTextFromDropDownList(listPaymentType, "Cash");// paymentTypeCash
			setText(amoutToChargeField, totalAmount + Keys.TAB);
			scrollToWebElement(applyButton);
			makeScreenCapture("Enter amount, cash.", getWebDriver());
			click(applyButton);
		}

		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was added cash [" + totalAmount + "]");
	}

	private By certInputField = By.xpath("//label[normalize-space(text())='Certificate Number:']/following::input[position()=1 and contains(@id,'TextBox') and contains(@class,'InputInner')]");
	//Loyalty Rewards Certificate
	private void addLRC(final String certNumber) throws InterruptedException {

		callPayDialog(true);

		{
			selectVisibleTextFromDropDownList(listPaymentType, "Loyalty Rewards Certificate");
			setText(certInputField, certNumber + Keys.TAB);
			makeScreenCapture("Enter amount, cash.", getWebDriver());
			waitUntilElementNotPresent(applyButton);
			click(applyButton);
		}
		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was added Loyalty Rewards Certificate [" + certNumber + "]");
	}
	
	/*Cash Esends*/
	private void addCashEsends(final String totalAmount) throws InterruptedException {

		uploadToList(getTestInfo(), "Total amount for [" + totalAmount + "]$");

		callPayDialog(true);
			selectVisibleTextFromDropDownList(listPaymentType, "Cash Esends");// paymentTypeCash

			setText(amoutToChargeField, totalAmount + Keys.TAB);
			//scrollToWebElement(applyButton);
			makeScreenCapture("Enter amount, cash.", getWebDriver());
			click(applyButton);

		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was added Cash Esends [" + totalAmount + "]");
	}
	
	private void callPayDialog(boolean isCash) {
		
		if(isCash) {
			click(addPaymentType);
			//waitElement(ccFrame);
		}
		else {
			click(addPaymentType);
			waitElement(ccFrame);
		}
		
	}
}// END of CLASS
