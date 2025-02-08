package com.fetch_rewards.receipt_processor.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fetch_rewards.receipt_processor.configuration.ReceiptProcessorConfiguration;
import com.fetch_rewards.receipt_processor.dao.ReceiptProcessorDao;
import com.fetch_rewards.receipt_processor.domain.Item;
import com.fetch_rewards.receipt_processor.domain.Points;
import com.fetch_rewards.receipt_processor.domain.Receipt;
import com.fetch_rewards.receipt_processor.domain.ReceiptId;
import com.fetch_rewards.receipt_processor.exception.BadRequestException;
import com.fetch_rewards.receipt_processor.exception.ReceiptNotFoundException;
import com.fetch_rewards.receipt_processor.service.ReceiptProcessorService;

/**
 * Service implementation for calculating the points that should be awarded for
 * a given Receipt. This service contains methods for calculating points,
 * sending the points to be saved, and retrieving points for a UUID
 * corresponding to a given Receipt.
 */
@Service
public class ReceiptProcessorServiceImpl implements ReceiptProcessorService {

	@Autowired
	private ReceiptProcessorDao receiptProcessorDao;
	@Autowired
	private ReceiptProcessorConfiguration receiptProcessorConfig;

	/**
	 * Service entry point for retrieving the points processed and stored for a
	 * given Receipt
	 * 
	 * @param id is the UUID for a Receipt
	 * @return Points is a record containing the previously processed points for the
	 *         given receipt and its corresponding UUID.
	 *         com.fetch_rewards.receipt_processor.exception.ReceiptNotFoundException
	 *         will be thrown if points for the given UUID do not exist.
	 */
	@Override
	public Points getPoints(String id) throws ReceiptNotFoundException {
		var points = receiptProcessorDao.getPoints(id);
		if (points == null) {
			throw new ReceiptNotFoundException("No points could be found for the id: " + id);
		}
		return points;
	}

	/**
	 * Service entry point for calculating the total points that should be awarded
	 * based on receipt details.
	 *
	 * @param receipt is the incoming receipt that contains the details that should
	 *                be used for calculating points. See
	 *                com.fetch_rewards.receipt_processor.domain.Receipt for
	 *                information regarding the Receipt record attributes.
	 * @return ReceiptId is the UUID generated from the incoming Receipt. See
	 *         com.fetch_rewards.receipt_processor.domain.ReceiptId for information
	 *         regarding the ReceiptId record.
	 * @throws BadRequestException
	 */
	@Override
	public ReceiptId processReceipt(Receipt receipt) throws BadRequestException {
		try {
			var points = 0;
			points += getPointsFromRetailerName(receipt.retailer());
			var total = Double.parseDouble(receipt.total());
			points += getPointsForCentsValue(total, receiptProcessorConfig.getCentsForPoints());
			points += getPointsIfMultipleOf(total, receiptProcessorConfig.getTotalDivisibleBy());
			points += getPointsForNumberOfItems(receipt.items(), receiptProcessorConfig.getItemsGrouping());
			for (var item : receipt.items()) {
				points += getPointsForItemDescription(item.shortDescription(), Double.parseDouble(item.price()),
						receiptProcessorConfig.getDescriptionLengthDivisibleBy());
			}

			points += getPointsForPurchaseDate(LocalDate.parse(receipt.purchaseDate()));
			points += getPointsForPurchaseTime(LocalTime.parse(receipt.purchaseTime()));
			return receiptProcessorDao
					.saveReceiptPoints(UUID.nameUUIDFromBytes(receipt.toString().getBytes()).toString(), points);
		} catch (Exception ex) {
			throw new BadRequestException("Malformed Request: " + receipt.toString());
		}
	}

	/**
	 * This method takes the retailer name from a Receipt and calculates points
	 * based on the number of alphanumeric characters in the retailers name (one
	 * point per alphanumeric character)
	 * 
	 * @param retailerName is the name of the retailer
	 * @return points awarded based on the number of alphanumeric characters in the
	 *         retailers name
	 */
	private int getPointsFromRetailerName(String retailerName) {
		var points = 0;
		for (int i = 0; i < retailerName.length(); i++) {
			if (Character.isLetterOrDigit(retailerName.charAt(i))) {
				points++;
			}
		}
		return points;
	}

	/**
	 * This method takes the cents value from the total of the receipt and compares
	 * it with a configured cents value for awarding points. For example, to check
	 * if the total value is a whole dollar amount, the configured cents value would
	 * be "0"
	 * 
	 * @param total      the total value from a receipt
	 * @param centsValue the cents value to be checked against
	 * @return points awarded if the cents value from the total matches the
	 *         configured cents value for awarding points
	 * 
	 */
	private int getPointsForCentsValue(double total, double centsValue) {
		return total - (int) total == centsValue ? receiptProcessorConfig.getPointsAwardedCentsForPoints() : 0;
	}

	/**
	 * This method takes the total from a Receipt and checks if it is divisible by a
	 * configured value before awarding points.
	 * 
	 * @param total            is the total from the Receipt
	 * @param divisibleByValue is the value in which the total should be divisible
	 *                         by before awarding points
	 * @return points configured to be awarded if the total is divisible by the
	 *         configured value
	 */
	private int getPointsIfMultipleOf(double total, double divisibleByValue) {
		return total % divisibleByValue == 0 ? receiptProcessorConfig.getPointsAwardedTotalDivisibleBy() : 0;
	}

	/**
	 * This method takes the number of items from a Receipt and divides them into
	 * groups. It then takes the number of groups and multiples them by a configured
	 * value to calculate the number of points to award. For example, if the item
	 * grouping is configured to be 2 and there are 4 items on the receipt, the
	 * total number of groups would be 4/2 = 2. This number is multiplied by the
	 * configured point value to calculate the points that should be awarded.
	 * 
	 * @param items        is the list of Items from a Receipt
	 * @param itemGrouping is the number of items needed in a group before awarding
	 *                     points
	 * @return points awarded for the number of complete item groups
	 */
	private int getPointsForNumberOfItems(ArrayList<Item> items, int itemGrouping) {
		return (items.size() / itemGrouping) * receiptProcessorConfig.getPointsAwardedItemsGrouping();
	}

	/**
	 * This method takes an item description, trims it, and then checks if it is
	 * divisible by a configured value. If it is, it will return points equal to the
	 * items price multiplied by a configured point value.
	 * 
	 * @param description      is the short description for an item on the Receipt
	 * @param price            is the price for the corresponding item
	 * @param divisibleByValue
	 * @return points awarded if the description is divisible by a configured value.
	 *         This is calculated by taking the item price and multiplying it by a
	 *         configured value.
	 */
	private int getPointsForItemDescription(String description, double price, int divisibleByValue) {
		return description.trim().length() % divisibleByValue == 0
				? (int) Math.ceil(price * receiptProcessorConfig.getPointsAwardedDescriptionLength())
				: 0;
	}

	/**
	 * This method awards a configured point value based on if the day of month
	 * purchase date is even or odd.
	 * 
	 * @param date is the purchase date from a Receipt
	 * @return points awarded based on the purchase day of month being even or odd
	 */
	private int getPointsForPurchaseDate(LocalDate date) {
		return date.getDayOfMonth() % 2 == 0 ? receiptProcessorConfig.getPointsAwardedForDayOfMonthEven()
				: receiptProcessorConfig.getPointsAwardedForDayOfMonthOdd();
	}

	/**
	 * This method takes the purchase time from a Receipt and calculates if the
	 * purchase time is within a configured threshold.
	 * 
	 * @param time is the purchase time from a Receipt
	 * @return points awarded if the purchase time from the receipt falls within a
	 *         configured threshold
	 */
	private int getPointsForPurchaseTime(LocalTime time) {
		var lowerTimeBound = LocalTime.of(receiptProcessorConfig.getTimeLowerBoundHours(),
				receiptProcessorConfig.getTimeLowerBoundMinutes());
		var upperTimeBound = LocalTime.of(receiptProcessorConfig.getTimeUpperBoundHours(),
				receiptProcessorConfig.getTimeUpperBoundMinutes());
		return time.compareTo(lowerTimeBound) > 0 & time.compareTo(upperTimeBound) < 0
				? receiptProcessorConfig.getPointsAwardedTimeThreshold()
				: 0;
	}

}
