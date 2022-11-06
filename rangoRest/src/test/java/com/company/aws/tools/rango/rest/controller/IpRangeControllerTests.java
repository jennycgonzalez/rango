package com.company.aws.tools.rango.rest.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
import com.company.aws.tools.rango.services.model.IpRanges;

@SpringBootTest(classes = {IpRangeController.class})
@AutoConfigureMockMvc 
class IpRangeControllerTests {
	
	public static final String INVALID_REGION = "DUMMY";
	
	@MockBean
	private AmazonAWSClientService amazonClient;
	
    @Autowired
    private MockMvc mvc;

	@Test
	void findIpRangesByRegion_returnsTextWithError_whenAmazonClientThrowsException() throws Exception {
		
		when(amazonClient.getIpRanges()).thenThrow(AmazonAWSClientException.class);
		
		mvc.perform(get(Routes.FIND_BY_REGION)
		   .param(IpRangeController.PARAM_REGION, "Region A")
		   .contentType(MediaType.ALL))
		   .andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		   .andExpect(content().string(containsString(IpRangeController.ERROR_PREFIX)));
	}
	
	@Test
	void findIpRangesByRegion_returnsTextWithError_whenParameterRegionIsEmpty() throws Exception {
		
		when(amazonClient.getIpRanges()).thenReturn(new IpRanges());
		
		mvc.perform(get(Routes.FIND_BY_REGION)
		   .param(IpRangeController.PARAM_REGION, StringUtils.EMPTY)
		   .contentType(MediaType.ALL))
		   .andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		   .andExpect(content().string(containsString(IpRangeController.PARAM_BLANK_ERROR_PREFIX)));
	}
	
	@Test
	void findIpRangesByRegion_returnsTextWithError_whenRegionIsInvalid() throws Exception {
		
		when(amazonClient.getIpRanges()).thenReturn(new IpRanges());
		
		mvc.perform(get(Routes.FIND_BY_REGION)
		   .param(IpRangeController.PARAM_REGION, INVALID_REGION)
		   .contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		   .andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		   .andExpect(content().string(containsString(IpRangeController.INVALID_REGION_ERROR_PREFIX)));
	}
	

}
