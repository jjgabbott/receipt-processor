package com.fetch_rewards.receipt_processor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ReceiptNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleResourceNotFound(ReceiptNotFoundException ex) {
		//Log the exception
		System.out.println(ex);
		return new ErrorResponse("No receipt found for that ID.");
	}
	
	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleBadRequest(BadRequestException ex) {
		//Log the exception
		System.out.println(ex);
		return new ErrorResponse("The receipt is invalid.");
	}
}
