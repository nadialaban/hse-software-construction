package nlaban.hw4.lib.processors;

import java.lang.annotation.Annotation;

public class NotNullProcessor extends AnnotationProcessor {

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
			return "must not be null";
		}
		return "ok";
	}
}
