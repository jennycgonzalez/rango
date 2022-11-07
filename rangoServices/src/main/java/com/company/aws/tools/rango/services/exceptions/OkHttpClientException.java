package com.company.aws.tools.rango.services.exceptions;

public class OkHttpClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5342166416501745477L;
	
	public OkHttpClientException(String message, Throwable exception){
		super(message, exception);
	}
	
	public OkHttpClientException(Throwable exception){
		super(exception);
	}
}
