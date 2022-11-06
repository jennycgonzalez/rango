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

@Service
public class AmazonAWSClientService {

	private final String IP_RANGES = "https://ip-ranges.amazonaws.com/ip-ranges.json";
	
	@Autowired
	private OkHttpClientService httpClient;
	
	public IpRanges getIpRanges() throws AmazonAWSClientException {
		HttpResponse response = getAmazonResponse();
		return handleReponse(response);
	}

	private HttpResponse getAmazonResponse() {
		try {
			return httpClient.get(IP_RANGES);
		} catch(OkHttpClientException ex) {
			throw new AmazonAWSClientException("The http client failed at making the get request.", ex);
		}
	}

	private IpRanges handleReponse(HttpResponse response) {
		HttpResponseCode code = HttpResponseCode.fromHttpCode(response.getStatusCode());
		switch(code) {
		case BAD_REQUEST:
			// fall through is intended
		case FORBIDDEN:
			// fall through is intended
		case NOT_FOUND:
			throw new AmazonAWSClientException("The request to amazon aws ip ranges was unsuccessful. Please check URL used for get request.");
		case REQUEST_SUCCESSFUL:
			return parseResponseToIpRanges(response);
		default:
			throw new AmazonAWSClientException("The request to amazon aws ip ranges returned an unexpected code: " + response.getStatusCode());
		}
	}

	private IpRanges parseResponseToIpRanges(HttpResponse response) {
		throwIfBodyIsBlank(response);
		return null;
	}
	
	private void throwIfBodyIsBlank(HttpResponse response) {
		if(StringUtils.isBlank(response.getBody())) {
			throw new AmazonAWSClientException("The response body from aws ip-ranges is null or empty");
		}
	}
	
}
