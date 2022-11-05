package com.company.aws.tools.rango.services.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ParamCheckerTests {
	
	private final String EMPTY_STRING = "";
	
	@Test
	void throwIfEmpty_throwsExceptionWhenInputIsEmpty() {

		assertThrows(IllegalArgumentException.class, () -> {
			ParamChecker.throwIfEmpty(EMPTY_STRING);
		});
		
	}

}
