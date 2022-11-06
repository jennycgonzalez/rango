package com.company.aws.tools.rango.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.aws.tools.rango.rest.response.IpRangeResponse;
import com.company.aws.tools.rango.services.amazon.client.AmazonAWSClientService;
import com.company.aws.tools.rango.services.model.IpRanges;

@RestController
public class IpRangeController {
	
	@Autowired
	private AmazonAWSClientService amazonClient;
	
	@GetMapping(Routes.FIND_BY_REGION)
	public IpRangeResponse findIpRangesByRegion() {
		IpRanges ipRanges = amazonClient.getIpRanges();
		
		return null;
	}
	

}