package com.company.aws.tools.rango.services.http.client;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.company.aws.tools.rango.services.exceptions.OkHttpClientException;
import com.company.aws.tools.rango.services.utils.ParamChecker;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class OkHttpClientService implements HttpClient {
	
	OkHttpClient client;

	@Override
	public HttpResponse get(String url) {
		ParamChecker.throwIfBlank(url, "The parameter url must be not null or empty");
		HttpResponse httpResponse = new HttpResponse();
		try(Response response = sendGetRequest(url)){
			httpResponse.setStatusCode(response.code());
			httpResponse.setBody(response.body().toString());
			return httpResponse;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new OkHttpClientException("The get could not be executed due to cancellation,"
					+ " a connectivity problem or timeout", ex);
		}
	}
	
	private Response sendGetRequest(String url) throws IOException {
		client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		Call call = client.newCall(request);
		return call.execute();
	}
	
	
	
	

}
