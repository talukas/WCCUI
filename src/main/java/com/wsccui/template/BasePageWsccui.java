package com.wsccui.template;

import static com.wsccui.ConstatntWSCCUI.*;
import static core.ThreadsStore.getTestInfo;
import static core.ThreadsStore.getWebDriver;
//import static core.ThreadsStore.getWebDriver;
import static core.util.BaseConfig.OMSD_PSW;
//import static core.util.BaseConfig.START_IP;
import static core.util.BaseConfig.OMSD_USR;
import static core.util.BaseConfig.getDriverType;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
//import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
//import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
//import org.openqa.selenium.NoAlertPresentException;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.TimeoutException;
//import org.openqa.selenium.UnhandledAlertException;
//import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
import static core.util.BaseConfig.getProperty;
import static core.util.CustomAssertThat.assertThat;
import static core.GlobalConstatnt.*;
//import static core.webdriver.Constatnts.CHARS_NOT_ALLOWED;
//import static core.webdriver.Constatnts.SPLIT_BY_BUNDLE;
//import static core.webdriver.Constatnts.SPLIT_BY_BUNDLES;
//import static core.webdriver.Constatnts.getCookiesInstance;
//import static core.webdriver.Constatnts.getUrlToken;
//import static core.webdriver.Constatnts.setCookiesInstance;
//import static core.webdriver.Constatnts.setSubTotalExpected;
//import static core.webdriver.Constatnts.setUrlToken;
//import com.wsccui.exception.InvalidItemException;
import core.webdriver.HandleBasicAuthorization;
import core.webdriver.template.BasePage;
//import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;
//import static org.apache.commons.lang.StringEscapeUtils.escapeJava;
import static org.hamcrest.CoreMatchers.describedAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

/**
 * @author Yurii Chukhrai on [Aug 2016]
 * @version - [1.0]
 * @file - [BasePage.java]
 */

/*
 * This Class is Parent for all Classer for POM. Contain Basic methods for page
 */
public class BasePageWsccui extends BasePage {

	static final Logger LOG = Logger.getLogger(BasePageWsccui.class.getName());
	
	@Step("Enter Item. Draft Order page")
	protected double enterItems(final String bundlesItems, final String storeNumber, final String orderType)
			throws NumberFormatException, SocketException, UnknownHostException, Exception {
		/* New Version */
		final By storeNumberInput = By.xpath(
				"//label[contains(normalize-space(text()),'Store Number')]/following::input[position()=1 and contains(@id,'TextBox') and contains(@class,'InputInner')]");
		// By.xpath("//label[contains(normalize-space(text()),'Store
		// Number')]/following::div[1]/div[1]/div[1]/input[1]");

		final By selectTypeOrder = By.xpath(
				"//label[contains(normalize-space(text()),'Select Order Type')]/following::input[position()=1 and contains(@class,'ArrowButtonInner') and @role='presentation']");
		// By.xpath("//label[contains(normalize-space(text()),'Select Order
		// Type')]/following::div[1]/div[1]/div[1]/input[1]");

		final By nextButton = By.xpath("(//span[contains(normalize-space(text()),'Next')])[2]");
		final By estimateOrderTotalLabel = By.xpath("//label[normalize-space(text())='Estimated Order Total:']/following::span[position()=1 and @class='scDataValue' and @dojoattachpoint='labelValueNode' and contains(text(),'$')]");
		double subTotalExpected = 0.0;
		final By updateOrderTotalBtn = By.xpath("//span[normalize-space(text())='Update Total']"); 
		final By updateOrderTotalLabel = By.xpath("//span[contains(@id,'Label') and normalize-space(text())='Order total:']/following::a[ contains(@class,'LinkDerived') and contains(@title,'pricing summary') and contains(text(),'$')]");
		final DecimalFormat doubleFormat = new DecimalFormat("#######.00");

		// Store Number
		if (storeNumber != null && !storeNumber.isEmpty()) {

			setText(storeNumberInput, storeNumber);
		}

		// Select Order Type
		// Mail Order or Phone Order
		// Default value 'Phone Order'
		if (orderType != null && !orderType.isEmpty()) {

			 //&& getValueAttribute(selectFTypeOrder, "value") != null
			
//			click(selectFTypeOrder);
//			click(By.xpath(
//					"//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
//							+ orderType.trim().toUpperCase() + " ORDER']"));
//			By orderTypeValue =By.xpath(
//					"//div[translate(normalize-space(text()),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='"
//							+ orderType.trim().toUpperCase() + " ORDER']");
			
 			selectVisibleTextFromDropDownList(selectTypeOrder, orderType+" ORDER");
		}

		// add iteration how many correct Items in Bundle
		String cleanBundlesItems = replaceStringRegex("([\\s\"'])", bundlesItems, "");

		List<String> bandleList = new ArrayList<String>(Arrays.asList(cleanBundlesItems.split(SPLIT_BY_BUNDLES)));
		
		// List<String> bandleList = new
		// ArrayList<String>(Arrays.asList(cleanBundlesItems.split(SPLIT_BY_BUNDLES)));

		//TODO For another frameworks
		// prepareCorctBundlCO(bandleList, SPLIT_BY_BUNDLE);
		
		int count = 1;
	
		for (String item : bandleList) {

			subTotalExpected+=addItemToCart(item, count);
			count++;
		}

		//double myEstimateOrderTotal = 0.0;
		
		//double estimateOrderTotalActual = Double.parseDouble(getText(estimateOrderTotalLabel).replaceAll("$", ""));
		//assertThat(estimateOrderTotalActual, describedAs("Expected 'Estimated Order Total' ["+ estimateOrderTotalExpected +"]", allOf(not(0.0), is(estimateOrderTotalExpected))));
		//doubleFormat
		/* Click Update Total */
				click(updateOrderTotalBtn);
				
		final String estimateOrderTotalActual = getText(estimateOrderTotalLabel).replaceAll(CHARS_NOT_ALLOWED, "").trim();
		//final String estimateOrderTotalExpectedString = doubleFormat.format(estimateOrderTotalExpected);
		//assertThat(estimateOrderTotalActual, describedAs("Expected 'Estimated Order Total' ["+ estimateOrderTotalExpectedString +"]", allOf(not("0.0"), is(estimateOrderTotalExpectedString))));
		//LOG.debug("Assert 'Estimated Order Total' ["+estimateOrderTotalActual+"] - Correct.");
		
		//double updatedOrderTotal = Double.parseDouble(getText((updateOrderTotalLabel));
		//assertThat(estimateOrderTotalActual, describedAs("Expected 'Order Total' ["+ estimateOrderTotalActual +"]", allOf(not(0.0), is(updatedOrderTotal))));
		final String updatedOrderTotal = getText(updateOrderTotalLabel).replaceAll(CHARS_NOT_ALLOWED, "");
		makeScreenCapture("Items were entered and 'Total price' was updated.", getWebDriver());
		assertThat(updatedOrderTotal, describedAs("Expected 'Order Total' ["+ updatedOrderTotal +"]", allOf(not("0.0"), is(estimateOrderTotalActual))));
		LOG.debug("Assert 'Order Total' ["+ updatedOrderTotal +"] - Correct.");
		setSubTotalExpected(doubleFormat.format(subTotalExpected));
		
		// if (driver.isTextPresent("OE200R2")) {
		// addItemToCart(bandleList, SPLIT_BY_BUNDLE);
		// }

		/* OLD Version */
		// waitProcessing();

		// setText(itemInputField, "1019926");
		// setText(itemInputField, sku);
		// click(btnAdd);
		//
		// // qty
		// actionMoveToElement(inputQtyItem).doubleClick().build().perform();
		//
		// //setText(inputQtyItem, "10", Keys.TAB);
		// setText(inputQtyItem, qty + Keys.TAB);
		// makeScreen("Enter item page.");

		// scrollToWebElement(nextButton);
		scrollToTopPage();
		
		// Update total:
		
		
		// actionMoveToElement(inputQtyItem).doubleClick().build().perform();
		// actionMoveToElement(nextButton).build().perform();
		click(nextButton);

		return Double.parseDouble(updatedOrderTotal);
	}

	final By chooseVariationCinfirm = By.xpath(
			"//span[not(contains(@class,'scHidden')) and contains(@class,'ButtonDerived') and contains(@class,'SpecialButton')]//span[normalize-space(text())='Confirm']");

	// Add Item to cart
		private double addItemToCart(final String bandle, int count) throws NumberFormatException, SocketException, UnknownHostException, Exception {

			final By itemInputField = By.xpath(
					"//label[contains(@for,'TextBox') and normalize-space(text())='Item ID']/following::input[contains(@id,'TextBox') and contains(@autocomplete,'off')][2]");
			// By.id("idx_form_TextBox_30");

			final By inputQtyItem = By.xpath("//input[@id='idx_form_NumberTextBox_0']");
			final By chooseVariationLabel = By.xpath(
					"//span[contains(@id,'Label') and contains(@class,'blockComponent') and contains(text(),'Choose the variation')]");
			final By validationBox = By.xpath(
					"//div[contains(@id,'AddItems') and contains(@class,'dijitVisible')]//div[contains(@id,'TextBox') and contains(@class,'dijitLeft') and contains(@class,'TextBoxWrapError') and contains(@class,'ValidationTextBoxError')]//div[contains(@class,'ValidationIcon')]/input[contains(@class,'ValidationInner')]");
			final By skuErrorMsgLabel = By.xpath("//div[contains(@id,'Message') and contains(@class,'errorMessage') and @role='alert']");
			final By skuPriceLabel = By.xpath("//a[normalize-space(text())='Override Price']/preceding::span[position()=3 and @class='scDataValue' and @dojoattachpoint='labelValueNode' and contains(text(),'$')]");
			
			// boolean status = false;

			// String category = null;
			String sku = null;
			String qty = null;
			String personalization = null;
			double skuTotal = 0.0;
			final DecimalFormat doubleFormat = new DecimalFormat("#######.00");

			String[] item = bandle.split(SPLIT_BY_BUNDLE);

			// category = item[0];
			// sku = replaceStringRegex(" ", item[0], "");
			// qty = replaceStringRegex(" ", item[1], "");
			// personalization = replaceStringRegex(" ", item[2], "");

			sku = item[0];
			qty = item[1];
			personalization = item[2];

			// +scrollToTopPage();

			/* Confirmation Window */
			// if(findElements(By.xpath("//div[contains(@id,'ConfirmationDialog')
			// and @role='alertdialog']")) != 0 ){
			if (findElements(By.xpath(
					"//div[contains(@style,'opacity: 1') and @role='alertdialog' and contains(@id,'ConfirmationDialog')]")) != 0) {

				By okBtn = By.xpath("//span[text()='Ok']");

				if (findElements(okBtn) != 0) {
					click(okBtn);
				}
				makeScreenCapture("Confirmation alert was handled.", getWebDriver());
			}

			setText(itemInputField, sku + Keys.ENTER);

			if (findElements(validationBox) != 0 || findElements(skuErrorMsgLabel) !=0) {
				// 192.168.204.215 - DJ44WR52
				String msg = "Invalid Item [" + sku + "]";
				makeScreenCapture(msg, getWebDriver());
				uploadToList(getTestInfo(), msg);
				LOG.error(msg);
				// return false;
				//throw new WrongItemException("Invalid Item");
				
			} else {
				// scrollToTopPage();
				// click(btnAdd);

				// Choose the variation item by selecting the preferred options.
				// Choose the variation item by selecting the preferred options.
				// 1

				// findElements(chooseVariationLabel);

				// +if (!isTextPresent("Choose the variation item by selecting the
				// preferred options.")) {
				// if(findElements(inputQtyItem)!=0){

				if (findElements(chooseVariationLabel) == 0) {
					// qty
					actionMoveToElement(inputQtyItem).doubleClick().build().perform();

					/* Enter variation if it need */
					// Choose the variation item by selecting the preferred options.

					// setText(inputQtyItem, "10", Keys.TAB);
					setText(inputQtyItem, qty + Keys.TAB);
				}

				// NOT IMPLEMENT YET

				else if (findElements(chooseVariationLabel) != 0 && findElements(chooseVariationCinfirm) != 0) {

					setVariation(count, qty);
				}

				makeScreenCapture("Enter item. SKU [" + sku + "]. Qty [" + qty + "]", getWebDriver());
				
				//Calc total price for SKU = price*QTY
				skuTotal = Double.parseDouble(getText(skuPriceLabel).replaceAll("[$,]", "").trim()) * Double.parseDouble(qty);
				skuTotal = Double.parseDouble(doubleFormat.format(skuTotal).toString());
				LOG.debug("SKU total = Price * QTY = ["+skuTotal+"]");

				// add personalization
				if (personalization != null && (personalization.equalsIgnoreCase("Y")
						|| personalization.equalsIgnoreCase("MY") || personalization.equalsIgnoreCase("YM"))) {
					setPersonalize(Integer.parseInt(qty));
				}
				// scrollToTopPage();
			}
			return skuTotal;
		}
		
		private void setVariation(int line, final String qty) throws InterruptedException {
			final By variationStyleLink = By.xpath(
					"//ul[@id='UnorderedListID']//li[contains(@id,'ButtonSetWidget') and contains(@class,'skuSelector')]//li[contains(@id,'libWidget') and @class='li-style']//a[contains(@id,'abWidget') and @class='a-style']");
			final By chooseVariationCinfirm = By.xpath(
					"//span[not(contains(@class,'scHidden')) and contains(@class,'ButtonDerived') and contains(@class,'SpecialButton')]//span[normalize-space(text())='Confirm']");
			final By lineInputList = By.xpath("//div[" + line + "]/table/tbody/tr/td[4]");
			final By lineInputField = By.xpath("//table/tbody/tr/td[1]/div/div[3]/div[2]/div[2]/div[" + line
					+ "]/table/tbody/tr/td[4]/div/div/div[2]/div[1]/div/input[1]");

			actionMoveToElement(lineInputList).doubleClick().build().perform();
			// setText(By.xpath("div["+count+"]/table/tbody/tr/td[4]/div/div/div[2]/div[1]/div/input[1]"),
			// qty + Keys.TAB);

			setText(lineInputField, qty + Keys.TAB);
			// click(By.xpath("(//span[contains(text(),'Confirm')])[3]"));

			for (WebElement style : findElements(variationStyleLink, "")) {
				click(style);
			}

			makeScreenCapture("Enter item. Select style.", getWebDriver());
			click(chooseVariationCinfirm);
		}

		private boolean setPersonalize(int qty) throws SocketException, UnknownHostException, Exception {
			boolean status = false;
			final By persnBtn = By.xpath("//span[contains(@id,'Button') and contains(@class,'ButtonText') and normalize-space(text())='Personalize']");
					//By.xpath("//span[@id='dijit_form_Button_25_label']");
			// private final By persnTxt = By.xpath("//input[@name='text1']");
			final By continBtn = By.xpath("//a[contains(text(),'Continue')]");
			final By closeBtn = By.xpath("//span[contains(@id,'Button') and normalize-space(text())='Close']");
			final By applyToAll = By.xpath("//input[@class='monoApplyAll' and @type='checkbox']");
			// final String monogramInputFieldsBase =
			// "//div[not(contains(@style,'opacity: 0')) and
			// normalize-space(text())='Please enter
			// text.']/following-sibling::div[@class='monogram-entry-set
			// selectable-content']/label[contains(@style,'display: block') and
			// @class='charLimitLabel']";
			final By monogramInputFields = By.xpath("//div[contains(@class,'monogram-select-and-preview') and not(contains(@class,'hidden')) and not(contains(@style,'height: 0px')) and not(contains(@style,'opacity: 0'))]//*[not (contains(@style,'display: none'))]/input[contains(@class,'persText')]");
	//By.xpath("//div[contains(@class,'monogram-select-and-preview') and not(contains(@class,'hidden')) and not(contains(@style,'height: 0px')) and not(contains(@style,'opacity: 0'))]//input[contains(@class,'persText')]");

			String driverType = getDriverType();
			String usr = getProperty("proxy_user"); //"B76KIgVovq3e5jkhrQfS1w==";
			String psw = getProperty("proxy_psw"); //"tMxSiQn5vv+zRs/E0hE4ZQ==";
					

			if (driverType.equalsIgnoreCase("FF") || driverType.equalsIgnoreCase("R_FF") || driverType.equalsIgnoreCase("FF_R")) {

				(new Thread(new HandleBasicAuthorization(usr, getCurrentTime(psw, getCurrentLanguage("en"))))).start();
				click(persnBtn);
			}

			else if (driverType.equalsIgnoreCase("CHROME") || driverType.equalsIgnoreCase("R_CHROME") || driverType.equalsIgnoreCase("CHROME_R")) {

				try {
					(new Thread(new HandleBasicAuthorization(usr,
							getCurrentTime(psw, getCurrentLanguage("en"))))).start();

				} catch (Exception e) {
					// e.printStackTrace();
					LOG.error("Incorrect login or password [" + e.getStackTrace() + "]");
					throw new IllegalArgumentException("Can't Login.");
				}

				click(persnBtn);
			}
			else{
				LOG.error("Unknow browser type for Sikuli");
			}

			switchToDefaultFrame();

			switchToFrame(By.xpath(".//*[@id='myFrame']"));

			// 2. Enter Text:
			String pageSource = getSRC();

			/*
			 * if (regExp("(Enter\\sText\\:)", pageSource, 1) != null) {
			 * 
			 * // Apply for each Item for (int i = 1; i <= qty; i++) { //
			 * (//input[@name='text1'])[2] // Exist fields only for text [A-z]
			 * setText(By.xpath("(//input[@name='text1'])[" + i + "]"), "P"); }
			 */

			// Apply All Items
			if (findElements(applyToAll) != 0) {

				click(applyToAll);
			}

			if (regExp("(Enter\\sText\\:)", pageSource, 1) != null) {
				// How many fields to enter text?
				for (WebElement currentField : findElements(monogramInputFields, "")) {
					setText(currentField, "P" + Keys.TAB);
				}
			}

			// 3. Select Color:
			// if(regExp("(Select\\sColor::)",pageSource)!=null){
			//
			// setText(persnTxt,"Y");
			// }
			// Thread.sleep(10000L);

			// if (findElements(continBtn) != 0) {
			// click(continBtn);
			// actionMoveToElement(continBtn).click().build().perform();
			// }

			/* Copy new Cookies */
			/*
			 * org.openqa.selenium.InvalidCookieDomainException: invalid cookie
			 * domain: invalid domain:".qa11.markandgraham.com"
			 */
			// final Set<Cookie> oldCookiesInstance = getCookiesInstance();
			// System.out.println("Old "+oldCookiesInstance.size());
			//
			// final Set<Cookie> newCookiesInstance = driver.manage().getCookies();
			// System.out.println("New "+newCookiesInstance.size());
			// for(Cookie tmp:newCookiesInstance){
			// System.out.print("Domain ["+tmp.getDomain()+"]. Name
			// ["+tmp.getName()+"]. Expiry["+tmp.getExpiry()+"]. Value
			// ["+tmp.getValue()+"]. Path ["+tmp.getPath()+"]");
			// System.out.println();
			// }
			//
			// if (newCookiesInstance !=null && !equals(oldCookiesInstance,
			// newCookiesInstance)) {
			//
			// setCookiesInstance(newCookiesInstance);
			//
			// System.out.println("Update cookies. ");
			// System.out.println("Old "+getCookiesInstance().size());
			// System.out.println("New "+newCookiesInstance.size());
			//
			// // addCookies();
			// }

			if (findElements(continBtn) != 0) {
				click(findElement(continBtn), "");
			}

			else if (findElements(continBtn) == 0 && findElements(closeBtn) != 0) {
				click(findElement(closeBtn), "");
			}

			makeScreenCapture("Set monogram.", getWebDriver());
			switchToDefaultFrame();

			return status;
		}
		
		/* POM Methods */
		/* Login to WSCUUI */
		// TESTME
		@Step("Login...")
		protected BasePage logIn() throws InterruptedException, KeyManagementException, NoSuchAlgorithmException {
			
			Set<Cookie> cookiesInstance = getCookiesInstance();
			String urlToken = getUrlToken();

			openURL(getProperty("wsccui_host"));

			if (cookiesInstance != null && !cookiesInstance.isEmpty() && urlToken != null) {
				/* Add cookies - Fast Login */
				LOG.debug("Fast login procedure.");
				addCookies(cookiesInstance, urlToken);

			} else {
				LOG.debug("Basic login procedure.");
				setLogin();
			}

			return this;
		}
		
		final By homeSpan = By.xpath("//span[contains(@class,'screenTitle') and text()='Home'] ");
		
		private void setLogin() throws InterruptedException {

			/* Login to WSCUUI */
			final By userField = By.xpath("//input[@name='username']");
			final By passwordField = By.xpath("//input[@name='password']");
			final By loginBtn = By.xpath("//span[contains(text(),'Login')]");
			final String title = "Williams-Sonoma Call Center UI";
			

			// Slow Login
			String usr = getProperty(OMSD_USR);
			String psw = getProperty(OMSD_PSW);

			getTitle(title);

			

			/* LOGIN page */
			{
				// alertAccept();

				setText(userField, usr, " ");
				setText(passwordField, psw, " ");
				// setText(passwordField, psw + Keys.ENTER);

				// Errors in JS on the page
				//if (getDriverType().equalsIgnoreCase("FF")) {
				//	Thread.sleep(7000L);
				//}

				makeScreenCapture("Login page.", getWebDriver());
				click(loginBtn);

				findElement(homeSpan);

				setCookiesInstance(getWebDriver().manage().getCookies());
				setUrlToken(getWebDriver().getCurrentUrl());

				attachText("Cookies", getAllCookies());
				makeScreenCapture("Main page.", getWebDriver());
			}
		}
	
		@Step("Check new Session ...")
		private void checkSession(Set<Cookie> cookiesInstance, String urlToken) throws InterruptedException {
			
			if (getWebDriver().getCurrentUrl().contains("ErrorMsg")) {

				setCookiesInstance(null);
				setUrlToken(null);
				makeScreenCapture("FAILD - cookies", getWebDriver());

				acceptLoginFaild();
				setLogin();

				LOG.warn("Session was reset by remote server. Clean Cookies and urlToken.");
			} else if (!getWebDriver().getCurrentUrl().contains("ErrorMsg")) {
			}
			LOG.debug("Url Token - OK.");
		}
		
		@Step("Add cookies.")
		private void addCookies(final Set<Cookie> cookiesInstance, final String urlToken) throws InterruptedException {
			// openURL(Config.getProperty(START_URL));

			// Set<Cookie> cookiesInstance = getCookiesInstance();
			// String urlToken = getUrlToken();

			attachText("New Cookies.", getAllCookies());
			// try {
			if (getWebDriver() != null) {
				getWebDriver().manage().deleteAllCookies();
				// alertAccept();

				for (Cookie cookie : cookiesInstance) {
					getWebDriver().manage().addCookie(cookie);
				}

				LOG.debug("Cookies was added ...");
				// re-visit the page
				openURL(urlToken);

				// System.out.println("urlToken "+urlToken);
				checkSession(cookiesInstance, urlToken);
				// waitElementS(homeSpan,"");

				// TODO //UnhandledAlertException: unexpected alert open:
				// at
				// core.template.BasePage.waitUntilElementPresent(BasePage.java:364)
				// at core.template.BasePage.findElement(BasePage.java:121)
				// at core.template.BasePage.addCookies(BasePage.java:1335)
				// at core.template.BasePage.logIn(BasePage.java:673)
				findElement(homeSpan);

				// LOG.debug("Cookies was added ...");
			} else {
				LOG.error("Can't add cookies - WebDriver empty!");
			}
		}
		
		private void acceptLoginFaild() {
			final By acceptLoginFaildBtn = By.xpath(
					"//div[contains(@id,'dijit_Dialog') and @role='dialog' and not(contains(@style,'display: none'))]//span[contains(@id,'Button') and contains(@class,'ButtonText') and normalize-space(text())='OK']");

			if (waitElementS(acceptLoginFaildBtn, "") != 0) {
				click(acceptLoginFaildBtn);
				LOG.warn("Was found 'Login Faild dialog.'");
			} else {
				LOG.debug("Was NOT found 'Login Faild'");
			}
		}
		
}// END OF CLASS
