package com.fetch_rewards.receipt_processor.dao;

import com.fetch_rewards.receipt_processor.domain.Points;
import com.fetch_rewards.receipt_processor.domain.ReceiptId;

/**
 * This is the interface for the ReceiptProcessorDao
 */
public interface ReceiptProcessorDao {
	/**
	 * Expects to retrieve points from a data store and return a Points record based
	 * on an incoming UUID
	 */
	public Points getPoints(String id);

	/**
	 * Expects to save points for an incoming UUID to a data store
	 */
	public ReceiptId saveReceiptPoints(String id, int points);
}
