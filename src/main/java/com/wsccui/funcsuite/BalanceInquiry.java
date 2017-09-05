package com.wsccui.funcsuite;

//import static com.wsccui.ConstatntWSCCUI.*;
import static core.ThreadsStore.getTestInfo;
import static core.ThreadsStore.getWebDriver;
import static core.GlobalConstatnt.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.wsccui.exception.InputParametrNullException;
import com.wsccui.exception.InvalidBalanceInquiryException;
import com.wsccui.template.BasePageWsccui;
import ru.yandex.qatools.allure.annotations.Step;

public class BalanceInquiry extends BasePageWsccui {

	static final Logger LOG = Logger.getLogger(BalanceInquiry.class.getName());

	/* Create order */
	@Step("WSCCUI. Get balance inquiry ...")
	public boolean getBalanceSvcCard(final String paymentType, final String giftCardInfo,
			final String loyaltyRewardsCertificate) throws KeyManagementException, NoSuchAlgorithmException,
			InterruptedException, InputParametrNullException, InvalidBalanceInquiryException {

		final By balanceInquiryLink = By
				.xpath("//a[contains(@class,'LinkDerived') and normalize-space(text())='Balance Inquiry']");
		final By paymentTypeArrow = By.xpath(
				"//label[@dojoattachpoint='compLabelNode' and normalize-space(text())='Payment type:']/following::input[position()=1 and contains(@class,'ArrowButtonInner') and @role='presentation']");
		final By gCardNumberInput = By.xpath(
				"//label[contains(@for,'TextBox') and normalize-space(text())='Card number:']/following::input[position()=1 and contains(@id,'TextBox') and contains(@class,'InputInner')]");
		final By gCardPinInput = By.xpath(
				"//label[contains(@for,'TextBox') and normalize-space(text())='PIN:']//following::input[position()=1 and contains(@id,'TextBox') and contains(@class,'InputInner')]");
		final By loyaltyCertificateInput = By.xpath(
				"//label[contains(@for,'TextBox') and normalize-space(text())='Certificate #:']/following::input[position()=1 and contains(@id,'TextBox') and contains(@class,'InputInner')]");
		final By getFundsBtn = By.xpath("//span[contains(@id,'Button') and normalize-space(text())='Get Funds']");

		final By balanceLabel = By.xpath(
				"//label[contains(@for,'CurrencyTextBox') and normalize-space(text())='Balance:']/following::input[contains(@id,'CurrencyTextBox') and @role='textbox']");
		// By.xpath("//label[contains(@for,'CurrencyTextBox') and
		// normalize-space(text())='Balance:']/following::input[position()=2 and
		// @type='hidden']");
		// final By balanceLabelLoyalty = By.xpath("");
		final By errorMsg = By.xpath("//div[contains(@class,'errorMessage')]");
		boolean status = false;
		// double balance = 0.0;

		if (paymentType != null && !paymentType.isEmpty()) {

			/* Open start page */
			logIn();
			click(balanceInquiryLink);

			/*
			 * Gift Card/Merchandise Card/E-Gift Card 1) Gift 2) Merchandise 3)
			 * E-Gift
			 * 
			 * Loyalty Rewards Certificate 1) Loyalty
			 */

			if (paymentType.trim().equalsIgnoreCase("Gift") || paymentType.trim().equalsIgnoreCase("Merchandise")
					|| paymentType.trim().equalsIgnoreCase("E-Gift")) {

				if (giftCardInfo != null && !giftCardInfo.isEmpty() && giftCardInfo.contains(SPLIT_BY_BUNDLE)) {
					final String gCardNumber = (giftCardInfo.split(SPLIT_BY_BUNDLE))[0];
					final String gCardPin = (giftCardInfo.split(SPLIT_BY_BUNDLE))[1];
					LOG.info("TID["+Thread.currentThread().getId()+"] "+"Was parsed 'Gift card' info [" + giftCardInfo + "]");

					selectVisibleTextFromDropDownList(paymentTypeArrow, paymentType + " Card");
					setText(gCardNumberInput, gCardNumber);
					setText(gCardPinInput, gCardPin);

					makeScreenCapture("Gift_Merchandise_E-Gift.", getWebDriver());
					click(getFundsBtn);

					if (findElements(errorMsg) == 0) {
						status = true;
						makeScreenCapture("Balance inquiry.", getWebDriver());
						String balanceGcardString = getValueAttribute(balanceLabel, "value");

						if (balanceGcardString != null && !balanceGcardString.isEmpty()) {
							// balance =
							// Double.parseDouble(balanceGcardString.replaceAll(CHARS_NOT_ALLOWED,
							// ""));
							uploadToList(getTestInfo(), "Gift/Merchandise/E-Gift card [" + gCardNumber + "] balance ["
									+ balanceGcardString + "]");
						} else {
							LOG.error("Can't find Gift/Merchandise/E-Gift balance.");
						}

						// throw new InvalidBalanceInquiryException("Invalid
						// Balance Inquiry for card# ["+gCardNumber+"], PIN
						// ["+gCardPin+"]");
					}
				} else {
					LOG.warn(WRN_MSG + "Gift card]");
				}
			}

			// Venkateswarlu Chalikolimi <VChalikolimi@WSGC.com>
			// Go ahead with SVC Balance Inquiry as LRC Balance inquiry
			// functionality does not work from WCC.
			else if (paymentType.trim().equalsIgnoreCase("Loyalty")) {
				selectVisibleTextFromDropDownList(paymentTypeArrow, paymentType);
				setText(loyaltyCertificateInput, loyaltyRewardsCertificate);
				makeScreenCapture("Loyalty Rewards Certificate.", getWebDriver());
				click(getFundsBtn);

				if (findElements(errorMsg) == 0) {
					status = true;
					makeScreenCapture("Loyalty Rewards Certificate - Balance.", getWebDriver());
					String balanceLRCString = getValueAttribute(balanceLabel, "value");

					if (balanceLRCString != null && !balanceLRCString.isEmpty()) {
						// balance =
						// Double.parseDouble(balanceGcardString.replaceAll(CHARS_NOT_ALLOWED,
						// ""));
						uploadToList(getTestInfo(), "Loyalty Rewards Certificate [" + loyaltyRewardsCertificate
								+ "] balance [" + balanceLRCString + "]");
					} else {
						LOG.error("Can't find Loyalty Rewards Certificate balance.");
					}
					// throw new InvalidBalanceInquiryException("Invalid Balance
					// Inquiry for card# ["+gCardNumber+"], PIN
					// ["+gCardPin+"]");
				}
			} else {
				LOG.warn(WRN_MSG + "Loyalty Rewards Certificate]");
			}
		} else {
			throw new InputParametrNullException("Can't find payment type for Balance Inquiry.");
		}
		return status;
	}
}
