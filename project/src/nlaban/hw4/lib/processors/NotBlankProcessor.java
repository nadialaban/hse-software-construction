package nlaban.hw4.lib.processors;

import java.lang.annotation.Annotation;

public class NotBlankProcessor extends AnnotationProcessor {

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
			return "error: invalid type (NotBlank works with String)";
		}

		if (((String) obj).isBlank()) {
			return "value should not be blank";
		}

		return "ok";
	}
}
