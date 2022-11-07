package com.company.aws.tools.rango.services.ip.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.company.aws.tools.rango.services.model.Ip4Prefix;
import com.company.aws.tools.rango.services.model.Ip6Prefix;
import com.company.aws.tools.rango.services.model.IpRanges;
import com.company.aws.tools.rango.services.model.Region;

@Service
public class IpRangesFilterService {
	
	public static final String IP4PREFIXES_TITEL = "---- Ip4 prefixes ---";
	public static final String IP6PREFIXES_TITEL = "---- Ip6 prefixes ---";
	
	public String filterByRegion(IpRanges ipRanges, String region) {
		return Region.ALL.toString().equals(region) ? getAllIpRangesWithIp4Prefixes(ipRanges) 
				: filterIpRangesByRegion(ipRanges, region);
	}
	
	
	private String getAllIpRangesWithIp4Prefixes(IpRanges ipRanges) {
		List<String> prefixes = ipRanges.getPrefixes().stream()
				.map(Ip4Prefix::getIp_prefix)
				.collect(Collectors.toList());
		return buildIp4PrefixesResult(prefixes);
	}

	private String filterIpRangesByRegion(IpRanges ipRanges, String region) {
		StringBuilder builder = new StringBuilder();
		builder.append(filterIpRangesWithIp4PrefixByRegion(ipRanges, region));
		builder.append("/n");
		builder.append(filterIp6PrefixesRangesByRegion(ipRanges, region));
		return builder.toString();
	}
	
	private String filterIpRangesWithIp4PrefixByRegion(IpRanges ipRanges, String region) {
		List<String> prefixes =  ipRanges.getPrefixes().stream()
				.filter(p -> p.getRegion().startsWith(region.toLowerCase()))
				.map(Ip4Prefix::getIp_prefix)
				.collect(Collectors.toList());
		return buildIp4PrefixesResult(prefixes);
	}
	
	private String filterIp6PrefixesRangesByRegion(IpRanges ipRanges, String region) {
		List<String> prefixes =  ipRanges.getIpv6_prefixes().stream()
				.filter(p -> p.getRegion().startsWith(region.toLowerCase()))
				.map(Ip6Prefix::getIpv6_prefix)
				.collect(Collectors.toList());
		return buildIp6PrefixesResult(prefixes);
	}
	
	private String buildIp4PrefixesResult(List<String> prefixes) {
		return IP4PREFIXES_TITEL + StringUtils.join(prefixes, "\n");
	}
	
	private String buildIp6PrefixesResult(List<String> prefixes) {
		return IP6PREFIXES_TITEL + StringUtils.join(prefixes, "\n");
	}
	
}
