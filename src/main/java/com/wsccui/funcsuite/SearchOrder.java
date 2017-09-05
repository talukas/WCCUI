package com.wsccui.funcsuite;

import static core.ThreadsStore.getWebDriver;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.wsccui.template.BasePageWsccui;
import ru.yandex.qatools.allure.annotations.Step;


/**
 * Invalid search criteria, enter at least one of the following combinations: 
 * (Order Number), 
 * (Bill to email), 
 * (Last Name), 
 * (Last Name and Postal Code), 
 * (Customer ID), 
 * (Tracking Number), 
 * (Phone), 
 * (Credit Card Number), 
 * (First Name ,Last Name and Postal Code), 
 * (State and Last Name), 
 * (Gift Card Number).  
*/
public class SearchOrder extends BasePageWsccui {

	static final Logger LOG = Logger.getLogger(SearchOrder.class.getName());
	private final By screenTitleSearchOrder = By.xpath("//span[contains(@id,'widgets_Label') and contains(@class,'screenTitle') and normalize-space(text())='Order Search']");
	
	@Step("Search orders")
	public int getFoundRecords(final String orderNumber, final String ccNumber,
			final String customerID, final String email, final String orderStatus, final String CONCEPT) throws InterruptedException, KeyManagementException, NoSuchAlgorithmException {

		final By orderSearchLink = By.xpath("//a[contains(text(),'Order Search')]");

		final By ccNumInputField = By.xpath(
				"//input[contains(@id,'ssdcsDataToTokenize') and @class='ssdcsPan' and @name='ssdcsDataToTokenize']");
		final By searchBtn = By.xpath(
				"(//span[contains(@id,'Button') and @data-dojo-attach-point='containerNode' and normalize-space(text())='Search'])[2]");
		//OR
		//div[@class='isccsCenter']//span[contains(@id,'Button') and normalize-space(text())='Search']

		final By orderNumberInputField = By.xpath(
				"(//label[contains(@for,'TextBox') and normalize-space(text())='Order Number:']/following::div[1]/div[1]/div[1]/input[1])[2]");

		final By customerIdInputField = By.xpath(
				"(//label[contains(@for,'TextBox') and normalize-space(text())='Customer ID:']/following::div[1]/div[1]/div[1]/input[1])[2]");

		final By emailInputField = By.xpath(
				"//label[contains(@for,'TextBox') and normalize-space(text())='Email:']/following::div[1]/div[1]/div[1]/input[1]");

		final By ccFrame = By.xpath("//iframe[starts-with(@id,'padssIframe')]");

		final By foundRecords = By
				.xpath("//span[contains(@class,'scDataValue') and @dojoattachpoint='labelValueNode']");

		final By conceptSelect = By.xpath("//div[contains(@class,'Visible') and contains(@id,'OrderSearchEditor')]//label[contains(@for,'FilteringSelect') and normalize-space(text())='Concept:']/following::input[position()=1 and contains(@class,'ArrowButtonInner') and @role='presentation']");
		//final By conceptInput = By.xpath("//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + CONCEPT.trim().toUpperCase() + "']");
		final By invalidSearch = By.xpath("//div[contains(@class,'messageTitles') and contains(text(),'Invalid search criteria')]");
		final By tabOrderTitle;
		final By unknownTabOrderTitle = By.xpath("//span[@class='tabLabel' and  @role='tab' and string-length(normalize-space(translate(text(),'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ', '')))=12]");
		//
//		getValueAttribute(selectConceptMenu, "value");
//		click(conceptArrow);
//
//		click(By.xpath(
//				"//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='" + concept.trim().toUpperCase() + "']"));
		//
		
		String howManyRecords = "0";

		logIn();
		click(orderSearchLink);
		
		/* Is it correct Screen*/
		findElement(screenTitleSearchOrder);
		
		/*1. Select concept */
//		getValueAttribute(conceptSelect, "value");
//		click(conceptSelect);
//		click(conceptInput);
		selectVisibleTextFromDropDownList(conceptSelect, selectConcept(CONCEPT));//conceptInput
		
		selectOrderStatus(orderStatus);
		
		String findOrder = null;
		// switchToFrame(ccFrame);

		/* Search by Order Number */
		if (orderNumber != null && !orderNumber.isEmpty()) {
			
			String cleanOrderNumber = replaceStringRegex("([\\s'])", orderNumber,"");
			tabOrderTitle = By.xpath("//span[contains(@id,'ContainerCenter') and @role='tab' and contains(text(),'"+cleanOrderNumber+"')]");
			
			
			// 070206135252
			if (cleanOrderNumber.length() == 12) {
				
				//selectOrderStatus(orderStatus);
				setText(orderNumberInputField, cleanOrderNumber);
				makeScreenCapture("WSCCUI: search order number ["+orderNumber +"]. Order status ["+ orderStatus + " orders]", getWebDriver());

				click(searchBtn);
				makeScreenCapture("WSCCUI: found order number ["+orderNumber +"]. Order status ["+ orderStatus + " orders]", getWebDriver());

				//String findOrder = findOrderInSRC(ORDER_NUMBER_REGEX);
				
				//if(findOrder != null && findOrder.equalsIgnoreCase(cleanOrderNumber)){
				
				
				if(findElements(tabOrderTitle) != 0){
					findOrder = getText(tabOrderTitle);
					
					LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was found [1] actual order ["+findOrder+"], expected order ["+cleanOrderNumber+"]");
					return 1;
				}
				else {
					LOG.error("Can't find expected order number ["+cleanOrderNumber+"]. Actual order number ["+findOrder+"]");
					
					return 0;
				}
				//Integer.parseInt(findOrderInSRC())
				
			}
			else{
				// ERROR MSSG
				LOG.warn("Wrong order number ["+cleanOrderNumber+"]");
			}
		}
		else{
			LOG.warn("Search criteria 'Oder number' is empty.");
		}

		/* Search by Credit card */
		if (ccNumber != null && !ccNumber.isEmpty()) {

			String cleanCcNumber = replaceStringRegex("([\\s])", ccNumber,"");
			
			switchToFrame(ccFrame);

			setText(ccNumInputField, cleanCcNumber + Keys.TAB);

			String ccValueOnBluer = getValueAttribute(ccNumInputField, "value");
			if (ccValueOnBluer != null && !ccValueOnBluer.isEmpty()) {

				LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was found type ["+ccValueOnBluer+"] for credit card ["+cleanCcNumber+"]");
				
				makeScreenCapture("WSCCUI: enter search criteria 'Credit card' ["+ccNumber+"], type ["+ccValueOnBluer+"]. Status [" + orderStatus + " orders]", getWebDriver());
				
				switchToDefaultFrame();
			}
			else{
				// Error MSG
				LOG.warn("Can't validate type of credit card ["+ccNumber+"]");
			}
		}
		else{
			LOG.warn("Search criteria 'Credit card number' is empty.");
		}
		
		/* Search by Customer ID */
		if (customerID != null && !customerID.isEmpty()) {
			// 9
			// 949403408
			// 000000000
			String cleanCustomerID = replaceStringRegex("([\\s])", customerID,"");

			//if (cleanCustomerID.length() == 9) {
				setText(customerIdInputField, cleanCustomerID);
				makeScreenCapture("WSCCUI: enter search criteria 'Customer ID' ["+customerID+"]. Status [" + orderStatus + " orders]", getWebDriver());
			//}
//			else{
//				// Error MSG
//				LOG.warn("Wrong lenght of 'Customer ID' ["+cleanCustomerID+"]");
//			}
		}
		else{
			LOG.warn("Search criteria 'Customer ID' is empty.");
		}

		/* Search by Email */
		if (email != null && !email.isEmpty()) {
			setText(emailInputField, email);
			makeScreenCapture("WSCCUI: enter search criteria 'Email' ["+email+"]. Status [" + orderStatus + " orders]", getWebDriver());
		}

		else {
			// Error MSG
			LOG.warn("Search criteria 'Email' is empty.");
		}

		if((email == null || email.isEmpty()) && (customerID == null || customerID.isEmpty()) && (ccNumber == null || ccNumber.isEmpty()) && (orderNumber == null || orderNumber.isEmpty())){
			String msg = "Search criteria: [Email, Customer ID, Credit card number, Oder number] are empty";
			LOG.error(msg);
			throw new IllegalArgumentException(msg);
		}
		
		// selectOrderStatus(orderStatus);

		// makeScreenCapture("WSCCUI: enter search order criteria, status [" + orderStatus + " orders]");
		click(searchBtn);
		
		if(findElements(invalidSearch) != 0){
			String msg = "Invalid Search criteria";
			LOG.error(msg);
			makeScreenCapture(msg, getWebDriver());
			
			throw new IllegalStateException(msg);
		}
		
		// If we found only 1 record
		else if(findElements(unknownTabOrderTitle) != 0){
			findOrder = getText(unknownTabOrderTitle);
			
			String msg = "WSCCUI: found order number ["+orderNumber +"]. Order status ["+ orderStatus + " orders]";
			makeScreenCapture(msg, getWebDriver());
			LOG.info("TID["+Thread.currentThread().getId()+"] "+msg);
			
			return 1;
		}
		
		// If we found more than only 1 record
		else{
			LOG.info("TID["+Thread.currentThread().getId()+"] "+"Search criteria was correct.");
			howManyRecords = regExp("(\\d{0,6})(?:\\s*)?(?:Records)", getText(foundRecords),1);
			attachText("How many records was found.", howManyRecords);
			makeScreenCapture("WSCCUI: How many orders were found. Status [" + orderStatus + " orders]", getWebDriver());
		}
		
		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was found ["+howManyRecords+"] records");
		return Integer.parseInt(howManyRecords);
	}


	@Step("Select order status [{0}]")
	private void selectOrderStatus(final String orderStat) {
		//String orderStatLowCs = orderStat.toLowerCase();
		/**
		 * Draft Open Closed All
		 */

		//label[contains(translate(.,'Draft orders', 'draft orders'), 'draft orders')]
		//click(By.xpath("//label[contains(translate(.,'" + orderStat + " orders', '" + orderStatLowCs + " orders'), '"+ orderStatLowCs + " orders')]/preceding::div[1]/input"));
		//findElement(screenTitleSearchOrder);
		click(By.xpath("//label[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), '"+orderStat.trim().toUpperCase()+" ORDERS')]/preceding::div[1]/input"));
		
		LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was selected order status ["+orderStat+"]");
	}

	// @Step("Looking for Order by CC [{1}], status [{2}]")
	// private int searchOrderByCC(final String testCaseID, final String
	// ccNumber, final String orderStat)
	// throws InterruptedException {
	// // final By orderSearchLink = By.xpath("//a[contains(text(),'Order
	// // Search')]");
	// // final By searchBtn = By.xpath("(//span[contains(@id,'Button') and
	// // @data-dojo-attach-point='containerNode' and
	// // normalize-space(text())='Search'])[2]");
	// // final By ccFrame =
	// // By.xpath("//iframe[starts-with(@id,'padssIframe')]");
	//
	//// final By ccNumInputField = By.xpath(
	//// "//input[contains(@id,'ssdcsDataToTokenize') and @class='ssdcsPan' and
	// @name='ssdcsDataToTokenize']");
	//// final By foundRecords = By
	//// .xpath("//span[contains(@class,'scDataValue') and
	// @dojoattachpoint='labelValueNode']");
	//
	// // String howManyRecords = null;
	// //
	// // logIn(testCaseID);
	// // click(orderSearchLink);
	// // switchToFrame(ccFrame);
	//
	// setText(ccNumInputField, ccNumber + Keys.TAB);
	//
	// String ccValueOnBluer = getValueAttribute(ccNumInputField);
	// if (ccValueOnBluer != null && !ccValueOnBluer.isEmpty()) {
	//
	// switchToDefaultFrame();
	//
	// selectOrderStatus(orderStat);
	// // String orderStatLowCs = orderStat.toLowerCase();
	// //
	// // //label[contains(translate(.,'Draft orders', 'draft orders'),
	// // 'draft orders')]
	// // click(By.xpath("//label[contains(translate(.,'"+orderStat+"
	// // orders', '"+orderStatLowCs+" orders'), '"+orderStatLowCs+"
	// // orders')]/preceding::div[1]/input"));
	// makeScreenCapture("WSCCUI: Enter credit card number, status [" + orderStat +
	// " orders]");
	//
	// click(searchBtn);
	// howManyRecords = regExp("(\\d{0,6})(?:\\s*)?(?:Records)",
	// getText(foundRecords));
	// attachText("How many records was found.", howManyRecords);
	// makeScreenCapture("WSCCUI: How many order were found. Status [" + orderStat
	// + " orders]");
	//
	// } else {
	// throw new IllegalStateException("Could not find credit card");
	// }
	//
	// return Integer.parseInt(howManyRecords);
	// }
}// END OF CLASS
