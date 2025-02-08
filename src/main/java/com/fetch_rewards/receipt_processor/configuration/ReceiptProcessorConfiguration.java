package com.fetch_rewards.receipt_processor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This is the configuration class for holding variables to be used when
 * calculating points for a receipt. The values for this configuration are
 * prefixed with receipt.processor and can be found within the application
 * properties file.
 */
@Configuration
@ConfigurationProperties(prefix = "receipt.processor")
public class ReceiptProcessorConfiguration {
	private int centsForPoints;
	private int pointsAwardedCentsForPoints;
	private double totalDivisibleBy;
	private int pointsAwardedTotalDivisibleBy;
	private int itemsGrouping;
	private int pointsAwardedItemsGrouping;
	private int descriptionLengthDivisibleBy;
	private double pointsAwardedDescriptionLength;
	private int pointsAwardedForDayOfMonthOdd;
	private int pointsAwardedForDayOfMonthEven;
	private int timeLowerBoundHours;
	private int timeLowerBoundMinutes;
	private int timeUpperBoundHours;
	private int timeUpperBoundMinutes;
	private int pointsAwardedTimeThreshold;

	public int getCentsForPoints() {
		return centsForPoints;
	}

	public void setCentsForPoints(int centsForPoints) {
		this.centsForPoints = centsForPoints;
	}

	public int getPointsAwardedCentsForPoints() {
		return pointsAwardedCentsForPoints;
	}

	public void setPointsAwardedCentsForPoints(int pointsAwardedCentsForPoints) {
		this.pointsAwardedCentsForPoints = pointsAwardedCentsForPoints;
	}

	public double getTotalDivisibleBy() {
		return totalDivisibleBy;
	}

	public void setTotalDivisibleBy(double totalDivisibleBy) {
		this.totalDivisibleBy = totalDivisibleBy;
	}

	public int getPointsAwardedTotalDivisibleBy() {
		return pointsAwardedTotalDivisibleBy;
	}

	public void setPointsAwardedTotalDivisibleBy(int pointsAwardedTotalDivisibleBy) {
		this.pointsAwardedTotalDivisibleBy = pointsAwardedTotalDivisibleBy;
	}

	public int getItemsGrouping() {
		return itemsGrouping;
	}

	public void setItemsGrouping(int itemsGrouping) {
		this.itemsGrouping = itemsGrouping;
	}

	public int getPointsAwardedItemsGrouping() {
		return pointsAwardedItemsGrouping;
	}

	public void setPointsAwardedItemsGrouping(int pointsAwardedItemsGrouping) {
		this.pointsAwardedItemsGrouping = pointsAwardedItemsGrouping;
	}

	public int getDescriptionLengthDivisibleBy() {
		return descriptionLengthDivisibleBy;
	}

	public void setDescriptionLengthDivisibleBy(int descriptionLengthDivisibleBy) {
		this.descriptionLengthDivisibleBy = descriptionLengthDivisibleBy;
	}

	public double getPointsAwardedDescriptionLength() {
		return pointsAwardedDescriptionLength;
	}

	public void setPointsAwardedDescriptionLength(double pointsAwardedDescriptionLength) {
		this.pointsAwardedDescriptionLength = pointsAwardedDescriptionLength;
	}

	public int getPointsAwardedForDayOfMonthOdd() {
		return pointsAwardedForDayOfMonthOdd;
	}

	public void setPointsAwardedForDayOfMonthOdd(int pointsAwardedForDayOfMonthOdd) {
		this.pointsAwardedForDayOfMonthOdd = pointsAwardedForDayOfMonthOdd;
	}

	public int getPointsAwardedForDayOfMonthEven() {
		return pointsAwardedForDayOfMonthEven;
	}

	public void setPointsAwardedForDayOfMonthEven(int pointsAwardedForDayOfMonthEven) {
		this.pointsAwardedForDayOfMonthEven = pointsAwardedForDayOfMonthEven;
	}

	public int getTimeLowerBoundHours() {
		return timeLowerBoundHours;
	}

	public void setTimeLowerBoundHours(int timeLowerBoundHours) {
		this.timeLowerBoundHours = timeLowerBoundHours;
	}

	public int getTimeLowerBoundMinutes() {
		return timeLowerBoundMinutes;
	}

	public void setTimeLowerBoundMinutes(int timeLowerBoundMinutes) {
		this.timeLowerBoundMinutes = timeLowerBoundMinutes;
	}

	public int getTimeUpperBoundHours() {
		return timeUpperBoundHours;
	}

	public void setTimeUpperBoundHours(int timeUpperBoundHours) {
		this.timeUpperBoundHours = timeUpperBoundHours;
	}

	public int getTimeUpperBoundMinutes() {
		return timeUpperBoundMinutes;
	}

	public void setTimeUpperBoundMinutes(int timeUpperBoundMinutes) {
		this.timeUpperBoundMinutes = timeUpperBoundMinutes;
	}

	public int getPointsAwardedTimeThreshold() {
		return pointsAwardedTimeThreshold;
	}

	public void setPointsAwardedTimeThreshold(int pointsAwardedTimeThreshold) {
		this.pointsAwardedTimeThreshold = pointsAwardedTimeThreshold;
	}

}
