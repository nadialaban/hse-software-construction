package nlaban.hw5.phonebook.model.validator.handlers;

import java.lang.annotation.Annotation;
import nlaban.hw5.phonebook.model.validator.annotatios.Required;

public class RequiredHandler extends AnnotationHandler {

	/**
	 * Обработчик аннотации
	 *
	 * @param obj        - объект для проверки
	 * @param annotation - аннотация
	 * @return сообщение о проверке
	 */
	@Override
	public String handle(Object obj, Annotation annotation) {
		if (obj == null || obj.toString().isBlank()) {
			if (((Required) annotation).isPhone()) {
				return "* Необходимо ввести хотя бы один номер телефона";
			}
			return "* Обязательное поле";
		}

		return null;
	}
}
