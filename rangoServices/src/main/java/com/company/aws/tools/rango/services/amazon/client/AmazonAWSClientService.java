package com.company.aws.tools.rango.services.amazon.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.aws.tools.rango.services.http.client.OkHttpClientService;
import com.company.aws.tools.rango.services.model.IpRangesAmazon;

@Service
public class AmazonAWSClientService {

	@Autowired
	private OkHttpClientService httpClient;
	
	public IpRangesAmazon getIpRanges() {
		// do get request
		// parse the response body
		// return IpRangesAmazon Object		
		return null;
	}
	
}
