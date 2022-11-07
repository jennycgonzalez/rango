package com.company.aws.tools.rango.services.ip.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.company.aws.tools.rango.services.model.Ip4Prefix;
import com.company.aws.tools.rango.services.model.Ip6Prefix;
import com.company.aws.tools.rango.services.model.IpRanges;
import com.company.aws.tools.rango.services.model.RegionPrefix;

@Service
public class IpRangesFilterService {
	
	public static final String IP4PREFIXES_TITEL = "---- Ip4 prefixes ---";
	public static final String IP6PREFIXES_TITEL = "---- Ip6 prefixes ---";
	
	public String filterByRegion(IpRanges ipRanges, String region) {
		return RegionPrefix.ALL.toString().equals(region) ? getAllIpRangesWithIp4Prefixes(ipRanges) 
				: filterIpRangesByRegion(ipRanges, region);
	}
	
	
//	private String getAllIpRangesFromValidRegions() {
//		
//	}
	
	private String getAllIpRangesWithIp4Prefixes(IpRanges ipRanges) {
		List<String> prefixes = ipRanges.getPrefixes().stream()
				.map(Ip4Prefix::getIp_prefix)
				.collect(Collectors.toList());
		return IP4PREFIXES_TITEL + StringUtils.join(prefixes, "\n");
	}

	private String filterIpRangesByRegion(IpRanges ipRanges, String region) {
		StringBuilder builder = new StringBuilder();
		builder.append(IP4PREFIXES_TITEL);
		builder.append("\n");
		builder.append(filterIpRangesWithIp4PrefixByRegion(ipRanges, region));
		builder.append("\n");
		builder.append(IP6PREFIXES_TITEL);
		builder.append("\n");
		builder.append(filterIp6PrefixesRangesByRegion(ipRanges, region));
		return builder.toString();
	}
	
	private String filterIpRangesWithIp4PrefixByRegion(IpRanges ipRanges, String regionPrefix) {
		List<String> prefixes =  ipRanges.getPrefixes().stream()
				.filter(p -> p.getRegion().startsWith(regionPrefix.toLowerCase()))
				.map(Ip4Prefix::getIp_prefix)
				.collect(Collectors.toList());
		return StringUtils.join(prefixes, "\n");
	}
	
	private String filterIp6PrefixesRangesByRegion(IpRanges ipRanges, String region) {
		List<String> prefixes =  ipRanges.getIpv6_prefixes().stream()
				.filter(p -> p.getRegion().startsWith(region.toLowerCase()))
				.map(Ip6Prefix::getIpv6_prefix)
				.collect(Collectors.toList());
		return StringUtils.join(prefixes, "\n");
	}
	
}
