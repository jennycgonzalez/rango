package com.company.aws.tools.rango.rest.controller;

import static org.hamcrest.core.StringContains.containsString;
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
	public static final String TEST_IP4PREFIX_A = "3.2.34.0/26";
	
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
	void findIpRangesByRegion_returnsTextWithExpectedPrefix_whenRegionIsValid() throws Exception {
		IpRanges ipRanges = createIpRangesWithRegionAndIp4Prefix(Region.AF.toString(), TEST_IP4PREFIX_A);
		when(amazonClient.getIpRanges()).thenReturn(ipRanges);
		
		mvc.perform(get(Routes.FIND_BY_REGION)
				.param(IpRangeController.PARAM_REGION_NAME, Region.AF.toString())
				.contentType(MediaType.ALL))
		.andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		.andExpect(content().string(containsString(TEST_IP4PREFIX_A)));
	}
	
	private IpRanges createIpRangesWithRegionAndIp4Prefix(String region, String ip4Prefix) {
		IpRanges ipRanges = new IpRanges();
		ipRanges.setCreateDate("date");
		ipRanges.setSyncToken("token");
		ipRanges.setPrefixes(createIp4PrefixList(TEST_IP4PREFIX_A, region));
		return ipRanges;
	}
	
	private List<Ip4Prefix> createIp4PrefixList(String ip4_prefix, String region) {
		List<Ip4Prefix> prefixes = new ArrayList<>();
		Ip4Prefix prefix = new Ip4Prefix();
		prefix.setIp_prefix(ip4_prefix);
		prefix.setRegion(region);
		prefixes.add(prefix);
		return prefixes;
	}

}
