package com.company.aws.tools.rango.services.ip.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.company.aws.tools.rango.services.model.Ip4Prefix;
import com.company.aws.tools.rango.services.model.IpRanges;

@Service
public class IpRangesFilter {
	
	public String filterByRegion(IpRanges ipRanges, String region) {
		List<String> prefixes =  ipRanges.getPrefixes().stream()
				.filter(p -> p.getRegion().startsWith(region.toLowerCase()))
				.map(Ip4Prefix::getIp_prefix)
				.collect(Collectors.toList());
		return StringUtils.join(prefixes, "\n");
	}


}
