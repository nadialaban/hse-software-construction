package tools;

import nlaban.hw4.lib.annotations.Constrained;
import nlaban.hw4.lib.annotations.Positive;

@Constrained
public class ConstrainedAnnotationClass {

	public NotConstrainedClass notConstrained = new NotConstrainedClass();
	public ConstrainedClass constrained = new ConstrainedClass();

	public static class NotConstrainedClass {

		@Positive
		public int n = -1;
	}

	@Constrained
	public static class ConstrainedClass {

		@Positive
		public int n = -1;
	}

}
