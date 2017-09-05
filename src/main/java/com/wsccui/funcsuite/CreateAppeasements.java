package com.wsccui.funcsuite;

import static core.GlobalConstatnt.*;
import java.sql.SQLException;
import java.util.List;
import core.db.sterling.SterlingDB;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.wsccui.pom.SummaryPage;
import com.wsccui.template.BasePageWsccui;
import ru.yandex.qatools.allure.annotations.Step;

import static core.ThreadsStore.getTestInfo;
import static core.ThreadsStore.getWebDriver;
import static core.util.ObjectSupplier.$;

public class CreateAppeasements extends BasePageWsccui {

	static final Logger LOG = Logger.getLogger(CreateAppeasements.class.getName());
	private String totalApprs;

	@Step("Get page [Appease Customer] for Order number [{0}], line# [{1}]")
	public CreateAppeasements createDraftAppeasements(final String orderNumber, final String linesNumber,
			final String reasonCode, final String subReason) throws InterruptedException {

		// final By canclBtn = By.xpath("(//span[contains(@id,'Button') and
		// text()='Cancel'])[1]");
		final By orderLineRBtn = By.xpath(
				"//div[contains(@id,'RadioButtonSet') and not(contains(@class,'scHidden'))]//label[normalize-space(text())='Apply appeasement to:']/following::input[position()=2]");

		final By orderLineRBtnAll = By.xpath(
				"//div[contains(@id,'RadioButtonSet') and not(contains(@class,'scHidden'))]//label[normalize-space(text())='Apply appeasement to:']/following::input[position()=3]");

		By lineNumberChkBox;
		// By.xpath("//div[contains(@class,'RowHeaderBody')]/div[contains(@class,'RowHeaderRow')
		// and
		// @visualindex='"+(lineNumber-1)+"']//span[@class='gridxIndirectSelectionCheckBoxInner']");
		final By reasonCodeSelect = By.xpath("//label[normalize-space(text())='Reason code']/following::input[1]");

		final By reasonCodes = By
				.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
						+ reasonCode.toUpperCase() + "' and @role='option' and contains(@id,'FilteringSelect')]");
		final By subReasonSelect = By.xpath("//label[normalize-space(text())='Sub-Reason code:']/following::input[1]");
		final By subReasonInputField = By.xpath(
				"//label[normalize-space(text())='Sub-Reason code:']/following::input[position()=2 and not(@disabled='')]");

		final By subReasons = By
				.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
						+ subReason.toUpperCase() + "' and @role='option' and contains(@id,'FilteringSelect')]");
		final By nextBtn = By.xpath("(//span[contains(@id,'Button') and normalize-space(text())='Next'])[1]");

		//logIn();

		SummaryPage summaryPage = new SummaryPage();
		//$(SummaryPage.class)
		//.getSummaryPageByOrderNumber(orderNumber)
		summaryPage.getAppeasementsPage();

		// 1. Apply appeasement to

		// 2. Order Line
		if (linesNumber != null && !linesNumber.isEmpty() && !linesNumber.equalsIgnoreCase("0")) {

			click(orderLineRBtn);

			// Cycle
			// String[] lines = linesNumber.split(SPLIT_BY_BUNDLES);

			// for (String currLine : lines) {
			// int lineNumber = Integer.parseInt(currLine);
			/* Only a one Line per times. */
			int lineNumber = Integer.parseInt(linesNumber);
			lineNumberChkBox = By
					.xpath("//div[not(contains(@style,'height: 0')) and @role='grid' and @tabindex='0']//div[contains(@class,'RowHeaderBody')]/div[contains(@class,'RowHeaderRow') and @visualindex='"
							+ (lineNumber - 1) + "']//span[contains(@class,'SelectionCheckBox') and @role='checkbox']");
			click(lineNumberChkBox);
			// }
		}
		// 3. Order
		else {
			click(orderLineRBtnAll);
		}

		// 4. Reason code
		click(reasonCodeSelect);
		click(reasonCodes);
		//TODO
		//selectVisibleTextFromDropDownList(listPaymentType, "Gift Card/Merchandise Card/E-Gift Card");

		// 5. Sub-Reason code:
		// Sometimes NOT Exist
		if (findElements(subReasonInputField) != 0) {
			click(subReasonSelect);
			click(subReasons);
		}

		// 6. Button Next
		makeScreenCapture("WSCCUI: Draft Appeasement.", getWebDriver());
		click(nextBtn);

		return this;
	}

	@Step("Set Appeasement Options")
	public CreateAppeasements setAppeasementOptions(final String appTypes, final String amountCreditOrderLine,
			final String contactTypes, final String contactDetails, final String linesNumber)
			throws InterruptedException {

		// final By canclBtn = By.xpath("(//span[contains(@id,'Button') and
		// text()='Cancel'])[1]");
		// final By appType = By.xpath("//div[@aria-label='Credit amount off
		// order line:']//div[contains(@id,'RadioButtonSet') and
		// not(contains(@class,'scHidden'))]//label[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz',
		// 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'" + appTypes.toUpperCase() +
		// "')]/preceding::input[1]");
		final By amountOrderLine = By.xpath(
				"//div[not(contains(@class,'scHidden')) and contains(@id,'ContentPane') and contains(@_sc_extensibility_referenceuid,'extn_amount_cp') and contains(@data-dojo-props,'insert') ]//input[(contains(@id,'CurrencyTextBox') and @autocomplete='off' and @role='textbox' and not(@disabled)) or (contains(@id,'NumberTextBox') and @autocomplete='off' and not(@disabled))]");
		final By totalApprsText = By.xpath("//div[not(contains(@class,'scHidden')) and contains(@id,'widgets_DataLabel') and contains(@widgetid,'widgets_DataLabel')]//label[contains(normalize-space(text()),'Total appeasement:')]/following::span[1]");
		
		final By totalApprsOrder = By.xpath("//div[contains(@id,'ContentPane') and contains(@class,'ContentPaneSingleChild') and @uid='extn_amount_cp']//div[not(contains(@class,'scHidden')) and contains(@id,'widgets_DataLabel') and contains(@widgetid,'widgets_DataLabel')]//label[contains(normalize-space(text()),'Total appeasement:')]/following::span[1]");
		
		final By contactTypeSelect = By.xpath(
				"//label[normalize-space(text())='Contact type:']/following::input[position()=1 and contains(@class,'ArrowButtonInner') and @value and @role='presentation']");
		final By contactType = By
				.xpath("//div[contains(@id,'FilteringSelect') and contains(@class,'MenuItem') and contains(translate(text(),'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
						+ contactTypes.toUpperCase() + "')]");
		final By contactDetailsInput = By
				.xpath("//label[normalize-space(text())='Contact details:']/following::input[position()=1]");
		final By confirmBtn = By.xpath("(//span[contains(@id,'Button') and normalize-space(text())='Confirm'])[2]");
		final By priorityChkBox = By.xpath(
				"//label[contains(normalize-space(text()),'High priority')]/parent::div/preceding-sibling::div[1]/input");
		final By appTypeForAllOrder = By.xpath(
				"//label[contains(@for,'RadioButtonSet') and normalize-space(text())='Dollar appeasement given on the order as a merchandise card']/preceding::input[position()=1]");

		final String[] appTypeArray = appTypes.split(SPLIT_BY_BUNDLES);

		if (linesNumber != null && !linesNumber.isEmpty() && !linesNumber.equalsIgnoreCase("0")) {
			for (String appTypeCurrent : appTypeArray) {

				// 1. Select [Credit amount off order line:]
				if (appTypeCurrent.toUpperCase().contains("DOLLAR")
						|| appTypeCurrent.toUpperCase().contains("PERCENTAGE")) {

					final By appType = By
							.xpath("//div[@aria-label='Credit amount off order line:']//div[contains(@id,'RadioButtonSet') and not(contains(@class,'scHidden'))]//label[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
									+ appTypeCurrent.toUpperCase() + "')]/preceding::input[1]");

					click(appType);
				} else {
					LOG.warn(
							"Current Appeasement type [" + appTypeCurrent + "] not for type: [DOLLAR] or [PERCENTAGE]");
				}

				// 3. Select [Waive Charges] (or Appeasement Type)
				if (appTypeCurrent.toUpperCase().contains("WAIVE")) {
					final By waive = By
							.xpath("//label[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"
									+ appTypeCurrent.toUpperCase()
									+ "')]/preceding::input[position()=1 and not(@disabled)]");

					if (findElements(waive) != 0) {
						click(waive);
					} else {
						LOG.warn("Can't find Appeasement type - checkbox [" + appTypeCurrent.toUpperCase() + "]");
					}
				} else {
					LOG.warn("Current Appeasement type [" + appTypeCurrent + "] not for type: [WAIVE]");
				}

			}
			
			
		} else {
			click(appTypeForAllOrder);
		}

		// 2. Enter amount
		setText(amountOrderLine, amountCreditOrderLine);

		// 4. Get Total Appeasement for order line
		// String totalApprs = getText(totalApprsText);
		if (linesNumber != null && !linesNumber.isEmpty() && !linesNumber.equalsIgnoreCase("0")) {
		totalApprs = replaceStringRegex("([\\s\\$])", getText(totalApprsText), "");
		}
		// Get Total Appeasement for Order
		else{
			totalApprs = replaceStringRegex("([\\s\\$])", getText(totalApprsOrder), "");
		}
		
		uploadToList(getTestInfo(), "Total appeasement: $[" + totalApprs + "]");

		// 5. Add [Add Note] and Priority.
		// Note: will fill automatically
		click(priorityChkBox);

		/* Contact Preference */
		// 6. Add [Contact type:]
		click(contactTypeSelect);
		click(contactType);

		// 7. Add [Contact details:]
		setText(contactDetailsInput, contactDetails);
		makeScreenCapture("WSCCUI: Draft Appeasement, options.", getWebDriver());

		// 8. click [Confirm] Btn
		// need scroll to button
		click(confirmBtn);
		makeScreenCapture("WSCCUI: Created Appeasement, summary page.", getWebDriver());

		// Assert

		return this;
	}

	/* Summary_click_View_All_Invoices */
	@Step("Get Order Invoices information.")
	public boolean getOrderInvoices(final String ORDER_NUMBER, final String linesNumber) throws InterruptedException, SQLException, ClassNotFoundException {

		final By invoiceQtyLine = By.xpath(
				"//div[contains(@id,'isccs_editors_OrderEditor') and contains(@id,'isccs_editors_OrderEditor') and contains(@class,'dijitVisible')]//td[string-length(normalize-space(text()))=20 and @role='gridcell']");
		// = By.xpath("//div[contains(@id,'isccs_editors_OrderEditor') and
		// contains(@id,'isccs_editors_OrderEditor') and
		// contains(@class,'dijitVisible')]//td[contains(normalize-space(text()),'Appeasement')
		// and @role='gridcell']");
		// final By invoicedLine =
		// By.xpath("//td[contains(text(),'$"+totalApprs+"')]");
		final By invoiceNumber = By
				.xpath("//label[contains(normalize-space(text()),'Invoice number:')]/following::span[position()=1]");

		// final By invoiceReference =
		// By.xpath("//label[contains(normalize-space(text()),'Reference
		// number:')]/following::span[position()=1]");
		final By totalAmount = By.xpath("//label[normalize-space(text())='Total:']/following::span[1]");
		// = By.xpath("//span[contains(@id,'widgets_Label') and
		// contains(@class,'groupHeader') and
		// (contains(normalize-space(text()),'Total charge ($') or
		// (contains(normalize-space(text()),'Total charge $')))]");

		//	boolean sterlingVerification = false;
		boolean status = false;
		int count = 0;
		long start;

		// NOT exist Invoice order for Order level, only for line level.
		if (linesNumber != null && !linesNumber.isEmpty() && !linesNumber.equalsIgnoreCase("0")) {
		$(SummaryPage.class).viewAllInvoices();

		List<WebElement> invoiceListQty = findElements(invoiceQtyLine, "");
		int invoiceQty = invoiceListQty.size();
		uploadToList(getTestInfo(), "Order Invoices list, qty [" + invoiceQty + "]");

		// Assert found more than 0 invoice records
		// assertThat(invoiceListQty, describedAs("Expected Not '0' Invoice
		// list", is(not(0))));

		/* If in the order exist more than a one invoice order .... */
		checkInvoice: for (int i = 0; i < invoiceQty; i++) {

			click(invoiceListQty.get(i));
			makeScreenCapture("WSCCUI: Order Invoices(" + i + "). Invoice Detail.", getWebDriver());

			/* Read Invoice number */
			String invoiceNumberValue = getText(invoiceNumber);
			// Assert found not null [Invoice number]
			// assertThat(invoiceListQty, describedAs("Expected Not '0' Invoice
			// number", notNullValue()));
			uploadToList(getTestInfo(), i + ") Invoices number (WSCCUI) [" + invoiceNumberValue + "]");
			// Assert in sterling

			/* Read Reference number */
			// String invoiceReferenceValue = null;
			// if(findElements(invoiceReference) != 0){
			// invoiceReferenceValue = getText(invoiceReference);
			// // Assert found not null [Reference number]
			// // assertThat(invoiceReferenceValue, describedAs("Expected Not
			// '0' Reference number", notNullValue()));
			// uploadToList(testInfoArrayText, i+") Reference number (WSCCUI) ["
			// + invoiceReferenceValue + "]");
			// }

			// Assert in sterling
			/* Read Total amount for invoice number */
			String totalAmountValue = replaceStringRegex("([\\$()])", getText(totalAmount), "");
			// = regExp(TOATAL_AMOUNT_APP, getText(totalAmount), 1);

			uploadToList(getTestInfo(), i + ") Total charge (WSCCUI) [" + totalAmountValue + "]");

			// System.out.println(a.isInvoiceOrderCorrect("20170215-17103821737","170320294680","0.22","CM","0.22"));

			/* Need to check on next Release */
			// if((new SterlingDB()).isInvoiceOrderCorrect(invoiceNumberValue,
			// ORDER_NUMBER, totalAmountValue)){
			// sterlingVerification = true;
			// }
			// else{
			// sterlingVerification = false;
			// break checkInvoice;
			// }

			// 1
			status = (new SterlingDB()).isInvoiceOrderCorrect(invoiceNumberValue, ORDER_NUMBER, totalAmountValue);
			count = 0;

			start = System.currentTimeMillis();
			while (!status) {
				if (count > TIME_SLEEP_MULTIPLIER_5) {

					status = false;
					break checkInvoice;
				} else {

					Thread.sleep(TIME_SLEEP_1000 * TIME_SLEEP_MULTIPLIER_10);
					status = (new SterlingDB()).isInvoiceOrderCorrect(invoiceNumberValue, ORDER_NUMBER,
							totalAmountValue);
					// LOG.info("TID["+Thread.currentThread().getId()+"] "+"TID["+Thread.currentThread().getId()+"] "+"Check status in Sterling [" + status + "].
					// Interation [" + count + "].");
					count++;
				}
			}

			String msg = "Invoice [" + invoiceNumberValue + "] exist in Sterling [" + status + "]. Iteration [" + count
					+ "]. Duration [" + (System.currentTimeMillis() - start) / 1000 + "] sec.";
			// = "Released order from DTC to Sterling, duration
			// ["+(System.currentTimeMillis() - start)/1000+"] sec. Status
			// ["+status+"]";
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"TID["+Thread.currentThread().getId()+"] "+msg);
			uploadToList(getTestInfo(), msg);
			// 2
		}


		}
		else{
			LOG.warn("NOT exist Invoice order for Order level, only for line level.");
			status = true;
		}
		
		return status;
		// return sterlingVerification;
	}

} // END OF CLASS