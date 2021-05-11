package nlaban.hw6.phonebook.model.validator.handlers;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public class NameHandler extends AnnotationHandler {

	/**
	 * Обработчик аннотации для проверки формата имени
	 *
	 * @param obj        - объект для проверки
	 * @param annotation - аннотация
	 * @return сообщение о проверке
	 */
	@Override
	public String handle(Object obj, Annotation annotation) {
		String pattern = "([A-ZА-ЯЁ][а-яa-zё]+[ -]?)+";
		if (!obj.toString().isBlank() && !Pattern.matches(pattern, obj.toString())) {
			return "* Неверный формат ФИО.";
		}

		return null;
	}
}
