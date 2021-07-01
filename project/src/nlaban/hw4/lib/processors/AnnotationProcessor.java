package nlaban.hw4.lib.processors;

import java.lang.annotation.Annotation;

public abstract class AnnotationProcessor {

	/**
	 * Обработчик аннотации
	 *
	 * @param obj        - объект для проверки
	 * @param annotation - аннотация
	 * @return сообщение о проверке
	 */
	public abstract String process(Object obj, Annotation annotation);
}
