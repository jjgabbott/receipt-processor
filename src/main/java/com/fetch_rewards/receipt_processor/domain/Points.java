package com.fetch_rewards.receipt_processor.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public record Points(@Schema(description = "The points corresponding to a given receipt.") int points) {
}
