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
	public HttpResponse get(String url) throws OkHttpClientException{
		ParamChecker.throwIfBlank(url, "The parameter url must be not null or empty");
		try(Response response = sendGetRequest(url)){
			return createHttpResponse(response);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new OkHttpClientException("The get request could not be executed due to cancellation,"
					+ " a connectivity problem or timeout", ex);
		}
	}
	
	private Response sendGetRequest(String url) throws IOException {
		client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		Call call = client.newCall(request);
		return call.execute();
	}
	
	private HttpResponse createHttpResponse(Response response) throws IOException {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setStatusCode(response.code());
		httpResponse.setBody(response.body().string());
		return httpResponse;
	}
	
	

}
