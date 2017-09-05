package com.wsccui.funcsuite;

import static com.wsccui.ConstatntWSCCUI.*;
import static core.GlobalConstatnt.*;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import com.wsccui.template.BasePageWsccui;
import core.db.sterling.SterlingDB;
import ru.yandex.qatools.allure.annotations.Step;

//import ru.yandex.qatools.allure.annotations.Step;
import static core.ThreadsStore.getTestInfo;
import static core.ThreadsStore.getWebDriver;

public class CreateAlert extends BasePageWsccui {

	static final Logger LOG = Logger.getLogger(CreateOrder.class.getName());

	private final By linkCreateAlert = By.xpath(
			"//div[contains(@id,'OrderEditor') and contains(@class,'dijitVisible')]//div[not(contains(@class,'scHidden')) and contains(@id,'widgets_Link') and contains(@class,'LinkDerived')]//a[contains(@class,'LinkDerived') and normalize-space(text())='Create Alert']");
	// By.xpath("//a[contains(text(),'Create Alert')]");

	// private final By conceptSelect =
	// By.xpath("(//label[text()='Concept:']/following::div[1]/div[1]/div[1]/input[1])[2]");
	private final By alertTypeSelect = By
			.xpath("//label[text()='Alert type:']/following::div[1]/div[1]/div[1]/input[1]");
	private final By alertPrioritySelect = By
			.xpath("//label[text()='Priority:']/following::div[1]/div[1]/div[1]/input[contains(@role,'presentation')]");

	// private final By linkCreateAlert = By.xpath(
	// private final By linkCreateAlert = By.xpath(
	// private final By linkCreateAlert = By.xpath(
	// private final By linkCreateAlert = By.xpath(

	/* Create order */
	@Step("Enter Mandatory requirements")
	public CreateAlert createDraftAlert(final String alertType, final String priorityAlert)
			throws InterruptedException {
		// For WSCCUI available only MG brand
		// final String concept = "Mark and Graham";
		final By createAlrtTitle = By
				.xpath("//span[contains(@class,'screenTitle') and normalize-space(text())='Create Alert']");
		// uploadToList(testInfoArrayText, "Test case [" + testCaseID + "]");

		/* Open start page */
		// logIn();

		click(linkCreateAlert);

		/* Confirm screen Title */
		findElement(createAlrtTitle);

		/* Select concept */
		{
			// click(conceptSelect);
			// click(By.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
			// + concept.trim().toUpperCase() + "']"));
		}

		/* Select Alert type */
		{
			click(alertTypeSelect);
			click(By.xpath(
					"//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
							+ alertType.trim().toUpperCase() + "']"));
		}

		/* Select priority */
		{
			click(alertPrioritySelect);
			click(By.xpath(
					"//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
							+ priorityAlert.trim().toUpperCase() + "']"));
		}

		return this;
	}

	private final By assignedUserInput = By.xpath(
			"(//label[text()='Assigned to user:']/following::div[1]/div[1]/div[1]/input[contains(@id,'TextBox') and @tabindex='0'])[1]");
	//private final By saleOrderInput = By.xpath("(//label[text()='Order Number:']/following::input[1])[last()]");
	// By.xpath("(//label[text()='Order
	// Number:']/following::div[1]/div[1]/div/input[contains(@id,'TextBox') and
	// @tabindex='0'])[3]");
	//private final By returnOrderInput = By.xpath("(//label[text()='Return number:']/following::input[1])[last()]");

	private final By descriptionInputField = By.xpath("//label[text()='Description:']/following::textarea[1]");
	// By.xpath("//textarea[contains(@style,'position: relative') and
	// @aria-invalid='true']");
	private final By confButtn = By.xpath("(//span[contains(@id,'Button') and text()='Confirm'])[last()]");
	private final By alertConf = By.xpath("//div[contains(text(),'Alert details updated successfully.')]");
	private final By mngAlrtTitle = By
			.xpath("//span[contains(@class,'screenSubTitle') and normalize-space(text())='Manage Alert']");

	@Step("Enter Optional requirements")
	public String enterAlertOptions(final String assignUser, final String description) throws InterruptedException {
//final String associatOrder, final String orderNumber,
	//	final By associaChkBox = By.xpath("//label[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'"+ associatOrder.trim().toUpperCase() + " ORDER')]/preceding::input[1]");

		if (assignUser != null && !assignUser.isEmpty()) {
			// enter user
			setText(assignedUserInput, assignUser);
		}

		/* Association */
		// Return Order or Sales Order
//		if (associatOrder != null && !associatOrder.isEmpty() && (orderNumber != null && !orderNumber.isEmpty())) {
//			// click(By.xpath("(//label[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz',
//			// 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'" +
//			// associatOrder.trim().toUpperCase() + "
//			// ORDER')]/preceding::div[1]/input[contains(@id,'RadioButtonSet')
//			// and @aria-checked='false'])[2]"));
//			click(associaChkBox);
//
//			if (associatOrder.equalsIgnoreCase("Return")) {
//				setText(returnOrderInput, orderNumber);
//			}
//
//			else if (associatOrder.equalsIgnoreCase("Sales")) {
//				setText(saleOrderInput, orderNumber);
//			}
//		}

		/* Enter Descriptions */
		setText(descriptionInputField, description);

		makeScreenCapture("Draft Alert.", getWebDriver());
		// btnConf
		click(confButtn);

		/* Confirmation window */

		/* Confirm screen Title */
		findElement(mngAlrtTitle);

		// Aler ID = 20170205012536906165983
		String alertID = findOrderInSRC(ALERT_ID_REGEX);
		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was found Alert ID [" + alertID + "]");
		uploadToList(getTestInfo(), "Create Alert ID [" + alertID + "]");
		makeScreenCapture("Created Alert, Get Alert ID.", getWebDriver());

		// btnConf
		click(confButtn);
		makeScreenCapture("Created Alert. Confirmation page.", getWebDriver());

		// Is text Present [Alert details updated successfully.]
		findElement(alertConf);

		return alertID;
	}

	@Step("Validation Alert ID [{2}] in Sterling for order [{0}]")
	public boolean alertValidationSterling(final String ORDER_NO, final String ALERT_TYPE, final String ALERT_ID,
			final String ALERT_DESCRIPT_EXPECTED) throws SQLException, InterruptedException, ClassNotFoundException {

		boolean status = (new SterlingDB().isAlertExistSterling(ORDER_NO, ALERT_TYPE, ALERT_ID, "detail_description",
				ALERT_DESCRIPT_EXPECTED));
		int count = 0;

		final long start = System.currentTimeMillis();
		while (!status) {
			if (count > TIME_SLEEP_MULTIPLIER_5) {
				break;
			} else {
				Thread.sleep(TIME_SLEEP_1000 * TIME_SLEEP_MULTIPLIER_10);
				status = (new SterlingDB().isAlertExistSterling(ORDER_NO, ALERT_TYPE, ALERT_ID, "detail_description",
						ALERT_DESCRIPT_EXPECTED));
				// LOG.info("TID["+Thread.currentThread().getId()+"] "+"Check status in Sterling [" + status + "].
				// Interation [" + count + "].");
				count++;
			}
		}

		String msg = "Alert ID exist in Sterling [" + status + "]. Iteration [" + count + "]. Duration ["
				+ (System.currentTimeMillis() - start) / 1000 + "] sec.";
		// = "Released order from DTC to Sterling, duration
		// ["+(System.currentTimeMillis() - start)/1000+"] sec. Status
		// ["+status+"]";
		LOG.info("TID["+Thread.currentThread().getId()+"] "+msg);
		uploadToList(getTestInfo(), msg);

		return status;
	}
} // END OF CLASS