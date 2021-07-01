package tools;

import java.util.ArrayList;
import java.util.List;
import nlaban.hw4.lib.annotations.Constrained;
import nlaban.hw4.lib.annotations.Negative;
import nlaban.hw4.lib.annotations.NotBlank;
import nlaban.hw4.lib.annotations.NotEmpty;
import nlaban.hw4.lib.annotations.NotNull;
import nlaban.hw4.lib.annotations.Positive;

@Constrained
public class ListAnnotationsClass {

	public List<@NotNull ConstrainedClass> listConstrained = new ArrayList<>();
	public List<@NotEmpty List<@NotBlank String>> innerListAnnotation = new ArrayList<>();
	public List<@NotNull List<@NotEmpty List<@Negative Integer>>> twiceInnerListAnnotation = new ArrayList<>();

	public ListAnnotationsClass() {
		// Заполняем список с классами, которые подвергаются проверке
		listConstrained.add(null);
		listConstrained.add(new ConstrainedClass(1));
		listConstrained.add(new ConstrainedClass(-1));

		// Заполняем вложенный лист
		innerListAnnotation.add(new ArrayList<>());
		innerListAnnotation.add(new ArrayList<>(List.of("a", "  ")));

		// Заполняем дважды вложенный лист
		twiceInnerListAnnotation.add(null);
		twiceInnerListAnnotation.add(new ArrayList<>());
		twiceInnerListAnnotation.get(1).add(new ArrayList<>());
		twiceInnerListAnnotation.add(new ArrayList<>());
		twiceInnerListAnnotation.get(2).add(null);
		twiceInnerListAnnotation.get(2).add(new ArrayList<>(List.of(-1, -2, 0)));
	}

	@Constrained
	public static class ConstrainedClass {

		@Positive
		public int n;

		public ConstrainedClass(@Positive int n) {
			this.n = n;
		}
	}
}
