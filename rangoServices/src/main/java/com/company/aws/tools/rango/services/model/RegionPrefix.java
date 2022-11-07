package com.company.aws.tools.rango.services.model;

public enum RegionPrefix {
	EU, US, AP, CN,
	SA, AF, CA;
	
	public static boolean isValid(String regionPrefix) {
		for(RegionPrefix prefix : values()) {
			if(prefix.toString().equals(regionPrefix)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean startsWithValidPrefix(String region) {
		for(RegionPrefix prefix : values()) {
			if(region.startsWith(prefix.toString().toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
}
