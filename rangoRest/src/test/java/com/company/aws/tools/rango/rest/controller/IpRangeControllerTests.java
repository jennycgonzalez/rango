package com.company.aws.tools.rango.rest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.company.aws.tools.rango.services.amazon.client.AmazonAWSClientService;
import com.company.aws.tools.rango.services.exceptions.AmazonAWSClientException;

@SpringBootTest(classes = {IpRangeController.class})
@AutoConfigureMockMvc 
class IpRangeControllerTests {
	
	@MockBean
	private AmazonAWSClientService amazonClient;
	
    @Autowired
    private MockMvc mvc;

	@Test
	void findIpRangesByRegion_returnsErrorResponse_whenAmazonClientThrowsException() throws Exception {
		
		when(amazonClient.getIpRanges()).thenThrow(AmazonAWSClientException.class);
		
		mvc.perform(get(Routes.FIND_BY_REGION)
		   .contentType(MediaType.ALL))
		   //.andExpect(status().isInternalServerError())
		   .andExpect(content().contentType(IpRangeController.MEDIA_TYPE_TEXT_PLAIN))
		   .andExpect(content().string(IpRangeController.GENERAL_ERROR));
	}

}
