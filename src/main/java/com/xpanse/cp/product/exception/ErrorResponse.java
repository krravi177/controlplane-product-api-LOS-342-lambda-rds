package com.xpanse.cp.product.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private LocalDateTime errorTime;
	private int statusCode;
	private String errorMessage;

	public ErrorResponse(LocalDateTime errorTime, String errorMessage) {
		super();
		this.errorTime = errorTime;
		this.errorMessage = errorMessage;
	}
	
	public ErrorResponse(LocalDateTime errorTime, String errorMessage, int statusCode) {
		super();
		this.errorTime = errorTime;
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
	}

}
