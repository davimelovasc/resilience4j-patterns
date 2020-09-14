package com.example.demo.resilienceModule.pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.GlobalVariables;

@Service
public class Normal implements Pattern {
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${service.url}")
	private String url = "http://httpbin.org/status/200";


	@Override
	public boolean request(GlobalVariables variables) {
		try {
			variables.requestsToServer++;

			restTemplate.getForObject(url, String.class);
			variables.successRequests++;
		}catch(Exception ex) {
			  
			return false;
		}
		return true;
	}

}
