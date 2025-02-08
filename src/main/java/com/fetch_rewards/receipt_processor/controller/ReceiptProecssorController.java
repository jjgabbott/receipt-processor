package com.fetch_rewards.receipt_processor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fetch_rewards.receipt_processor.domain.Points;
import com.fetch_rewards.receipt_processor.domain.Receipt;
import com.fetch_rewards.receipt_processor.domain.ReceiptId;
import com.fetch_rewards.receipt_processor.exception.BadRequestException;
import com.fetch_rewards.receipt_processor.exception.ReceiptNotFoundException;
import com.fetch_rewards.receipt_processor.service.ReceiptProcessorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This is the controller for exposing end points to be used for calculating and
 * storing points for receipts and fetching those points from a data store.
 */
@RestController
@RequestMapping("/receipts")
public class ReceiptProecssorController {

	@Autowired
	private ReceiptProcessorService receiptProcessor;

	/**
	 * This method exposes the /receipts/process end point to process the points
	 * associated with an incoming Receipt. If the receipt is formatted correctly,
	 * points will be calculated and saved within a data store. A ReceiptId record
	 * containing the receipt's UUID will be returned on successful processing
	 * (along with a 200 status code). If processing fails, the exception will be
	 * logged and a 400 status request will be returned.
	 * 
	 * @param receipt is a Receipt containing details to be used for calculating the
	 *                number of points associated with a specific receipt
	 * @return ReceiptId is the record containing the UUID for the incoming receipt
	 *         that can be used later for fetching the points associated with this
	 *         receipt
	 * @throws BadRequestException
	 */
	@Operation(summary = "Submits a receipt for processing.", description = "Submits a receipt for processing.")
	@PostMapping("process")
	public ResponseEntity<ReceiptId> processReceipt(@RequestBody Receipt receipt) throws BadRequestException {
		return new ResponseEntity<ReceiptId>(receiptProcessor.processReceipt(receipt), HttpStatus.OK);
	}

	/**
	 * This method exposes the /receipts/{id}/points end point and will retrieve the
	 * points associated with the incoming UUID. If points exist for the incoming
	 * UUID, a Points record containing the receipt's corresponding points will be
	 * returned (along with a 200 status code). If the UUID does not exist in the
	 * data store, a 404 status code will be returned. If there are any other issues
	 * retrieving the points for the corresponding UUID, a 500 status code will be
	 * returned.
	 * 
	 * @param id is the UUID for a receipt
	 * @return Points is a record containing the points associated with the
	 *         receipt's incoming UUID
	 * @throws ReceiptNotFoundException
	 */
	@Operation(summary = "Returns the points awarded for the receipt.", description = "Returns the points awarded for the receipt.")
	@GetMapping("/{id}/points")
	public ResponseEntity<Points> getPoints(
			@Parameter(description = "The ID of the receipt.", required = true, schema = @Schema(type = "string", pattern = "^\\S+$"
			)) @PathVariable String id) throws ReceiptNotFoundException {
		return new ResponseEntity<Points>(receiptProcessor.getPoints(id), HttpStatus.OK);
	}
}
