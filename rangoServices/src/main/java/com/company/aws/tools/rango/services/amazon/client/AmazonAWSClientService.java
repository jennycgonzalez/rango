package com.company.aws.tools.rango.services.amazon.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.exceptions.OkHttpClientException;
import com.company.aws.tools.rango.services.http.client.HttpResponse;
import com.company.aws.tools.rango.services.http.client.HttpResponseCode;
import com.company.aws.tools.rango.services.http.client.OkHttpClientService;
import com.company.aws.tools.rango.services.model.IpRanges;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AmazonAWSClientService {

	private final String IP_RANGES = "https://ip-ranges.amazonaws.com/ip-ranges.json";
	
	@Autowired
	private OkHttpClientService httpClient;
	
	public IpRanges getIpRanges() throws AmazonAWSClientException {
		HttpResponse response = getAmazonResponse();
		try {
			return handle(response);
		}  catch(JsonProcessingException ex) {
			throw new AmazonAWSClientException("The amazon aws ip-ranges response body could not be parsed. "
					+ "Body is: " + response.getBody() , ex);
		}
	}

	private HttpResponse getAmazonResponse() throws OkHttpClientException{
		try {
			return httpClient.get(IP_RANGES);
		} catch(OkHttpClientException ex) {
			throw new AmazonAWSClientException("The http client failed at making the request.", ex);
		} 
	}

	private IpRanges handle(HttpResponse response) throws JsonProcessingException {
		HttpResponseCode code = HttpResponseCode.fromHttpCode(response.getStatusCode());
		if(reponseIsSuccessful(code)) {
			return parseBodyToIpRanges(response.getBody());
		} else {
			return handleFailure(code);
		}
	}
	
	private boolean reponseIsSuccessful(HttpResponseCode code) {
		return HttpResponseCode.REQUEST_SUCCESSFUL.equals(code);
	}

	private IpRanges parseBodyToIpRanges(String body) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(body, IpRanges.class);
	}
	
	private IpRanges handleFailure(HttpResponseCode code) {
		switch(code) {
		case BAD_REQUEST:
			// fall through is intended
		case FORBIDDEN:
			// fall through is intended
		case NOT_FOUND:
			throw new AmazonAWSClientException("The request to amazon aws ip ranges was unsuccessful. Please check URL used for get request.");
		default:
			throw new AmazonAWSClientException("The request to amazon aws ip ranges returned an unexpected code: " + code.getNumValue());
		}
	}

}
