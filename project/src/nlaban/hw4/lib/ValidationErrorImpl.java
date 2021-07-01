package nlaban.hw4.lib;

import nlaban.hw4.lib.interfaces.ValidationError;

public class ValidationErrorImpl implements ValidationError {

	private final String message;
	private final String path;
	private final Object failedValue;

	public ValidationErrorImpl(String message, String path, Object failedValue) {
		this.message = message;
		this.path = path;
		this.failedValue = failedValue;
	}


	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public Object getFailedValue() {
		return failedValue;
	}

	@Override
	public String toString() {
		return String.format("%40s\t|\t%-40s\t|\t%s",
				message, path, failedValue == null ? "null" : failedValue.toString());
	}

}
