package com.example.demo.resilienceModule.pattern;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import com.example.demo.GlobalVariables;
import com.example.demo.cliente.Connector;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import util.Options;
import util.RetryParams;

public class RetryPattern implements Pattern {

	private Connector connector = new Connector();

	private Retry retry;

	private CheckedFunction0<Boolean> retryableSupplier;

	public RetryPattern(RetryParams params, GlobalVariables variables) {
		this.createAndConfigRetry(params, variables);
	}

	@Override
	public boolean request(GlobalVariables variables, Options options) {
		if(Try.of(retryableSupplier).recover(throwable -> false).get()) {
			variables.successRequests++;
			return true;
		} 
		System.out.println("nunca chegou aqui");
		return false;
	}

	public void createAndConfigRetry(RetryParams params, GlobalVariables variables) {
		RetryConfig config;

		if(params.getSleepDurationType().equalsIgnoreCase("EXPONENTIAL_BACKOFF")) {
			config = RetryConfig.custom()
					.maxAttempts(params.getCount())
					.waitDuration(Duration.ofMillis(params.getSleepDuration()))
					.intervalFunction(IntervalFunction.ofExponentialBackoff(params.getSleepDuration()))
					.build();
		} else {
			config = RetryConfig.custom()
					.maxAttempts(params.getCount())
					.waitDuration(Duration.ofMillis(params.getSleepDuration()))
					.intervalFunction(IntervalFunction.ofDefaults())
					.build();
		}

		RetryRegistry registry = RetryRegistry.of(config);

		this.retry = registry.retry("retry1");

		retry.getEventPublisher()
		.onSuccess(event -> {
			variables.requestsToServer++;
			variables.successRequests++;
			System.out.println("sucessRequest (res): " + variables.successRequests);
		})
		.onError(event -> {
			System.out.println("falhou o maximo de vezes");
			variables.requestsToServer++;
			variables.failRequests++;
		}).onRetry(event -> {
			System.out.println("falou, vai retentar");
			variables.requestsToServer++;
			variables.failRequests++;
		});

		retryableSupplier = Retry.decorateCheckedSupplier(this.retry, connector::makeRequest);

	}

}
