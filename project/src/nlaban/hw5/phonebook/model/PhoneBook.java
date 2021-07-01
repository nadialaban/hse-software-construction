package nlaban.hw5.phonebook.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.function.Predicate;
import nlaban.hw5.phonebook.model.validator.ValidationError;
import nlaban.hw5.phonebook.model.validator.Validator;

public class PhoneBook {

	// Все контакты
	static List<Contact> contacts = new ArrayList<>();

	// Отображаемые контакты
	static List<Contact> currentContacts = new ArrayList<>();

	public static List<Contact> getCurrentContacts() {
		return currentContacts;
	}

	public static List<Contact> getContacts() {
		return contacts;
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
			contacts.add(contact);
			currentContacts.add(contact);
			return errors;
		}

		return errors;
	}

	/**
	 * Удвление контакта
	 *
	 * @param fullName - полное имя
	 */
	public static void removeContact(String fullName) {
		contacts.removeIf(c -> c.getFullName().equals(fullName));
		currentContacts.removeIf(c -> c.getFullName().equals(fullName));
	}

	/**
	 * Поиск по полному имени
	 *
	 * @param fullName - плное имя
	 * @return контакт
	 */
	public static Contact getContact(String fullName) {
		return (Contact) contacts.stream().filter(c -> c.getFullName().equals(fullName)).toArray()[0];
	}

	/**
	 * Редактирование контакта
	 *
	 * @param oldFullName - изначальное полное имя
	 * @param contact     - измененный контакт
	 * @return ошибки или null, если все хорошо
	 */
	public static List<ValidationError> editContact(String oldFullName, Contact contact) {
		var tmp = contacts.stream().filter(c -> c.getFullName().equals(oldFullName)).toArray();
		Contact old = (Contact) tmp[0];
		old.setEditing(true);

		var errors = Validator.validate(contact);

		if (errors.isEmpty()) {
			contacts.remove(old);
			contacts.add(contact);
			currentContacts.remove(old);
			currentContacts.add(contact);
			return errors;
		}

		old.setEditing(false);

		return errors;
	}

	/**
	 * Фильтрация контактов
	 *
	 * @param predicate - условие фильтрации
	 */
	public static void filterContacts(Predicate<Contact> predicate) {
		var filtered = contacts.stream().filter(predicate).toArray();
		currentContacts.clear();
		for (var contact : filtered) {
			currentContacts.add((Contact) contact);
		}
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
				errors.add(new ValidationError(e.getMessage(), row, "?", "?"));
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
	 * Импорт контактов при запуске
	 *
	 * @return конфликты
	 * @throws IOException при проблемах с созданнием файла
	 */
	public static List<ValidationError> onOpen() throws IOException {
		List<ValidationError> errors = new ArrayList<>();
		try {
			errors = importContacts("PhoneBook.csv");
		} catch (IOException e) {
			File file = new File("PhoneBook.csv");
			file.createNewFile();
		}

		return errors;
	}

	/**
	 * Экспорт контактов при выключении
	 *
	 * @throws IOException при проблемах с открытием файла
	 */
	public static void onClose() throws IOException {
		try {
			exportContacts("PhoneBook.csv", true);
		} catch (IOException e) {
			File file = new File("PhoneBook.csv");
			file.createNewFile();
			exportContacts("PhoneBook.csv", true);
		}
	}
}
