package ru.corp.az.azrun.common.taskx;

public class OperationNotAllowed extends Exception {

	private static final long serialVersionUID = 8647045371209294571L;

	public OperationNotAllowed() {
		super();
	}
	
	public OperationNotAllowed(String message) {
		super(message);
	}
}
