package com.company.aws.tools.rango.services.model;

import java.util.List;

public class IpRangesAmazon {

	private String syncToken;
	
	private String createDate;
	
	private List<Ip4Prefix> prefixes;
	
	private List<Ip6Prefix> ipv6_prefixes;

	public String getSyncToken() {
		return syncToken;
	}

	public void setSyncToken(String syncToken) {
		this.syncToken = syncToken;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public List<Ip4Prefix> getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(List<Ip4Prefix> prefixes) {
		this.prefixes = prefixes;
	}

	public List<Ip6Prefix> getIpv6_prefixes() {
		return ipv6_prefixes;
	}

	public void setIpv6_prefixes(List<Ip6Prefix> ipv6_prefixes) {
		this.ipv6_prefixes = ipv6_prefixes;
	}
		
}
