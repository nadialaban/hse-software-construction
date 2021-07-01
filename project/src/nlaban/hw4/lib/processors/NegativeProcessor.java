package nlaban.hw4.lib.processors;

import java.lang.annotation.Annotation;

public class NegativeProcessor extends AnnotationProcessor {

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

		if (!(obj instanceof Byte) && !(obj instanceof Short) &&
				!(obj instanceof Integer) && !(obj instanceof Long)) {
			return "error: invalid type (Negative works with decimal numbers)";
		}

		if (((Number) obj).longValue() >= 0) {
			return "value should be negative";
		}

		return "ok";
	}
}
