package nlaban.hw4.lib.interfaces;

public interface ValidationError {

	String getMessage();

	String getPath();

	Object getFailedValue();
}
