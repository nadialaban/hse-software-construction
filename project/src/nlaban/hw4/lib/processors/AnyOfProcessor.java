package nlaban.hw4.lib.processors;

import java.lang.annotation.Annotation;
import java.util.List;
import nlaban.hw4.lib.annotations.AnyOf;

public class AnyOfProcessor extends AnnotationProcessor {

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

		if (!(obj instanceof String)) {
			return "error: invalid type (AnyOf works with String)";
		}

		var validValues = List.of(((AnyOf) annotation).value());
		if (!validValues.contains(obj.toString())) {
			return "value '" + obj + "' is not valid";
		}
		return "ok";
	}
}
