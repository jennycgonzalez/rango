package com.company.aws.tools.rango.services.amazon.client;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.http.client.HttpResponse;
import com.company.aws.tools.rango.services.http.client.HttpResponseCode;
import com.company.aws.tools.rango.services.http.client.OkHttpClientService;

@SpringBootTest(classes = {AmazonAWSClientService.class, OkHttpClientService.class})
public class AmazonAWSClientServiceTests {
	
	@MockBean
	private OkHttpClientService httpClient;
	
	@Autowired
	private AmazonAWSClientService amazonClient;
	
	@Test
	void getIpRanges_throwsException_whenResponseIsEmpty() {

		when(httpClient.get(Mockito.anyString())).thenReturn(getEmptyResponse());
		
		assertThrows(AmazonAWSClientException.class, () -> {
			amazonClient.getIpRanges();
			
		});
	}
	
	private HttpResponse getEmptyResponse() {
		return new HttpResponse(HttpResponseCode.NO_CONTENT.getNumValue(), "");
	}
	
	
	@Test
	void getIpRanges_throwsException_whenResponseCodeEqualsBadRequest() {
		
		when(httpClient.get(Mockito.anyString())).thenReturn(getBadRequestResponse());
		
		assertThrows(AmazonAWSClientException.class, () -> {
			amazonClient.getIpRanges();
			
		});
	}
	
	private HttpResponse getBadRequestResponse() {
		return new HttpResponse(HttpResponseCode.BAD_REQUEST.getNumValue(), "");
	}

}
