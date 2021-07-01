package nlaban.hw4.lib.processors;

import java.lang.annotation.Annotation;
import nlaban.hw4.lib.annotations.InRange;

public class InRangeProcessor extends AnnotationProcessor {

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
			return "error: invalid type (InRage works with decimal numbers)";
		}

		long min = ((InRange) annotation).min(),
				max = ((InRange) annotation).max(),
				val = ((Number) obj).longValue();

		if (max < min) {
			return "error: conflicting params (InRange)";
		}

		if (val < min || val > max) {
			return "value should be in range [" + min + ";" + max + "]";
		}

		return "ok";
	}
}
