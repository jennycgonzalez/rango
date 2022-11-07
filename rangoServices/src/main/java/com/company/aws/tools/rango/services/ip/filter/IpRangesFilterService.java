package com.company.aws.tools.rango.services.ip.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.company.aws.tools.rango.services.model.Ip4Prefix;
import com.company.aws.tools.rango.services.model.IpRanges;
import com.company.aws.tools.rango.services.model.Region;

@Service
public class IpRangesFilterService {
	
	public static final String IP4PREFIXES_TITEL = "---- Ip4 prefixes ---";
	
	public String filterByRegion(IpRanges ipRanges, String region) {
		return Region.ALL.toString().equals(region) ? getAllIp4Prefixes(ipRanges) 
				: filterIp4PrefixesRangesByRegion(ipRanges, region);
	}
	
	private String getAllIp4Prefixes(IpRanges ipRanges) {
		List<String> prefixes = ipRanges.getPrefixes().stream()
				.map(Ip4Prefix::getIp_prefix)
				.collect(Collectors.toList());
		return buildIp4PrefixesResult(prefixes);
	}


	private String filterIp4PrefixesRangesByRegion(IpRanges ipRanges, String region) {
		List<String> prefixes =  ipRanges.getPrefixes().stream()
				.filter(p -> p.getRegion().startsWith(region.toLowerCase()))
				.map(Ip4Prefix::getIp_prefix)
				.collect(Collectors.toList());
		return buildIp4PrefixesResult(prefixes);
	}
	
	private String buildIp4PrefixesResult(List<String> prefixes) {
		return IP4PREFIXES_TITEL + StringUtils.join(prefixes, "\n");
	}
	
}
