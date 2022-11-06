package com.company.aws.tools.rango.services.exceptions;

public class AmazonAWSClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2670208607763604151L;

	public AmazonAWSClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
