package com.company.aws.tools.rango.services.amazon.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.http.client.HttpResponse;
import com.company.aws.tools.rango.services.http.client.OkHttpClientService;
import com.company.aws.tools.rango.services.model.IpRanges;

@Service
public class AmazonAWSClientService {

	private final String IP_RANGES = "https://ip-ranges.amazonaws.com/ip-ranges.json";
	
	@Autowired
	private OkHttpClientService httpClient;
	
	public IpRanges getIpRanges() {
		HttpResponse response = httpClient.get(IP_RANGES);
		return parseResponseToIpRanges(response);
	}

	private IpRanges parseResponseToIpRanges(HttpResponse response) {
		if(StringUtils.isBlank(response.getBody())) {
			throw new AmazonAWSClientException("The response body from aws ip-ranges is null or empty");
		}
		return null;
	}
	
}