package tools;

import java.util.List;
import java.util.Map;
import java.util.Set;
import nlaban.hw4.lib.annotations.AnyOf;
import nlaban.hw4.lib.annotations.Constrained;
import nlaban.hw4.lib.annotations.InRange;
import nlaban.hw4.lib.annotations.Negative;
import nlaban.hw4.lib.annotations.NotBlank;
import nlaban.hw4.lib.annotations.NotEmpty;
import nlaban.hw4.lib.annotations.NotNull;
import nlaban.hw4.lib.annotations.Positive;
import nlaban.hw4.lib.annotations.Size;

@Constrained
public class AllAnnotationsClass {

	// Делаю поля публичными, чтобы не перегружать код сеттерами
	// По умолчанию все поля заполнены валидными значениями для облегчения тестирования
	@AnyOf({"a", "b", "c"})
	public String anyOf = null;

	@NotNull
	public Boolean notNull = true;

	@NotBlank
	public String notBlank = null;

	@NotEmpty
	public String notEmptyString = null;
	@NotEmpty
	public Map<String, Integer> notEmptyMap = null;
	@NotEmpty
	public List<Integer> notEmptyList = null;
	@NotEmpty
	public Set<Integer> notEmptySet = null;

	@InRange(min = 2, max = 5)
	public Integer inRange = null;

	@Negative
	public Integer neg = null;
	@Positive
	public Integer pos = null;

	@Size(min = 2, max = 4)
	public List<Integer> sizeList = null;
	@Size(min = 2, max = 4)
	public Map<String, Integer> sizeMap = null;
	@Size(min = 2, max = 4)
	public String sizeString = null;
	@Size(min = 2, max = 4)
	public Set<Integer> sizeSet = null;

}
