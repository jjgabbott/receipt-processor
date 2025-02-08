package com.fetch_rewards.receipt_processor.exception;

/**
 * This is a custom exception to be thrown when a Receipt's UUID does not exist in a data store.
 */
public class ReceiptNotFoundException extends RuntimeException{
	
	/**
	 * This is a generated serial ID used as a precaution for if this class is ever serialized.
	 */
	private static final long serialVersionUID = -5384471781110150159L;

	public ReceiptNotFoundException(String message) {
		super(message);
	}
}
