package com.company.aws.tools.rango.services.amazon.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.exceptions.OkHttpClientException;
import com.company.aws.tools.rango.services.http.client.HttpResponse;
import com.company.aws.tools.rango.services.http.client.HttpResponseCode;
import com.company.aws.tools.rango.services.http.client.OkHttpClientService;
import com.company.aws.tools.rango.services.model.IpRanges;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = {AmazonAWSClientService.class, OkHttpClientService.class})
public class AmazonAWSClientServiceTests {
	
	@MockBean
	private OkHttpClientService httpClient;
	
	@Autowired
	private AmazonAWSClientService amazonClient;
	
	@Test
	void getIpRanges_throwsException_whenReuqestIsSuccessfulButResponseBodyIsEmpty() {

		when(httpClient.get(Mockito.anyString())).thenReturn(getResponseWithEmptyBody());
		
		assertThrows(AmazonAWSClientException.class, () -> {
			amazonClient.getIpRanges();
			
		});
	}
	
	private HttpResponse getResponseWithEmptyBody() {
		return new HttpResponse(HttpResponseCode.REQUEST_SUCCESSFUL.getNumValue(), "");
	}
	
	
	@Test
	void getIpRanges_throwsException_whenResponseCodeEqualsBadRequest() {
		
		when(httpClient.get(Mockito.anyString())).thenReturn(getBadRequestResponse());
		
		assertThrows(AmazonAWSClientException.class, () -> {
			amazonClient.getIpRanges();
			
		});
	}
	
	private HttpResponse getBadRequestResponse() {
		return new HttpResponse(HttpResponseCode.BAD_REQUEST.getNumValue(), "This was a bad request");
	}
	
	@Test
	void getIpRanges_throwsException_whenResponseCodeIsUnexpected() {
		
		when(httpClient.get(Mockito.anyString())).thenReturn(getResponseWithUnexpectedCode());
		
		assertThrows(AmazonAWSClientException.class, () -> {
			amazonClient.getIpRanges();
			
		});
	}
	
	private HttpResponse getResponseWithUnexpectedCode() {
		return new HttpResponse(HttpResponseCode.UNEXPECTED_CODE.getNumValue(), "This has unexpected code");
	}

	@Test
	void getIpRanges_throwsException_whenHttpClientThrowsException() {
		
		when(httpClient.get(Mockito.anyString())).thenThrow(OkHttpClientException.class);
		
		assertThrows(AmazonAWSClientException.class, () -> {
			amazonClient.getIpRanges();
			
		});
	}
	
	@Test
	void getIpRanges_returnsNotNullIpRanges_whenResponseBodyIsCorrect() throws JsonProcessingException {
		
		when(httpClient.get(Mockito.anyString())).thenReturn(getCorrectRequestResponse());
		
		IpRanges ipRanges = amazonClient.getIpRanges();
		
		assertNotNull(ipRanges);
	}

	private HttpResponse getCorrectRequestResponse() throws JsonProcessingException {
		IpRanges ipRanges = new IpRanges();
		ObjectMapper mapper = new ObjectMapper();
		return new HttpResponse(HttpResponseCode.REQUEST_SUCCESSFUL.getNumValue(), mapper.writeValueAsString(ipRanges));
	}
	
}
