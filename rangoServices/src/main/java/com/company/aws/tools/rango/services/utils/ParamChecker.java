package com.company.aws.tools.rango.services.utils;

public final class ParamChecker {
	
	private ParamChecker() {}
	
	public static void throwIfBlank(String param, String message) {
		if(param.isBlank()) {
			throw new IllegalArgumentException(message);
		}
	}
	
	

}
