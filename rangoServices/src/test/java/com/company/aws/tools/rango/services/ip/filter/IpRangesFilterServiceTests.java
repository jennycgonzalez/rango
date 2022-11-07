package com.company.aws.tools.rango.services.ip.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.company.aws.tools.rango.services.model.Ip4Prefix;
import com.company.aws.tools.rango.services.model.Ip6Prefix;
import com.company.aws.tools.rango.services.model.IpRanges;
import com.company.aws.tools.rango.services.model.RegionPrefix;

@SpringBootTest(classes = {IpRangesFilterService.class})
public class IpRangesFilterServiceTests {
	
	public static final String INVALID_REGION = "DUMMY_REGION";
	public static final String TEST_REGION_US = "us-west-2";
	public static final String TEST_REGION_CA = "ca-south-1";	
	public static final String INVALID_TEST_REGION_IL = "il-south-1";	
	public static final String TEST_IP4PREFIX_US_A = "3.2.34.0/26";
	public static final String TEST_IP4PREFIX_US_B = "13.34.65.64/28";
	public static final String TEST_IP4PREFIX_CA_C = "3.108.0.0/14";
	public static final String TEST_IP4PREFIX_CA_D = "52.93.178.219/32";
	public static final String TEST_INVALID_IP4PREFIX_IL_C = "92.93.198.219/32";
	public static final String TEST_INVALID_IP4PREFIX_IL_D = "92.93.178.269/32";
	
	
	public static final String TEST_REGION_EU = "eu-south-1";
	public static final String TEST_REGION_AP = "ap-south-2";
	public static final String TEST_IP6PREFIX_EU_A = "2a05:d07a:a000::/40";
	public static final String TEST_IP6PREFIX_EU_B = "2a09:d07a:a002::/60";
	public static final String TEST_IP6PREFIX_AP_C = "2406:da1b::/36";
	public static final String TEST_IP6PREFIX_AP_D = "2408:da1b::/66";
	public static final String TEST_INVALID_IP6PREFIX_IL_C = "2a05:d050:2000::/40";
	public static final String TEST_INVALID_IP6PREFIX_IL_D = "2600:9000:ddd::/48";
	
	@Autowired
	private IpRangesFilterService filter;

	@Test
	void filterByRegion_returnsResult_containingIp4Prefix_fromInputPrefix() {
		IpRanges ipRanges = createIpRangesWithIp4PrefixesWithRegion(TEST_REGION_US);
		
		String result = filter.filterByRegionPrefix(ipRanges, RegionPrefix.US.toString());
		
		assertResultContains(result, TEST_IP4PREFIX_US_A);
		assertResultContains(result, TEST_IP4PREFIX_US_B);
	}
	
	@Test
	void filterByRegion_returnsResult_WithoutIp4PrefixesThatDoNotHaveInputPrefix() {
		IpRanges ipRanges = createIpRangesWithIp4PrefixesWithRegions(TEST_REGION_US, TEST_REGION_CA);
		
		String result = filter.filterByRegionPrefix(ipRanges, RegionPrefix.US.toString());
		
		assertResultContains(result, TEST_IP4PREFIX_US_A);
		assertResultContains(result, TEST_IP4PREFIX_US_B);
		assertResultDoesNotContain(result, TEST_IP4PREFIX_CA_C);
		assertResultDoesNotContain(result, TEST_IP4PREFIX_CA_D);
	}
	
	@Test
	void filterByRegion_returnsAllIp4Prefixes_withValidRegionPrefix_whenInputEqualsAll() {
		IpRanges ipRanges = createIpRangesWithIp4PrefixesWithRegions(TEST_REGION_US, TEST_REGION_CA);
		
		String result = filter.filterByRegionPrefix(ipRanges, IpRangesFilterService.ALL_REGIONS);
		
		assertResultContains(result, TEST_IP4PREFIX_US_A);
		assertResultContains(result, TEST_IP4PREFIX_US_B);
		assertResultContains(result, TEST_IP4PREFIX_CA_C);
		assertResultContains(result, TEST_IP4PREFIX_CA_D);
	}
	
	@Test
	void filterByRegion_returnsResult_withoutIp4PrefixesThatHaveInvalidPrefixRegion_whenInputEqualsAll() {
		IpRanges ipRanges = createIpRangesWithIp4PrefixesWithValidAndInvalidRegions(TEST_REGION_US, INVALID_TEST_REGION_IL);
		
		String result = filter.filterByRegionPrefix(ipRanges, IpRangesFilterService.ALL_REGIONS);
		
		assertResultContains(result, TEST_IP4PREFIX_US_A);
		assertResultContains(result, TEST_IP4PREFIX_US_B);
		assertResultDoesNotContain(result, TEST_INVALID_IP4PREFIX_IL_C);
		assertResultDoesNotContain(result, TEST_INVALID_IP4PREFIX_IL_D);
	}
	
	
	@Test
	void filterByRegion_returnsResultWithIp4PrefixesTitel() {
		IpRanges ipRanges = createIpRangesWithIp4PrefixesWithRegions(TEST_REGION_US, TEST_REGION_CA);
		
		String result = filter.filterByRegionPrefix(ipRanges, IpRangesFilterService.ALL_REGIONS);
		
		assertResultContains(result, IpRangesFilterService.IP4PREFIXES_TITEL);
	}
	
	@Test
	void filterByRegion_returnsResult_containingIp6Prefix_fromRegion() {
		IpRanges ipRanges = createIpRangesWithIp6PrefixesWithRegion(TEST_REGION_EU);
		
		String result = filter.filterByRegionPrefix(ipRanges, RegionPrefix.EU.toString());
		
		assertResultContains(result, TEST_IP6PREFIX_EU_A);
		assertResultContains(result, TEST_IP6PREFIX_EU_B);
	}
	
	@Test
	void filterByRegion_returnsAllIp6Prefixes_whenRegionEqualsAll() {
		IpRanges ipRanges = createIpRangesWithIp6PrefixesWithRegions(TEST_REGION_EU, TEST_REGION_AP);
		
		String result = filter.filterByRegionPrefix(ipRanges, IpRangesFilterService.ALL_REGIONS);
		
		assertResultContains(result, TEST_IP6PREFIX_EU_A);
		assertResultContains(result, TEST_IP6PREFIX_EU_B);
		assertResultContains(result, TEST_IP6PREFIX_AP_C);
		assertResultContains(result, TEST_IP6PREFIX_AP_D);
	}
	
	@Test
	void filterByRegion_returnsResult_WithoutIp6PrefixesThatAreNotFromRegion() {
		IpRanges ipRanges = createIpRangesWithIp6PrefixesWithRegions(TEST_REGION_EU, TEST_REGION_AP);
		
		String result = filter.filterByRegionPrefix(ipRanges, RegionPrefix.EU.toString());
		
		assertResultContains(result, TEST_IP6PREFIX_EU_A);
		assertResultContains(result, TEST_IP6PREFIX_EU_B);
		assertResultDoesNotContain(result, TEST_IP6PREFIX_AP_C);
		assertResultDoesNotContain(result, TEST_IP6PREFIX_AP_D);
	}
	
	@Test
	void filterByRegion_returnsResult_withoutIp6PrefixesThatHaveInvalidPrefixRegion_whenInputEqualsAll() {
		IpRanges ipRanges = createIpRangesWithIp6PrefixesWithValidAndInvalidRegions(TEST_REGION_US, INVALID_TEST_REGION_IL);
		
		String result = filter.filterByRegionPrefix(ipRanges, IpRangesFilterService.ALL_REGIONS);
		
		assertResultContains(result, TEST_IP6PREFIX_EU_A);
		assertResultContains(result, TEST_IP6PREFIX_EU_B);
		assertResultDoesNotContain(result, TEST_INVALID_IP4PREFIX_IL_C);
		assertResultDoesNotContain(result, TEST_INVALID_IP4PREFIX_IL_D);
	}
	
	
	private IpRanges createIpRangesWithIp6PrefixesWithRegion(String region) {
		IpRanges ipRanges = createBasisIpRanges();
		List<Ip6Prefix> prefixes = createIp6PrefixListAB(region);
		ipRanges.setIpv6_prefixes(prefixes);
		ipRanges.setPrefixes(Collections.emptyList());
		return ipRanges;
	}

	private void assertResultDoesNotContain(String result, String testIp4prefixCaD) {
		assertFalse(result.contains(testIp4prefixCaD));		
	}

	private IpRanges createIpRangesWithIp4PrefixesWithRegions(String regionA, String regionC) {
		IpRanges ipRanges = createBasisIpRanges();
		List<Ip4Prefix> prefixes = createIp4PrefixListAB(regionA);
		prefixes.addAll(createIp4PrefixListCD(regionC));
		ipRanges.setPrefixes(prefixes);
		ipRanges.setIpv6_prefixes(Collections.emptyList());
		return ipRanges;
	}
	
	private IpRanges createIpRangesWithIp4PrefixesWithValidAndInvalidRegions(String regionA, String regionC) {
		IpRanges ipRanges = createBasisIpRanges();
		List<Ip4Prefix> prefixes = createIp4PrefixListAB(regionA);
		prefixes.addAll(createIp4PrefixListInvalidRegion(regionC));
		ipRanges.setPrefixes(prefixes);
		ipRanges.setIpv6_prefixes(Collections.emptyList());
		return ipRanges;
	}
	
	private IpRanges createIpRangesWithIp6PrefixesWithValidAndInvalidRegions(String regionA, String regionC) {
		IpRanges ipRanges = createBasisIpRanges();
		List<Ip6Prefix> prefixes = createIp6PrefixListAB(regionA);
		prefixes.addAll(createIp6PrefixListInvalidRegion(regionC));
		ipRanges.setPrefixes(Collections.emptyList());
		ipRanges.setIpv6_prefixes(prefixes);
		return ipRanges;
	}
	
	private IpRanges createIpRangesWithIp6PrefixesWithRegions(String regionA, String regionC) {
		IpRanges ipRanges = createBasisIpRanges();
		List<Ip6Prefix> prefixes = createIp6PrefixListAB(regionA);
		prefixes.addAll(createIp6PrefixListCD(regionC));
		ipRanges.setIpv6_prefixes(prefixes);
		ipRanges.setPrefixes(Collections.emptyList());
		return ipRanges;
	}
	
	private List<Ip6Prefix> createIp6PrefixListCD(String regionC) {
		List<Ip6Prefix> prefixes = new ArrayList<>();
		prefixes.add(createIp6Prefix(TEST_IP6PREFIX_AP_C, regionC));
		prefixes.add(createIp6Prefix(TEST_IP6PREFIX_AP_D, regionC));
		return prefixes;
	}

	private void assertResultContains(String result, String value) {
		assertTrue(result.contains(value));
	}

	private IpRanges createIpRangesWithIp4PrefixesWithRegion(String region) {
		IpRanges ipRanges = createBasisIpRanges();
		ipRanges.setPrefixes(createIp4PrefixListAB(region));
		ipRanges.setIpv6_prefixes(Collections.emptyList());
		return ipRanges;
	}
	
	private IpRanges createBasisIpRanges() {
		IpRanges ipRanges = new IpRanges();
		ipRanges.setCreateDate("date");
		ipRanges.setSyncToken("token");
		return ipRanges;
	}
	
	private List<Ip4Prefix> createIp4PrefixListAB(String region) {
		List<Ip4Prefix> prefixes = new ArrayList<>();
		prefixes.add(createIp4Prefix(TEST_IP4PREFIX_US_A, region));
		prefixes.add(createIp4Prefix(TEST_IP4PREFIX_US_B, region));
		return prefixes;
	}
	
	private List<Ip4Prefix> createIp4PrefixListCD(String region) {
		List<Ip4Prefix> prefixes = new ArrayList<>();
		prefixes.add(createIp4Prefix(TEST_IP4PREFIX_CA_C, region));
		prefixes.add(createIp4Prefix(TEST_IP4PREFIX_CA_D, region));
		return prefixes;
	}
	
	private List<Ip4Prefix> createIp4PrefixListInvalidRegion(String region) {
		List<Ip4Prefix> prefixes = new ArrayList<>();
		prefixes.add(createIp4Prefix(TEST_INVALID_IP4PREFIX_IL_C, region));
		prefixes.add(createIp4Prefix(TEST_INVALID_IP4PREFIX_IL_D, region));
		return prefixes;
	}
	
	private List<Ip6Prefix> createIp6PrefixListInvalidRegion(String region) {
		List<Ip6Prefix> prefixes = new ArrayList<>();
		prefixes.add(createIp6Prefix(TEST_INVALID_IP6PREFIX_IL_C, region));
		prefixes.add(createIp6Prefix(TEST_INVALID_IP6PREFIX_IL_D, region));
		return prefixes;
	}
	
	private Ip4Prefix createIp4Prefix(String ip4_prefix, String region) {
		Ip4Prefix prefix = new Ip4Prefix();
		prefix.setIp_prefix(ip4_prefix);
		prefix.setRegion(region);
		return prefix;
	}
	
	private Ip6Prefix createIp6Prefix(String ip6_prefix, String region) {
		Ip6Prefix prefix = new Ip6Prefix();
		prefix.setIpv6_prefix(ip6_prefix);
		prefix.setRegion(region);
		return prefix;
	}

	private List<Ip6Prefix> createIp6PrefixListAB(String region) {
		List<Ip6Prefix> prefixes = new ArrayList<>();
		prefixes.add(createIp6Prefix(TEST_IP6PREFIX_EU_A, region));
		prefixes.add(createIp6Prefix(TEST_IP6PREFIX_EU_B, region));
		return prefixes;
	}
}
