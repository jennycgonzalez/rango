package com.company.aws.tools.rango.services.http.client;

public enum HttpResponseCode {
	REQUEST_SUCCESSFUL(200),
	NO_CONTENT(204),
	BAD_REQUEST(400),
	FORBIDDEN(403),
	NOT_FOUND(404),
	UNEXPECTED_CODE(1000);
	
	private int code;
	HttpResponseCode(int value){
		this.code = value;
	}
	
	public static HttpResponseCode fromHttpCode(int code) {
		for(HttpResponseCode value : values()) {
			if(value.code == code) {
				return value;
			}
		}
		return UNEXPECTED_CODE;
	}
	
	public int getNumValue() {
		return code;
	}
	
	
}
