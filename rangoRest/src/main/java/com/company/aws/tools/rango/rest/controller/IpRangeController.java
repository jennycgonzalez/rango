package com.company.aws.tools.rango.rest.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.aws.tools.rango.services.amazon.client.AmazonAWSClientService;
import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.ip.filter.IpRangesFilterService;
import com.company.aws.tools.rango.services.model.IpRanges;
import com.company.aws.tools.rango.services.model.RegionPrefix;

@RestController
public class IpRangeController {
	
	public static final String MEDIA_TYPE_TEXT_PLAIN = "text/plain;charset=ISO-8859-1";
	public static final String PARAM_REGION_NAME = "region";
	public static final String REQUEST_ERROR_PREFIX = "There was a problem with the request to amazon aws. Reason: ";
	public static final String REGION_BLANK_ERROR = "The parameter region must be neither empty nor null.";
	public static final String INVALID_REGION_ERROR_PREFIX = "The given region is invalid: ";
	
	@Autowired
	private AmazonAWSClientService amazonClient;
	
	@Autowired
	private IpRangesFilterService filter;
	
	@GetMapping(path = Routes.FIND_BY_REGION,  produces = {MEDIA_TYPE_TEXT_PLAIN})
	public String findIpRangesByRegion(@RequestParam(value = PARAM_REGION_NAME, required = false) String inputRegion) {
		try {
			return getIpRangesByRegion(inputRegion);
		} catch(AmazonAWSClientException ex) {
			return requestError(ex.getMessage());
		}
	}
	
	private String getIpRangesByRegion(String region) {
		if(StringUtils.isBlank(region)) {
			return REGION_BLANK_ERROR;
		}
		if(!RegionPrefix.isValid(region) && !IpRangesFilterService.ALL_REGIONS.equals(region)) {
			return invalidRegionError(region);
		}
		IpRanges ipRanges = amazonClient.getIpRanges();
		return filter.filterByRegionPrefix(ipRanges, region);
	}
	
	private String requestError(String message) {
		return REQUEST_ERROR_PREFIX + message;
	}
	
	private String invalidRegionError(String region) {
		return INVALID_REGION_ERROR_PREFIX + region;
	}

}