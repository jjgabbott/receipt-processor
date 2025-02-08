package com.fetch_rewards.receipt_processor.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public record Item(
		@Schema(description = "The Short Product Description for the item.", pattern = "^[\\w\\s\\-]+$", example = "Mountain Dew 12PK") String shortDescription,
		@Schema(description = "The total price payed for this item.", pattern = "^\\d+\\.\\d{2}$", example = "6.49") String price) {
}
