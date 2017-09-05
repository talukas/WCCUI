package com.wsccui.pom;

import static core.ThreadsStore.getTestInfo;
import static core.ThreadsStore.getWebDriver;

import java.util.NoSuchElementException;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import com.wsccui.funcsuite.CreateOrder;
import com.wsccui.template.BasePageWsccui;
import ru.yandex.qatools.allure.annotations.Step;

public class SummaryPage extends BasePageWsccui {

	static final Logger LOG = Logger.getLogger(CreateOrder.class.getName());

	private final By ORDER_NUMBER_SEARCH_FIELD = By
			.xpath("//label[contains(text(),'Order Number:')]/following::div[1]/div[1]/div[1]/input[1]");
	private final By SEARCH_BTN = By.xpath("//span[normalize-space(text())='Find Order']");

	private final By ORDER_SUMMARY_SCREEN_TITLE = By.xpath("(//span[normalize-space(text())='Order Summary'])[1]");
	// By.xpath("html/body/div[1]/div[2]/div/div[1]/div[3]/div[6]/div[1]/div[1]/div/table/tbody/tr/td[2]/div/span[1]");

	// By.xpath("(//span[normalize-space(text())='Order Summary'])[2]");
	// By.xpath("//img[@class='screenTitleImage']/following::span[1]");
	private final By goToOrderSummaryPage = By.xpath(
			"//div[contains(@id,'ContentPane') and contains(@class,'wizardNavigationPanel') and not(contains(@class,'NavigationPanelBottom'))]//span[contains(@id,'Button') and contains(@class,'Inline') and contains(@class,'ButtonText') and normalize-space(text())='Go to Order Summary']");

	/**
	 * - [0] Order Number - [1] Email address - [2] Last Name - [3] Postal code
	 */
	String[] searchKeys;

	public SummaryPage() {
	}

	public SummaryPage(String... searchKeys) {
		this.searchKeys = searchKeys;
	}

	@Step("Get Summary page use Order number [{0}]")
	public SummaryPage getSummaryPageByOrderNumber(final String orderNumber) throws InterruptedException {
		String orderNumberClean;

		/* Search use order number */
		if (orderNumber != null && !orderNumber.isEmpty()) {

			uploadToList(getTestInfo(), "Order number [" + orderNumber + "]");
			orderNumberClean = replaceStringRegex("([\\s'])", orderNumber, "");

			setText(ORDER_NUMBER_SEARCH_FIELD, orderNumberClean + Keys.ENTER);

			findElement(ORDER_SUMMARY_SCREEN_TITLE);

			makeScreenCapture("WSCCUI: Summary Page. Order number [" + orderNumberClean + "] was found.", getWebDriver());
		}

		else {
			String msg = "WSCCUI: Summary Page. Empty order number was entered.";
			LOG.error(msg);
			throw new NoSuchElementException(msg);
		}

		return this;
	}

	@Step("")
	public SummaryPage getSummaryPageByEmail(final String orderNumber) {

		return this;
	}

	@Step("")
	public SummaryPage getSummaryPageByLastName(final String orderNumber) {

		return this;
	}

	@Step("")
	public SummaryPage getSummaryPageByPostalCode(final String orderNumber) {

		return this;
	}

	@Step("Get page [Appease Customer]")
	public SummaryPage getAppeasementsPage() throws InterruptedException {
		final By createAppeasements = By.xpath("(//a[contains(normalize-space(text()),'Appease Customer')])[1]");
		final By appCustScreenTitle = By
				.xpath("//span[contains(@class,'screenTitle') and normalize-space(text())='Appease Customer']");
		final By appCustScreenSubTitle = By.xpath("//span[normalize-space(text())='Select Reason']");

		findElement(ORDER_SUMMARY_SCREEN_TITLE);
		click(createAppeasements);
		findElement(appCustScreenTitle);
		findElement(appCustScreenSubTitle);

		makeScreenCapture("WSCCUI: Appease Customer.", getWebDriver());

		return this;
	}

	@Step("Get page [Create Return]")
	public SummaryPage getReturnsPage() throws InterruptedException {
		final By createReturns = By.xpath("(//a[contains(normalize-space(text()),'Create Return/Replacement')])[1]");
		final By addItemsReturnSubTitle = By
				.xpath("//span[contains(@class,'screenSubTitle') and text()='Add Items to Return']");

		findElement(ORDER_SUMMARY_SCREEN_TITLE);
		click(createReturns);
		findElement(addItemsReturnSubTitle);

		makeScreenCapture("WSCCUI: Create returns.", getWebDriver());

		return this;
	}

	@Step("View All Invoices [{0}]")
	public SummaryPage viewAllInvoices() throws InterruptedException {
		final By viewAllInvcLink = By.xpath("//a[contains(text(),'View All Invoices')]");
		final By invoiceScreenTitle = By.xpath("//span[contains(normalize-space(text()),'Order Invoices')]");

		findElement(ORDER_SUMMARY_SCREEN_TITLE);
		click(viewAllInvcLink);
		findElement(invoiceScreenTitle);

		makeScreenCapture("WSCCUI: View All Invoices.", getWebDriver());

		return this;
	}

	// a[contains(text(),'View All Invoices')]
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

	/* Resolve holds */
	public boolean resolveHolds(final String ORDER_NUMBER) {
		final By holdOrderLink = By.xpath(
				"//div[contains(@id,'OrderEditor') and contains(@class,'Visible') and not(contains(@class,'Hidden'))]//img[contains(@class,'icon-onHold') and contains(@class,'ImageAfterText') and normalize-space(@title)='On hold']");
		final By resolveScreenTitle = By
				.xpath("//span[contains(@class,'screenTitle') and normalize-space(text())='Resolve Holds']");
		final By allActiveResolvs = By.xpath(
				"//div[contains(@id,'orderHold_ResolveHold') and contains(@class,'Visible')]//th[contains(@class,'RowHeaderHeaderCell')]/span[contains(@class,'IndirectSelectionCheckBox')]");
		final By saveResolvBtn = By.xpath(
				"//span[contains(@id,'Button') and contains(@class,'Inline') and contains(@class,'ButtonText') and normalize-space(text())='Save']");
		final By confirmMsg = By.xpath(
				"//div[contains(@class,'messageTitles') and contains(text(),'Holds have been resolved successfully')]");
		boolean status = false;

		findElement(ORDER_SUMMARY_SCREEN_TITLE);

		if (findElements(holdOrderLink) != 0) {

			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Holds are present on the Summary page");
			click(holdOrderLink);

			findElement(resolveScreenTitle);
			makeScreenCapture("WSCCUI: Holds are present.", getWebDriver());
			click(allActiveResolvs);
			click(saveResolvBtn);

			/* Confirm */
			if (findElement(confirmMsg) != null) {
				status = true;
				makeScreenCapture(
						"WSCCUI: 'Resolve Holds' were handled, saved and confirmed for order [" + ORDER_NUMBER + "].", getWebDriver());

				/* Go back to the Summary page */
				click(goToOrderSummaryPage);
				//makeScreenCapture("WSCCUI: Summary page after handled 'Resolve Holds'");
			} else {
				LOG.error("'Resolve Holds' were not 'Confirmed'.");
				makeScreenCapture("WSCCUI: Holds were not 'Confirmed'.", getWebDriver());
				
			}
		} else {
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Holds are not present on the Summary page");
			makeScreenCapture("WSCCUI: Holds are not present.", getWebDriver());
		}

		return status;
	}

}// END OF CLASS