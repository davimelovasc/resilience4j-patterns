package com.example.demo.resilienceModule.pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.GlobalVariables;

import io.github.resilience4j.retry.annotation.Retry;

@Service
public class RetryPattern implements Pattern {
	

	private String url = "http://httpbin.org/status/500";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	@Retry(name = "backendA", fallbackMethod = "fallback")
	public boolean request(GlobalVariables variables) {
		variables.requestsToServer++;
		restTemplate.getForObject(url, String.class);
		System.out.println("passou");
		variables.successRequests++;
		return true;
	}
	
	@SuppressWarnings("unused")
	private boolean fallback(GlobalVariables variables, Throwable t) {
		System.out.println("chamou fallback");
		return false;
	}

}
