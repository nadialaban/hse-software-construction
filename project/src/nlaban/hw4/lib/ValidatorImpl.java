package nlaban.hw4.lib;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nlaban.hw4.lib.annotations.Constrained;
import nlaban.hw4.lib.annotations.Negative;
import nlaban.hw4.lib.annotations.Positive;
import nlaban.hw4.lib.interfaces.ValidationError;
import nlaban.hw4.lib.interfaces.Validator;
import nlaban.hw4.lib.processors.AnnotationProcessor;

public class ValidatorImpl implements Validator {

	List<String> currentPath = new ArrayList<>();
	List<String> exceptions = new ArrayList<>();

	public List<String> getExceptions() {
		return exceptions;
	}

	/**
	 * Метод проверки объекта
	 *
	 * @param object - объект
	 * @return сет ошибок
	 */
	@Override
	public Set<ValidationError> validate(Object object) {
		// Поскольку отсутствие Constrained - ошибка, а кидать исключение нельзя,
		// возвращаеи null, если аннотации нет
		HashSet<ValidationError> errors = null;

		if (object.getClass().isAnnotationPresent(Constrained.class)) {
			errors = new HashSet<>();
			var fields = object.getClass().getDeclaredFields();

			for (var field : fields) {
				field.setAccessible(true);
				Object val;
				try {
					val = field.get(object);
				} catch (IllegalAccessException e) {
					// не должно сюда заходить по идее...
					continue;
				}
				currentPath.add(field.getName());
				validateField(val, field, errors);
				currentPath.remove(currentPath.get(currentPath.size() - 1));
			}
		}

		return errors;
	}

	/**
	 * Валидация поля
	 *
	 * @param val   - значение поля
	 * @param field - поле
	 */
	private void validateField(Object val, java.lang.reflect.Field field,
			HashSet<ValidationError> errors) {

		var annotations = new ArrayList<>(List.of(field.getAnnotatedType().getAnnotations()));
		if (field.getAnnotatedType().isAnnotationPresent(Positive.class) &&
				field.getAnnotatedType().isAnnotationPresent(Negative.class)) {
			/* по-хорошему тут надо какой-нибудь такой эксепшн, но нельзя менять сигнатуру основного метода
			 * поэтому я просто буду игнорировать конфликтующие аннотации и добавлять его в список */
			exceptions
					.add("error: conflicting annotations (value couldn't be both Negative and Positive)");
			annotations.removeIf(a -> a instanceof Positive || a instanceof Negative);
		}

		validateAnnotations(val, annotations, errors);

		// Если поле само подвергается проверке
		if (field.getType().isAnnotationPresent(Constrained.class)) {
			errors.addAll(this.validate(val));
		}

		// Если поле - список
		if (val instanceof List) {
			var type = (AnnotatedParameterizedType) field.getAnnotatedType();
			validateList(type, val, errors);

		}
	}

	/**
	 * Валидация списка
	 *
	 * @param type   - тип
	 * @param val    - значение (сам список)
	 * @param errors - ошибки
	 */
	private void validateList(AnnotatedParameterizedType type, Object val,
			HashSet<ValidationError> errors) {
		var lst = (List<?>) val;
		var actualType = type.getAnnotatedActualTypeArguments()[0];
		var innerAnnotations = new ArrayList<>(List.of(actualType.getAnnotations()));

		var field = currentPath.get(currentPath.size() - 1);
		currentPath.remove(field);

		for (var i = 0; i < lst.size(); i++) {
			currentPath.add(field + "[" + i + "]");

			// Проверяем аннотации
			validateAnnotations(lst.get(i), innerAnnotations, errors);

			// Если поле само подвергается проверке
			if (lst.get(i) != null &&
					lst.get(i).getClass().isAnnotationPresent(Constrained.class)) {
				errors.addAll(this.validate(lst.get(i)));
			}

			// Если поле само список
			if (lst.get(i) instanceof List<?>) {
				validateList((AnnotatedParameterizedType) actualType, lst.get(i), errors);
			}

			currentPath.remove(field + "[" + i + "]");
		}
		currentPath.add(field);
	}

	/**
	 * Проверка аннотаций поля
	 *
	 * @param val         - значение поля
	 * @param annotations - аннотации
	 * @param errors      - ошибки
	 */
	private void validateAnnotations(Object val, ArrayList<Annotation> annotations,
			HashSet<ValidationError> errors) {
		var path = String.join(".", currentPath);
		String message;

		// Проходим аннотации
		for (var annotation : annotations) {
			message = getProcessMessage(annotation, val);
			// Записываем интересующие нас ошибки
			if (message.startsWith("error:")) {
				// Ошибка программиста, тут стоило бы выбрасиывать исключение с текстом сообщения
				exceptions.add(message);
				continue;
			} else if (message.equals("unknown annotation")) {
				// Аннотация не относится к библиотеке, пропускаем
				continue;
			}
			if (!message.equals("ok")) {
				errors.add(new ValidationErrorImpl(message, path, val));
			}
		}
	}


	/**
	 * Метод для получения сообщения обработчика
	 *
	 * @param annotation - аннотация
	 * @param val        - значение поля
	 * @return сообщение обработчика
	 */
	private String getProcessMessage(Annotation annotation, Object val) {
		var annotationName = annotation.annotationType().getSimpleName();
		var className = "nlaban.hw4.lib.processors." + annotationName + "Processor";
		AnnotationProcessor processor;

		// Получаем нужный обработчик
		try {
			processor = (AnnotationProcessor) Class.forName(className).getConstructor().newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException |
				IllegalAccessException | InstantiationException |
				InvocationTargetException e) {
			return "unknown annotation";
		}

		return processor.process(val, annotation);
	}
}