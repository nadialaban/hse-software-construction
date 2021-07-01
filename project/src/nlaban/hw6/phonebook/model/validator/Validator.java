package nlaban.hw6.phonebook.model.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import nlaban.hw6.phonebook.model.Contact;
import nlaban.hw6.phonebook.model.DBManager;
import nlaban.hw6.phonebook.model.validator.handlers.AnnotationHandler;

// Валидатор стащен из дз4, но я убрала ненужные вещи
// (обработку constrained, списков итд)
// Просто для удобства

public class Validator {

    /**
     * Метод проверки объекта
     *
     * @param object - объект
     * @return сет ошибок
     */
    public static ArrayList<ValidationError> validate(Contact object) {
        ArrayList<ValidationError> errors = new ArrayList<>();

        if (object != null) {
            var fullName = object.getFullName();
            var id = object.getId();

            var old = DBManager.getContactByFullName(object.getFirstName(), object.getMiddleName(), object.getLastName());
            if (old != null && old.getId() != id) {
                errors.add(new ValidationError("* Контакт с таким ФИО уже существует",
                        "contact", fullName, old.getId(), object.toString()));
                return errors;
            }

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
                if (val == null) {
                    continue;
                }

                var annotations = new ArrayList<>(List.of(field.getAnnotatedType().getAnnotations()));
                String message;

                // Проходим аннотации
                for (var annotation : annotations) {
                    message = getProcessMessage(annotation, val);
                    // Записываем интересующие нас ошибки
                    if (message != null) {
                        errors.add(new ValidationError(message, field.getName(), fullName, id, object.toString()));
                    }
                }

                if (val.toString().contains(";")) {
                    errors.add(new ValidationError("* Значение не может содержать символ ;", field.getName(),
                            fullName, id, object.toString()));
                }

            }

            var phoneErrors = errors.stream()
                    .filter(e -> e.getMessage().equals("* Необходимо ввести хотя бы один номер телефона"))
                    .toArray();
            if (phoneErrors.length == 1) {
                errors.removeIf(e -> e.getMessage().equals("* Необходимо ввести хотя бы один номер телефона"));
            }

        }

        return errors;
    }

    /**
     * Метод для получения сообщения обработчика
     *
     * @param annotation - аннотация
     * @param val        - значение поля
     * @return сообщение обработчика
     */
    private static String getProcessMessage(Annotation annotation, Object val) {
        var annotationName = annotation.annotationType().getSimpleName();
        var className = "nlaban.hw6.phonebook.model.validator.handlers." + annotationName + "Handler";
        AnnotationHandler handler;

        // Получаем нужный обработчик
        try {
            handler = (AnnotationHandler) Class.forName(className).getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException |
                IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
            // не должно сюда заходить в контексте нашей программы
            return "unknown annotation";
        }

        return handler.handle(val, annotation);
    }
}