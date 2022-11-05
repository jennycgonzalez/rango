package com.company.aws.tools.rango.services.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ParamCheckerTests {
	
	private final String EMPTY_STRING = "";
	
	@Test
	void throwIfBlank_throwsException_whenInputIsEmpty() {
		assertThrows(IllegalArgumentException.class, () -> {
			ParamChecker.throwIfBlank(EMPTY_STRING, "Param must not be blank");
		});
	}

}
