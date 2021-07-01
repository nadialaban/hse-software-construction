package nlaban.hw5.phonebook.model.validator.handlers;

import java.lang.annotation.Annotation;

public abstract class AnnotationHandler {

	/**
	 * Обработчик аннотации
	 *
	 * @param obj        - объект для проверки
	 * @param annotation - аннотация
	 * @return сообщение о проверке
	 */
	public abstract String handle(Object obj, Annotation annotation);

}
