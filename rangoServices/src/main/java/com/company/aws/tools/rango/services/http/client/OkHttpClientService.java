package com.company.aws.tools.rango.services.http.client;

import org.springframework.stereotype.Service;

import com.company.aws.tools.rango.services.utils.ParamChecker;

@Service
public class OkHttpClientService implements HttpClient {
	
	

	@Override
	public HttpResponse get(String url) {
		ParamChecker.throwIfBlank(url, "The parameter url must be not null or empty");
		// TODO Auto-generated method stub
		return null;
	}

}
