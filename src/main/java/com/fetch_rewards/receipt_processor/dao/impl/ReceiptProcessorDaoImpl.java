package com.fetch_rewards.receipt_processor.dao.impl;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.fetch_rewards.receipt_processor.dao.ReceiptProcessorDao;
import com.fetch_rewards.receipt_processor.domain.Points;
import com.fetch_rewards.receipt_processor.domain.ReceiptId;

/**
 * This is the DAO implementation for storing and retrieving receipt UUIDs and
 * their corresponding points
 */
@Repository
public class ReceiptProcessorDaoImpl implements ReceiptProcessorDao {

	// Local storage for UUIDs and corresponding Receipt points
	private HashMap<String, Integer> receiptPointsMap = new HashMap<String, Integer>();

	/**
	 * This method will take an incoming UUID and fetch its points from the data
	 * store. It will return a new Points record with the UUID's corresponding
	 * points. If the UUID does not exist it will return null.
	 * 
	 * @param id is the UUID for a receipt
	 * @return Points is the record for holding points awarded to a specific
	 *         receipt. This will return null if the incoming UUID does not exist in
	 *         the data store.
	 */
	@Override
	public Points getPoints(String id) {
		return receiptPointsMap.get(id) != null ? new Points(receiptPointsMap.get(id)) : null;
	}

	/**
	 * This method takes an incoming UUID and corresponding points and saves them to
	 * the data store. It will return a ReceiptId containing the given Receipt's
	 * UUID. This can be used later to fetch the points for the corresponding
	 * Receipt.
	 * 
	 * @param id     is the UUID for a given Receipt
	 * @param points are the points calculated for the details associated with a
	 *               given Receipt
	 * @Return ReceiptID is a record containing the Receipt's UUID to be used later
	 *         for fetching the Receipt's points.
	 */
	@Override
	public ReceiptId saveReceiptPoints(String id, int points) {
		receiptPointsMap.put(id, points);
		return new ReceiptId(id);
	}

}
