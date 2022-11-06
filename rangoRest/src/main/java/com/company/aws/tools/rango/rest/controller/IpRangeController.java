package com.company.aws.tools.rango.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.aws.tools.rango.services.amazon.client.AmazonAWSClientService;
import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.model.IpRanges;

@RestController
public class IpRangeController {
	
	public static final String ERROR_PREFIX = "There was a problem with the communication with amazon aws. Reason: ";
	public static final String MEDIA_TYPE_TEXT_PLAIN = "text/plain;charset=ISO-8859-1";
	
	
	@Autowired
	private AmazonAWSClientService amazonClient;
	
	@GetMapping(path = Routes.FIND_BY_REGION,  produces = {MEDIA_TYPE_TEXT_PLAIN})
	public String findIpRangesByRegion() {
		try {
			IpRanges ipRanges = amazonClient.getIpRanges();
		} catch(AmazonAWSClientException ex) {
			return ERROR_PREFIX + ex.getMessage();
		}
		return null;
	}


}