package com.company.aws.tools.rango.rest.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.company.aws.tools.rango.services.amazon.client.AmazonAWSClientService;
import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;
import com.company.aws.tools.rango.services.model.Ip4Prefix;
import com.company.aws.tools.rango.services.model.IpRanges;

@SpringBootTest(classes = {IpRangeController.class})
@AutoConfigureMockMvc 
class IpRangeControllerTests {
	
	public static final String INVALID_REGION = "DUMMY_REGION";
	public static final String TEST_REGION_US = "us-west-2";
	public static final String TEST_REGION_ME = "me-south-1";
	public static final String TEST_IP4PREFIX_A = "3.2.34.0/26";
	public static final String TEST_IP4PREFIX_B = "13.34.65.64/27";
	public static final String TEST_IP4PREFIX_C = "3.108.0.0/14";
	public static final String TEST_IP4PREFIX_D = "52.93.178.219/32";
	
	@MockBean
	private AmazonAWSClientService amazonClient;
	
    @Autowired
    private MockMvc mvc;

	@Test
	void findIpRangesByRegion_returnsErrorText_whenAmazonClientThrowsException() throws Exception {
		
		when(amazonClient.getIpRanges()).thenThrow(AmazonAWSClientException.class);
		
		mvc.perform(get(Routes.FIND_BY_REGION)
		   .param(IpRangeController.PARAM_REGION_NAME, Region.AF.toString())
		   .contentType(MediaType.ALL))
		   .andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		   .andExpect(content().string(containsString(IpRangeController.REQUEST_ERROR_PREFIX)));
	}
	
	@Test
	void findIpRangesByRegion_returnsErrorText_whenParameterRegionIsEmpty() throws Exception {
		
		when(amazonClient.getIpRanges()).thenReturn(new IpRanges());
		
		mvc.perform(get(Routes.FIND_BY_REGION)
		   .param(IpRangeController.PARAM_REGION_NAME, StringUtils.EMPTY)
		   .contentType(MediaType.ALL))
		   .andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		   .andExpect(content().string(containsString(IpRangeController.PARAM_BLANK_ERROR_PREFIX)));
	}
	
	@Test
	void findIpRangesByRegion_returnsErrorText_whenRegionIsInvalid() throws Exception {
		
		when(amazonClient.getIpRanges()).thenReturn(new IpRanges());
		
		mvc.perform(get(Routes.FIND_BY_REGION)
		   .param(IpRangeController.PARAM_REGION_NAME, INVALID_REGION)
		   .contentType(MediaType.ALL))
		   .andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		   .andExpect(content().string(containsString(IpRangeController.INVALID_REGION_ERROR_PREFIX)));
	}
	
	@Test
	void findIpRangesByRegion_returnsTextWithExpectedPrefixes_whenRegionIsValid() throws Exception {
		IpRanges ipRanges = createIpRangesWithRegion(TEST_REGION_US);
		when(amazonClient.getIpRanges()).thenReturn(ipRanges);
		
		mvc.perform(get(Routes.FIND_BY_REGION)
		   .param(IpRangeController.PARAM_REGION_NAME, Region.US.toString())
		   .contentType(MediaType.ALL))
		   .andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		   .andExpect(content().string(containsString(TEST_IP4PREFIX_A)))
		   .andExpect(content().string(containsString(TEST_IP4PREFIX_B)));
	}
	
	@Test
	void findIpRangesByRegion_returnsTextWithoutEntriesThatAreNotFromRegion() throws Exception {
		IpRanges ipRanges = createIpRangesWithRegions(TEST_REGION_US, TEST_REGION_ME);
		when(amazonClient.getIpRanges()).thenReturn(ipRanges);
		
		mvc.perform(get(Routes.FIND_BY_REGION)
				.param(IpRangeController.PARAM_REGION_NAME, Region.US.toString())
				.contentType(MediaType.ALL))
		.andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		.andExpect(content().string(containsString(TEST_IP4PREFIX_A)))
		.andExpect(content().string(containsString(TEST_IP4PREFIX_B)))
		.andExpect(content().string(not(containsString(TEST_IP4PREFIX_C))))
		.andExpect(content().string(not(containsString(TEST_IP4PREFIX_D))));
		
	}
	
	private IpRanges createIpRangesWithRegions(String regionA, String regionC) {
		IpRanges ipRanges = createBasisIpRanges();
		List<Ip4Prefix> prefixes = createIp4PrefixListAB(regionA);
		prefixes.addAll(createIp4PrefixListCD(regionC));
		ipRanges.setPrefixes(prefixes);
		return ipRanges;
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
		prefixes.add(createIp4Prefix(TEST_IP4PREFIX_A, region));
		prefixes.add(createIp4Prefix(TEST_IP4PREFIX_B, region));
		return prefixes;
	}
	
	private List<Ip4Prefix> createIp4PrefixListCD(String region) {
		List<Ip4Prefix> prefixes = new ArrayList<>();
		prefixes.add(createIp4Prefix(TEST_IP4PREFIX_C, region));
		prefixes.add(createIp4Prefix(TEST_IP4PREFIX_D, region));
		return prefixes;
	}
	
	private Ip4Prefix createIp4Prefix(String ip4_prefix, String region) {
		Ip4Prefix prefix = new Ip4Prefix();
		prefix.setIp_prefix(ip4_prefix);
		prefix.setRegion(region);
		return prefix;
	}

}
