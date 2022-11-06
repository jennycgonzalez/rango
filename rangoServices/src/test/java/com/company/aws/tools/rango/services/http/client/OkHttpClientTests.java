package com.company.aws.tools.rango.services.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import com.company.aws.tools.rango.services.exceptions.OkHttpClientException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {OkHttpClientService.class})
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class})
class OkHttpClientTests {
	
	private final String IP_RANGES = "https://ip-ranges.amazonaws.com/ip-ranges.json";
	private final String FAULTY_URL = "https://ip-ranges.amazonaws.com/ip-ranges-fake.json";
	private final String EMPTY_URL = "";
	
	@Autowired
	OkHttpClientService client;
	
	@Test
	void get_throwsException_whenUrlIsEmpty() {
		
		assertThrows(IllegalArgumentException.class, () -> {
			client.get(EMPTY_URL);
		});
		
	}
	
	@Test
	void get_throwsException_whenUrlIsNull() {
		
		assertThrows(IllegalArgumentException.class, () -> {
			client.get(null);
		});
		
	}
	
	@Test
	void get_returnsSuccessfulResponse_whenUrlIsCorrect() {
		HttpResponse response = client.get(IP_RANGES);
		
		assertIsSuccessful(response);
	}

	private void assertIsSuccessful(HttpResponse response) {
		assertEquals(HttpResponseCode.REQUEST_SUCCESSFUL.getNumValue(), response.getStatusCode());
	}

	@Test
	void get_returnsForbiddenAsResponse_whenUrlIsFaulty() {
		HttpResponse response = client.get(FAULTY_URL);
		
		assertIsFailure(response);
	}
	
	private void assertIsFailure(HttpResponse response) {
		assertEquals(HttpResponseCode.FORBIDDEN.getNumValue(), response.getStatusCode());
	}
	
	@Disabled("For this test internet connection must be unavailable")
	@Test
	void get_throwsException_whenThereIsNoInternetConnection() {
		
		assertThrows(OkHttpClientException.class, () -> {
			client.get(IP_RANGES);
		});
		
	}
	
}

