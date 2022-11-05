package com.company.aws.tools.rango.services.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import okhttp3.Request;

@SpringBootTest
class OkHttpClientTests {
	
	private final String IP_RANGES_URL = "https://ip-ranges.amazonaws.com/ip-ranges.json";
	
	OkHttpClientService client;

	@Test
	void getRequest_becomesSuccessfulResponse() {
		Request request = buildGetRequest(IP_RANGES_URL);
		
		HttpResponse response = client.get(null);
		
		assertIsSuccessful(response);
	}

	private Request buildGetRequest(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void assertIsSuccessful(HttpResponse response) {
		// TODO Auto-generated method stub
		assertEquals(HttpResponseCode.REQUEST_SUCCESSFUL.getNumValue(), response.getStatusCode());
	}


}
