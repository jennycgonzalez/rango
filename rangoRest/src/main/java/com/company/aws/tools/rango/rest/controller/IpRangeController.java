package com.company.aws.tools.rango.rest.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.aws.tools.rango.services.amazon.client.AmazonAWSClientService;
import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.model.IpRanges;

@RestController
public class IpRangeController {
	
	public static final String ERROR_PREFIX = "There was a problem with the communication with amazon aws. Reason: ";
	public static final String MEDIA_TYPE_TEXT_PLAIN = "text/plain;charset=ISO-8859-1";
	public static final String PARAM_BLANK_ERROR_PREFIX = "The following parameter must be not empty or null: ";
	public static final String PARAM_REGION = "region";
	public static final String INVALID_REGION_ERROR_PREFIX = "The given region is invalid: ";
	
	
	@Autowired
	private AmazonAWSClientService amazonClient;
	
	@GetMapping(path = Routes.FIND_BY_REGION,  produces = {MEDIA_TYPE_TEXT_PLAIN})
	public String findIpRangesByRegion(@RequestParam(value = PARAM_REGION, required = false) String region) {
		if(StringUtils.isBlank(region)) {
			return paramIsBlankError(PARAM_REGION);
		}
		try {
			IpRanges ipRanges = amazonClient.getIpRanges();
		} catch(AmazonAWSClientException ex) {
			return ERROR_PREFIX + ex.getMessage();
		}
		return null;
	}
	
	private String paramIsBlankError(String name) {
		return PARAM_BLANK_ERROR_PREFIX + name;
	}
	
	private void isValidRegion(String region) {
		
	}
	
}