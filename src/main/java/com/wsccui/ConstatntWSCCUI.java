package com.wsccui;

//import java.sql.Connection;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Set;
//import org.apache.maven.doxia.logging.Log;
import org.openqa.selenium.Cookie;
//import org.openqa.selenium.WebDriver;

public class ConstatntWSCCUI {

	// For attacment

	private static final ThreadLocal<String> SUB_TOTAL_EXPECTED_CONTAINER = new ThreadLocal<>();

	public static synchronized void setSubTotalExpected(final String subTotalExpected) {
		SUB_TOTAL_EXPECTED_CONTAINER.set(subTotalExpected);
	}

	public static synchronized String getSubTotalExpected() {
		return SUB_TOTAL_EXPECTED_CONTAINER.get();
	}

	public final static String ALERT_ID_REGEX = "(?:<span)(?:.*)(\\b\\d{23,24}\\b)(?:\\s)*?(?:<\\/span>)";
	// = "(?:<span)(?:.*)(\\d{23})(?:<\\/span>)";
	public final static String TOTAL_AMOUNT_APP = "(?:Total\\scharge\\s)(?:\\()?(?:\\$)(?:\\s)*((?:-)?(?:\\s)*([0-9,])*\\.\\d{0,2})";

	
	public final static String TOTAL_AMOUNT_PAYMENT_CENTS = "(?:\\$)?(?:[0-9,\\s]+?\\.)(\\d{2})";
	public final static String TOTAL_AMOUNT_PAYMENT = "(?:\\$)?([0-9,\\s]+?\\.\\d{2})";
	//public final static String WRN_MSG = "Empty or NOT correct value of [";

	private static Set<Cookie> cookiesInstance;
	private static String urlToken;

	public static Set<Cookie> getCookiesInstance() {
		return ConstatntWSCCUI.cookiesInstance;
	}

	public static void setCookiesInstance(final Set<Cookie> NEW_VALUE) {

		if (cookiesInstance != null) {
			// ConstatntWSCCUI.cookiesInstance.addAll(NEW_VALUE);
			ConstatntWSCCUI.cookiesInstance.clear();
			ConstatntWSCCUI.cookiesInstance = NEW_VALUE;
		} else if (cookiesInstance == null) {
			ConstatntWSCCUI.cookiesInstance = NEW_VALUE;
		}
		// else if(cookiesInstance != null && NEW_VALUE == null){
		// ConstatntWSCCUI.cookiesInstance = NEW_VALUE;
		// }
	}

	public static String getUrlToken() {
		return ConstatntWSCCUI.urlToken;
	}

	public static void setUrlToken(final String NEW_VALUE) {
		ConstatntWSCCUI.urlToken = NEW_VALUE;
		// System.out.println("Set url"+ConstatntWSCCUI.urlToken);
	}
}
