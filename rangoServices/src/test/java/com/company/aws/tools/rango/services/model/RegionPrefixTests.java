package com.company.aws.tools.rango.services.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RegionPrefixTests {
	
	public final String INVALID_REGION_PREFIX = "IL";
	public final String VALID_TEST_REGION = "ap-northeast-3";
	

	@Test
	void isValid_returnsFalse_whenRegionPrefixIsInvalid() {
		assertFalse(RegionPrefix.isValid(INVALID_REGION_PREFIX));
	}
	
	@Test
	void isValid_returnsTrue_whenRegionPrefixIsValid() {
		assertTrue(RegionPrefix.isValid(RegionPrefix.EU.toString()));
	}
	
	@Test
	void startsWithValidPrefix_returnsFalse_whenRegion_DoesNotStartWithValidPrefix() {
		assertFalse(RegionPrefix.startsWithValidPrefix(INVALID_REGION_PREFIX));
	}
	
	@Test
	void startsWithValidPrefix_returnsTrue_whenRegion_startsWithValidPrefix() {
		assertTrue(RegionPrefix.startsWithValidPrefix(VALID_TEST_REGION));
	}
	
}
