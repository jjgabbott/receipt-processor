package com.fetch_rewards.receipt_processor.service;

import com.fetch_rewards.receipt_processor.domain.Points;
import com.fetch_rewards.receipt_processor.domain.Receipt;
import com.fetch_rewards.receipt_processor.domain.ReceiptId;
import com.fetch_rewards.receipt_processor.exception.BadRequestException;
import com.fetch_rewards.receipt_processor.exception.ReceiptNotFoundException;

/**
 * This is the interface for the ReceiptProcessorService
 */
public interface ReceiptProcessorService {

	/**
	 * Expects to retrieve points and return a Points record based on an incoming
	 * UUID or throw a ReceiptNotFoundException if points for the UUID does not
	 * exist
	 * @throws ReceiptNotFoundException
	 */
	public Points getPoints(String id) throws ReceiptNotFoundException;

	/**
	 * Expects to calculate the total number of points for a Receipt based on the
	 * receipt's details and send those points to be saved with the receipt's
	 * corresponding UUID. Returns the receipt's UUID if the points were calculated
	 * and saved correctly.
	 * @throws BadRequestException 
	 */
	public ReceiptId processReceipt(Receipt receipt) throws BadRequestException;
}
