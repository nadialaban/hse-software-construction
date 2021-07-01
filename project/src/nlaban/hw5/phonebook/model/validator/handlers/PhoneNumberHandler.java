package nlaban.hw5.phonebook.model.validator.handlers;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public class PhoneNumberHandler extends AnnotationHandler {

	/**
	 * Обработчик аннотации для проверки формата имени
	 *
	 * @param obj        - объект для проверки
	 * @param annotation - аннотация
	 * @return сообщение о проверке
	 */
	@Override
	public String handle(Object obj, Annotation annotation) {
		String pattern = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
		if (!obj.toString().isBlank() && !Pattern.matches(pattern, obj.toString())) {
			return "* Неверный формат номера телефона.";
		}

		return null;
	}
}