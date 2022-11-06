package com.company.aws.tools.rango.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.aws.tools.rango.rest.response.IpRangeResponse;

@RestController
public class IpRangeController {
	
	@GetMapping("/ip-range/findByRegion")
	public IpRangeResponse findIpRangesByRegion() {
		return null;
	}
	

}
