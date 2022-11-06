package com.company.aws.tools.rango.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.aws.tools.rango.services.amazon.client.AmazonAWSClientService;
import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.model.IpRanges;

@RestController
public class IpRangeController {
	
	@Autowired
	private AmazonAWSClientService amazonClient;
	
	@GetMapping(path = Routes.FIND_BY_REGION,  produces = {MediaType.TEXT_PLAIN_VALUE})
	public String findIpRangesByRegion() {
		try {
			IpRanges ipRanges = amazonClient.getIpRanges();
		} catch(AmazonAWSClientException ex) {
			return createErrorResponse();
		}
		return null;
	}

	private String createErrorResponse() {
		return "There is a problem in the communication with amazon aws.";
	}
	

}