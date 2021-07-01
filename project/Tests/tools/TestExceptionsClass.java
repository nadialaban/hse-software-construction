package tools;

import jdk.jfr.Description;
import nlaban.hw4.lib.annotations.AnyOf;
import nlaban.hw4.lib.annotations.Constrained;
import nlaban.hw4.lib.annotations.InRange;
import nlaban.hw4.lib.annotations.Negative;
import nlaban.hw4.lib.annotations.NotBlank;
import nlaban.hw4.lib.annotations.NotEmpty;
import nlaban.hw4.lib.annotations.Positive;
import nlaban.hw4.lib.annotations.Size;

@Constrained
public class TestExceptionsClass {

	@AnyOf("a")
	public Integer anyOfWrongType = 1;

	@InRange(min = 1, max = 2)
	public String inRangeWrongType = "a";
	@InRange(min = 5, max = 2)
	public Integer inRangeError = 3;

	@Positive
	@Negative
	public Integer posNegError = 0;
	@Positive
	public String posWrongType = "1";
	@Negative
	public String negWrongType = "-1";

	@NotBlank
	public Integer notBlankWrongType = 0;

	@NotEmpty
	public Integer notEmptyWrongType = 1;

	@Size(min = 1, max = 2)
	public Integer sizeWrongType = 1;
	@Size(min = 2, max = 1)
	public String sizeError = "1";

	@Description("Для проверки")
	public Integer notLibraryAnnotation = 0;
}
