package com.company.aws.tools.rango.services.http.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {OkHttpClientService.class})
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class})
class OkHttpClientTests {
	
	private final String IP_RANGES_URL = "https://ip-ranges.amazonaws.com/ip-ranges.json";
	
	@Autowired
	OkHttpClientService client;

	@Test
	void getRequest_becomesSuccessfulResponse() {
		assertNotNull(client);
		HttpResponse response = client.get(IP_RANGES_URL);
		
		assertRequestIsSuccessful(response);
	}

	private void assertRequestIsSuccessful(HttpResponse response) {
		// TODO Auto-generated method stub
		assertEquals(HttpResponseCode.REQUEST_SUCCESSFUL.getNumValue(), response.getStatusCode());
	}


}
