package com.company.aws.tools.rango.services.amazon.client;

import org.apache.commons.lang3.StringUtils;
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
		try {
			HttpResponse response = getAmazonResponse();
			return handle(response);
		} catch(OkHttpClientException ex) {
			throw new AmazonAWSClientException("The http client failed at making the get request.", ex);
		} catch(JsonProcessingException ex) {
			throw new AmazonAWSClientException("The amazon aws ip-ranges response body could not be parsed.", ex);
		}
	}

	private HttpResponse getAmazonResponse() throws OkHttpClientException{
		return httpClient.get(IP_RANGES);
	}

	private IpRanges handle(HttpResponse response) throws JsonProcessingException {
		HttpResponseCode code = HttpResponseCode.fromHttpCode(response.getStatusCode());
		if(reponseIsSuccessful(code)) {
			return parseBodyToIpRanges(response.getBody());
		} else {
			return handleFailureResponse(code);
		}
	}
	
	private boolean reponseIsSuccessful(HttpResponseCode code) {
		return HttpResponseCode.REQUEST_SUCCESSFUL.equals(code);
	}

	private IpRanges parseBodyToIpRanges(String body) throws JsonProcessingException {
		//throwIfBodyIsBlank(body);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(body, IpRanges.class);
	}
	
	private IpRanges handleFailureResponse(HttpResponseCode code) {
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

//	private void throwIfBodyIsBlank(String body) {
//		if(StringUtils.isBlank(body)) {
//			throw new AmazonAWSClientException("The response body from aws ip-ranges is null or empty");
//		}
//	}
	
}
