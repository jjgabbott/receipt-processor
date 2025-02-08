package com.fetch_rewards.receipt_processor.exception;

public class BadRequestException extends RuntimeException {

	/**
	 * This is a generated serial ID used as a precaution for if this class is ever serialized.
	 */
	private static final long serialVersionUID = 4811559873583679956L;
	
	public BadRequestException(String message) {
		super(message);
	}

}
