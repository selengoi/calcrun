package ru.corp.az.azrun.azcalc.task.utils;

public class OperationTimeoutException extends RuntimeException {

	private static final long serialVersionUID = 5802371592236912344L;

	public OperationTimeoutException() {
		super();
	}
	
	public OperationTimeoutException(String message) {
		super(message);		
	}
}
