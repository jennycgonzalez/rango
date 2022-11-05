package com.company.aws.tools.rango.services.utils;

import org.apache.commons.lang3.StringUtils;

public final class ParamChecker {
	
	private ParamChecker() {}
	
	public static void throwIfBlank(String param, String message) {
		if(StringUtils.isBlank(param)) {
			throw new IllegalArgumentException(message);
		}
	}
	
	

}
