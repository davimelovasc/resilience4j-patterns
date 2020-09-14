package com.example.demo.resilienceModule.pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.GlobalVariables;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CircuitBreakerPattern implements Pattern {
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	private String url = "http://httpbin.org/status/500";

	@CircuitBreaker(name = "circuit_breaker_A", fallbackMethod = "fallbackMethod")
	public boolean request(GlobalVariables variables) {
		variables.requestsToServer++;
		restTemplate.getForObject(url, String.class);
		variables.successRequests++;
		return true;
	}
	
	@SuppressWarnings("unused")
	private boolean fallbackMethod(GlobalVariables variables, Throwable t) {
		variables.failRequests++;
		System.out.println("fallback call");
		return false;
	}

	
}
