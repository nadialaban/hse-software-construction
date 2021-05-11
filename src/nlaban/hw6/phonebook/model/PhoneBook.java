package nlaban.hw6.phonebook.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import nlaban.hw6.phonebook.model.validator.ValidationError;
import nlaban.hw6.phonebook.model.validator.Validator;

public class PhoneBook {
    // Отображаемые контакты
    static List<Contact> currentContacts = new ArrayList<>();

    public static List<Contact> getCurrentContacts() {
        return currentContacts;
    }

    public static List<Contact> getContacts() {
        return DBManager.getAllContacts();
    }

    /**
     * Валидация и добавление контакта
     *
     * @param contact - контакт
     * @return ошибки или null, если все хорошо
     */
    public static List<ValidationError> addContact(Contact contact) {
        var errors = Validator.validate(contact);

        if (errors.isEmpty()) {
            contact.setId(DBManager.addContact(contact));
            currentContacts.add(contact);
            return errors;
        }

        return errors;
    }

    /**
     * Удаление контакта
     *
     * @param id - идентификатор контакта
     */
    public static void removeContact(long id) {
        DBManager.deleteContact(id);
        currentContacts.removeIf(c -> c.getId() == id);
    }

    /**
     * Редактирование контакта
     *
     * @param contact - измененный контакт
     * @return ошибки или null, если все хорошо
     */
    public static List<ValidationError> editContact(Contact contact) {
        var errors = Validator.validate(contact);

        if (errors.isEmpty()) {
            DBManager.updateContact(contact);

            currentContacts.removeIf(c -> c.getId() == contact.getId());
            currentContacts.add(contact);
            return errors;
        }

        return errors;
    }

    /**
     * Фильтрация контактов
     *
     * @param str - условие фильтрации
     */
    public static void filterContacts(String str) {
        currentContacts = DBManager.filterContacts(str);
    }

    /**
     * Импорт контактов из CSV файла
     *
     * @param path - путь к CSV
     * @return список ошибок и конфликтов
     * @throws IOException, если есть проблемы с файлом
     */
    public static List<ValidationError> importContacts(String path) throws IOException {
        var errors = new ArrayList<ValidationError>();
        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        String row;

        // Заголовок
        csvReader.readLine();
        // Читаем строки
        while ((row = csvReader.readLine()) != null) {
            Contact newContact;
            try {
                newContact = new Contact(row);
            } catch (InvalidPropertiesFormatException e) {
                errors.add(new ValidationError(e.getMessage(), row, "?", -1, "?"));
                continue;
            }
            errors.addAll(addContact(newContact));
        }

        csvReader.close();
        return errors;
    }

    /**
     * Экспорт контактов
     *
     * @param path - Путь к файлу
     */
    public static void exportContacts(String path, boolean all) throws IOException {
        FileWriter csvWriter = new FileWriter(path);
        csvWriter.append("firstName;middleName;lastName");
        csvWriter.append(";mobilePhoneNumber;homePhoneNumber");
        csvWriter.append(";address;birthDate;note");
        var contacts = getContacts();

        if (all) {
            for (Contact contact : contacts) {
                csvWriter.append("\n");
                csvWriter.append(contact.toString());
            }
        } else {
            for (Contact contact : currentContacts) {
                csvWriter.append("\n");
                csvWriter.append(contact.toString());
            }
        }

        csvWriter.flush();
        csvWriter.close();
    }

    /**
     * Создание таблицы
     */
    public static void onOpen() {
        DBManager.createDB();
        currentContacts = getContacts();
    }

}
