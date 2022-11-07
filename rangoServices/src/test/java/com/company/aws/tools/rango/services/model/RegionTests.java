package com.company.aws.tools.rango.services.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.company.aws.tools.rango.services.model.Region;

public class RegionTests {
	
	public final String INVALID_REGION = "IL";

	@Test
	void isValid_returnsFalse_whenRegionIsInvalid() {
		assertFalse(Region.isValid(INVALID_REGION));
	}
	
	@Test
	void isValid_returnsTrue_whenRegionValid() {
		assertTrue(Region.isValid(Region.EU.toString()));
	}
	
}
