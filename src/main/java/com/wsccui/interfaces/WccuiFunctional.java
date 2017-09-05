package com.wsccui.interfaces;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.wsccui.exception.UnknowTypeCreditCardException;
import com.wsccui.exception.InvalidItemException;
import com.wsccui.funcsuite.CreateOrder;

import core.exception.NullTaxesAs400Exceptions;

public interface WccuiFunctional {

	/* Create order */
	//public CreateOrder landingPage();
	//public CreateOrder logIn() throws InterruptedException;
	public CreateOrder createDraftOrder(final String concept) throws InterruptedException, KeyManagementException, NoSuchAlgorithmException;
	
	public CreateOrder selectCustomerCriteria(final String customerID, final String firstName, final String lastName, final String middleName,
			final String eMail, final String address_1, final String address_2, final String postalCode,
			final String dayTelephone, final String eveningTelephone) throws InterruptedException;
	public CreateOrder enterItem(final String bundlesItems, final String storeNumber, final String orderType)  throws InterruptedException, InvalidItemException, NumberFormatException, SocketException, UnknownHostException, Exception;

	public CreateOrder setBillToAdress(
			final String firstName,
			final String lastName,
			final String middleName,
			final String eMail,

			final String address_1,
			final String address_2,
			final String dayTelephone,

			final String eveningTelephone,
			final String postalCode
			//,final String customerPreference
			) throws InterruptedException, ClassNotFoundException, SQLException, NullTaxesAs400Exceptions;
	public CreateOrder setPaymentTypeAndPromo(final String paymentType, final String expiry, final String promCode, final String otherCards, final String pinCard) throws InterruptedException, UnknowTypeCreditCardException; //+
	
	/* not Implement yet*/
	public CreateOrder chooseVariation();
	//public WSCCUIMainPage setPersonalize();
	public CreateOrder changeAddress(final String bundlesLines) throws InterruptedException;
	
	public CreateOrder changeCarrierService(final String carrierService)throws InterruptedException;
	public CreateOrder setShipmentHolds();
	public CreateOrder setInstructions();
	
	public CreateOrder setGiftOptions(final String giftOptions) throws InterruptedException;
	
	
	
	/**
	 * Order number
	 * Order status
	 * */
	public String getOrderNumber() throws InterruptedException; 
	public CreateOrder logOut() throws InterruptedException;

	
	/* get Order Status */
	public CreateOrder setOrderSearchCriteria();
	public String getOrderStatus(final String orderNumber) throws InterruptedException;
	
	
}
