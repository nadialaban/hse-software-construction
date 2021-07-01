package nlaban.hw4.lib.processors;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nlaban.hw4.lib.annotations.Size;

public class SizeProcessor extends AnnotationProcessor {

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
			return "error: invalid type (Size works with String, Map, List and Set)";
		}

		int min = ((Size) annotation).min(),
				max = ((Size) annotation).max();

		if (max < min) {
			return "error: conflicting params (Size)";
		}

		if (obj instanceof String &&
				(((String) obj).length() < min || ((String) obj).length() > max)) {
			return "string length should be in range [" + min + ";" + max + "]";
		}

		if (obj instanceof Map &&
				(((Map<?, ?>) obj).size() > max || ((Map<?, ?>) obj).size() < min)) {
			return "map size should be in range [" + min + ";" + max + "]";
		}

		if (obj instanceof Collection<?> &&
				(((Collection<?>) obj).size() < min || ((Collection<?>) obj).size() > max)) {
			return "collection size should be in range [" + min + ";" + max + "]";
		}

		return "ok";
	}
}
