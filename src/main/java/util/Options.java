package util;

public class Options {
	private String resiliencePattern;
	private Integer qtdReqSuccess;
	private Integer maxRequest;
	
	private CircuitBreakerParams circuitBreakerParams;
	
	private RetryParams retryParams;

	public CircuitBreakerParams getCircuitBreakerParams() {
		return circuitBreakerParams;
	}

	public void setCircuitBreakerParams(CircuitBreakerParams circuitBreakerParams) {
		this.circuitBreakerParams = circuitBreakerParams;
	}

	public String getResiliencePattern() {
		return resiliencePattern;
	}

	public void setResiliencePattern(String resiliencePattern) {
		this.resiliencePattern = resiliencePattern;
	}

	public Integer getQtdReqSuccess() {
		return qtdReqSuccess;
	}

	public void setQtdReqSuccess(Integer qtdReqSuccess) {
		this.qtdReqSuccess = qtdReqSuccess;
	}

	public Integer getMaxRequest() {
		return maxRequest;
	}

	public void setMaxRequest(Integer maxRequest) {
		this.maxRequest = maxRequest;
	}

	public RetryParams getRetryParams() {
		return retryParams;
	}

	public void setRetryParams(RetryParams retryParams) {
		this.retryParams = retryParams;
	}
	

}
