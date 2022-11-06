package com.company.aws.tools.rango.rest.controller;

public enum Region {
	EU, US, AP, CN,
	SA, AF, CA;
	
	public static boolean isValid(String region) {
		for(Region value : values()) {
			if(value.toString().equals(region)) {
				return true;
			}
		}
		return false;
	}
}
