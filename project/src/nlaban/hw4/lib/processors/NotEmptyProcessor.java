package nlaban.hw4.lib.processors;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotEmptyProcessor extends AnnotationProcessor {

	/**
	 * Обработчик аннотации
	 *
	 * @param obj        - объект для проверки
	 * @param annotation - аннотация
	 * @return сообщение о проверке
	 */
	@Override
	public String process(Object obj, Annotation annotation) {
		if (obj == null) {
			return "ok";
		}

		if (!(obj instanceof String) && !(obj instanceof List<?>) &&
				!(obj instanceof Set<?>) && !(obj instanceof Map<?, ?>)) {
			return "error: invalid type (NotEmpty works with String, Map, List and Set)";
		}

		if (obj instanceof String && ((String) obj).isEmpty()) {
			return "string should not be empty";
		}

		if (obj instanceof Map && ((Map<?, ?>) obj).isEmpty()) {
			return "map should not be empty";
		}

		if (obj instanceof Collection && ((Collection<?>) obj).isEmpty()) {
			return "collection should not be empty";
		}

		return "ok";

	}
}
