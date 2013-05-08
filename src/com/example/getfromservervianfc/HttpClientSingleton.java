package com.example.getfromservervianfc;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientSingleton {

	private static HttpClient _instance;

	private HttpClientSingleton() { 
	}
	
	public static HttpClient getInstance() {
		if (_instance == null) {
			_instance = new DefaultHttpClient();
		}
		return _instance;
	}
}
