package com.product.exception;

public class AuthFailException extends Exception {

	/**
	 * deafult version id
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private int errorCode;

	public AuthFailException(String message, int errorCode) {
		super(message);
		this.message = message;
		this.errorCode = errorCode;
	}

	public AuthFailException() {
	}

	public String getMessage() {
		return message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
