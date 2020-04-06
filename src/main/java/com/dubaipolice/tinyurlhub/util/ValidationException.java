package com.dubaipolice.tinyurlhub.util;

@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {

	private String messageCode;

	public ValidationException(String msg) {
		super(msg);
	}

	public ValidationException(String msg, String messageCode) {
		super(msg);
		this.messageCode = messageCode;
	}

	public String getMessageCode() {
		return messageCode;
	}

}
