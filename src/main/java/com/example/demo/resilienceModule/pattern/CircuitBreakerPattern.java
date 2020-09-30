package com.example.demo.resilienceModule.pattern;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.GlobalVariables;
import com.example.demo.cliente.Connector;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.control.Try;
import util.CircuitBreakerParams;
import util.Options;


public class CircuitBreakerPattern implements Pattern {
	
	private Connector connector = new Connector();
	
	private CircuitBreaker circuitBreaker;
	
	private Supplier<Boolean> decoratedSupplier;
	
	public CircuitBreakerPattern(CircuitBreakerParams params, GlobalVariables variables) {
		this.createAndConfigCircuitBreaker(params, variables);
	}

	public boolean request(GlobalVariables variables, Options options) {
		variables.requestsToServer++;
		return Try.ofSupplier(decoratedSupplier).recover(throwable -> false).get();
	}
	
	public void createAndConfigCircuitBreaker(CircuitBreakerParams params, GlobalVariables variables) {
		CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
			    .failureRateThreshold(params.getFailureThreshold())
			    .slidingWindowSize(params.getSlidingWindowSize())
			    .minimumNumberOfCalls(params.getMinimumNumberOfCalls())
			    .waitDurationInOpenState(Duration.ofMillis(params.getDurationOfBreaking()))
			    .permittedNumberOfCallsInHalfOpenState(params.getPermittedNumberOfCallsInHalfOpenState())
			    .slowCallRateThreshold(params.getSlowCallRateThreshold())
			    .slowCallDurationThreshold(Duration.ofMillis(params.getSlowCallDurationThreshold()))
			    .build();
		
		
		CircuitBreakerRegistry circuitBreakerRegistry =
				  CircuitBreakerRegistry.of(circuitBreakerConfig);
		
		this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("cb1");
		//this.circuitBreaker = CircuitBreaker.ofDefaults("testName");
		
		circuitBreaker.getEventPublisher()
		.onSuccess(event -> {
			variables.successRequests++;
		})
		.onError(event -> {
			variables.failRequests++;
		})
		.onStateTransition(event -> {
			System.out.println("mudou estado cb");
		});
		
		decoratedSupplier = CircuitBreaker
	            .decorateSupplier(circuitBreaker, connector::makeRequest);
		
	}

	
}
