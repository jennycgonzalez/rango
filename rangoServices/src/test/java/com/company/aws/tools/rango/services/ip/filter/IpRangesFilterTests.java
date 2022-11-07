package com.company.aws.tools.rango.services.ip.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.company.aws.tools.rango.services.model.Ip4Prefix;
import com.company.aws.tools.rango.services.model.IpRanges;
import com.company.aws.tools.rango.services.model.Region;

@SpringBootTest(classes = {IpRangesFilter.class})
public class IpRangesFilterTests {
	
	public static final String INVALID_REGION = "DUMMY_REGION";
	public static final String TEST_REGION_US = "us-west-2";
	public static final String TEST_REGION_CA = "ca-south-1";
	public static final String TEST_IP4PREFIX_US_A = "3.2.34.0/26";
	public static final String TEST_IP4PREFIX_US_B = "13.34.65.64/27";
	public static final String TEST_IP4PREFIX_CA_C = "3.108.0.0/14";
	public static final String TEST_IP4PREFIX_CA_D = "52.93.178.219/32";
	
	@Autowired
	private IpRangesFilter filter;

	@Test
	void filterByRegion_returnsResult_containingIp4Prefix_fromRegion() {
		IpRanges ipRanges = createIpRangesWithRegion(TEST_REGION_US);
		
		String result = filter.filterByRegion(ipRanges, Region.US.toString());
		
		assertResultContainsPrefix(result, TEST_IP4PREFIX_US_A);
		assertResultContainsPrefix(result, TEST_IP4PREFIX_US_B);
	}
	
	@Test
	void filterByRegion_returnsResult_WithoutIp4PrefixesThatAreNotFromRegion() {
		IpRanges ipRanges = createIpRangesWithRegions(TEST_REGION_US, TEST_REGION_CA);
		
		String result = filter.filterByRegion(ipRanges, Region.US.toString());
		
		assertResultContainsPrefix(result, TEST_IP4PREFIX_US_A);
		assertResultContainsPrefix(result, TEST_IP4PREFIX_US_B);
		assertResultDoesNotContainPrefix(result, TEST_IP4PREFIX_CA_C);
		assertResultDoesNotContainPrefix(result, TEST_IP4PREFIX_CA_D);
	}
	
	@Test
	void filterByRegion_returnsAllIp4Prefixes_whenRegionEqualsAll() {
		IpRanges ipRanges = createIpRangesWithRegions(TEST_REGION_US, TEST_REGION_CA);
		
		String result = filter.filterByRegion(ipRanges, Region.ALL.toString());
		
		assertResultContainsPrefix(result, TEST_IP4PREFIX_US_A);
		assertResultContainsPrefix(result, TEST_IP4PREFIX_US_B);
		assertResultContainsPrefix(result, TEST_IP4PREFIX_CA_C);
		assertResultContainsPrefix(result, TEST_IP4PREFIX_CA_D);
	}
	
	private void assertResultDoesNotContainPrefix(String result, String testIp4prefixCaD) {
		assertFalse(result.contains(testIp4prefixCaD));		
	}

	private IpRanges createIpRangesWithRegions(String regionA, String regionC) {
		IpRanges ipRanges = createBasisIpRanges();
		List<Ip4Prefix> prefixes = createIp4PrefixListAB(regionA);
		prefixes.addAll(createIp4PrefixListCD(regionC));
		ipRanges.setPrefixes(prefixes);
		return ipRanges;
	}
	
	private void assertResultContainsPrefix(String result, String testIp4prefixUsA) {
		assertTrue(result.contains(testIp4prefixUsA));
	}

	private IpRanges createIpRangesWithRegion(String region) {
		IpRanges ipRanges = createBasisIpRanges();
		ipRanges.setPrefixes(createIp4PrefixListAB(region));
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
	
	private Ip4Prefix createIp4Prefix(String ip4_prefix, String region) {
		Ip4Prefix prefix = new Ip4Prefix();
		prefix.setIp_prefix(ip4_prefix);
		prefix.setRegion(region);
		return prefix;
	}

}
