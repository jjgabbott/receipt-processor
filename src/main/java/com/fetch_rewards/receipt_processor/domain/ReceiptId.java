package com.fetch_rewards.receipt_processor.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReceiptId(@Schema(description = "The id of a receipt") String id) {
}
