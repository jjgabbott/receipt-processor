package com.fetch_rewards.receipt_processor.domain;

import java.util.ArrayList;
import java.util.Objects;

import com.fetch_rewards.receipt_processor.exception.BadRequestException;

import io.swagger.v3.oas.annotations.media.Schema;

public record Receipt(
		@Schema(description = "The name of the retailer or store the receipt is from.", pattern = "[\\w\\s\\-&]+$", example = "M&M Corner Market") String retailer,
		@Schema(description = "The date of the purchase printed on the receipt.", format = "date", example = "2022-01-01") String purchaseDate,
		@Schema(description = "The time of the purchase printed on the receipt. 24-hour time expected.", format = "time", example = "13:01") String purchaseTime,
		@Schema(minimum = "1") ArrayList<Item> items,
		@Schema(description = "The total amount paid on the receipt.", pattern = "^\\d+\\.\\d{2}$", example = "6.49") String total) {
	public Receipt {
		Objects.requireNonNull(retailer);
		Objects.requireNonNull(purchaseDate);
		Objects.requireNonNull(purchaseTime);
		Objects.requireNonNull(items);
		Objects.requireNonNull(total);
		if(items.isEmpty()) {
			throw new BadRequestException("Items cannot be empty");
		}
	}
}
