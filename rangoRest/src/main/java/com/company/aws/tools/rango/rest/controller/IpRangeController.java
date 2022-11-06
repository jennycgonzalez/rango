package com.company.aws.tools.rango.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.aws.tools.rango.services.amazon.client.AmazonAWSClientService;
import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.model.Ip4Prefix;
import com.company.aws.tools.rango.services.model.IpRanges;

@RestController
public class IpRangeController {
	
	public static final String MEDIA_TYPE_TEXT_PLAIN = "text/plain;charset=ISO-8859-1";
	public static final String PARAM_REGION_NAME = "region";
	public static final String REQUEST_ERROR_PREFIX = "There was a problem with the request to amazon aws. Reason: ";
	public static final String PARAM_BLANK_ERROR_PREFIX = "The following parameter must be not empty or null: ";
	public static final String INVALID_REGION_ERROR_PREFIX = "The given region is invalid: ";
	
	@Autowired
	private AmazonAWSClientService amazonClient;
	
	@GetMapping(path = Routes.FIND_BY_REGION,  produces = {MEDIA_TYPE_TEXT_PLAIN})
	public String findIpRangesByRegion(@RequestParam(value = PARAM_REGION_NAME, required = false) String region) {
		try {
			return getIpRangesByRegion(region);
		} catch(AmazonAWSClientException ex) {
			return requestError(ex.getMessage());
		}
	}
	
	private String getIpRangesByRegion(String region) {
		if(StringUtils.isBlank(region)) {
			return paramIsBlankError(PARAM_REGION_NAME);
		}
		if(!Region.isValid(region)) {
			return invalidRegionError(region);
		}
		IpRanges ipRanges = amazonClient.getIpRanges();
		String result = ipRangesFilteredBy(ipRanges, region);
		return result;
	}

	private String ipRangesFilteredBy(IpRanges ipRanges, String region) {
		List<String> prefixes =  ipRanges.getPrefixes().stream()
				.filter(p -> p.getRegion().startsWith(region.toLowerCase()))
				.map(Ip4Prefix::getIp_prefix)
				.collect(Collectors.toList());
		return StringUtils.join(prefixes, "\n");
	}

	private String requestError(String message) {
		return REQUEST_ERROR_PREFIX + message;
	}
	
	private String invalidRegionError(String region) {
		return INVALID_REGION_ERROR_PREFIX + region;
	}

	private String paramIsBlankError(String name) {
		return PARAM_BLANK_ERROR_PREFIX + name;
	}
	
}