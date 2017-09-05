package com.wsccui.exception;

import org.apache.log4j.Logger;

public class InvalidBalanceInquiryException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1160673096711906818L;
	static final Logger LOG = Logger.getLogger(InvalidBalanceInquiryException.class.getName());
	
	public InvalidBalanceInquiryException(String msg) {
		super(msg);
		LOG.error(msg);
	}

}
