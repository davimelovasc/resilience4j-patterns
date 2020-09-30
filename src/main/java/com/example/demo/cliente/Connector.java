package com.example.demo.cliente;

import org.springframework.web.client.RestTemplate;


public class Connector {

	private RestTemplate restTemplate = new RestTemplate();
	
	private String url = "http://localhost:8080/";
	
	public Boolean makeRequest() {
		restTemplate.getForObject(url, String.class);
		return true;
	}
}
