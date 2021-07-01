package nlaban.hw5.phonebook.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import nlaban.hw5.phonebook.model.validator.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты не покрывают геттеры и обработчики исключений с отсутствием файла PhoneBook.csv (Если его
 * не удалить)
 * <p>
 * Покрытие 78% строк 68% методов (мало из-за геттеров)
 */

@DisplayName("Тестирование телефонной книги")
class PhoneBookTest {

	/**
	 * Иван Иванович - серьезный человек, У него есть полное имя и крутые номера А еще он работает в
	 * самом центре Москвы
	 */
	String ivanData = "Иван;Иванович;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'";
	Contact ivan;

	/**
	 * Вася Пупкин - простой студент с ФКН Он интроверт, поэтому сделал ошибку в номере телефона
	 * (буква О вместо 0), чтобы ему никто не звонил (особенно, научный руководитель)
	 */
	String vasilyData = "Вася;;Пупкин;88ОО5553535;;Покровский бульвар, д. 11;25 мар. 2000;'Хочу спать'";
	Contact vasily;

	/**
	 * Вовочка - пятиклассник У него строгая мама, поэтому телефона у него нет
	 */
	String vladimirData = "Вовочка;;Школьник;;;;05 янв. 2010;'Чем сейчас дети интересуются?'";
	Contact vladimir;

	@BeforeEach
	void setUp() {
		PhoneBook.contacts = new ArrayList<>();
		PhoneBook.currentContacts = new ArrayList<>();

		try {
			ivan = new Contact(ivanData);
			vasily = new Contact(vasilyData);
			vladimir = new Contact(vladimirData);
		} catch (InvalidPropertiesFormatException e) {
			// Не задйте сюда, тк данные корректны
			e.printStackTrace();
		}

	}

	@Test
	@DisplayName("Добавление контакта")
	void addContact() {
		assertEquals(0, PhoneBook.contacts.size());

		// Доавляем один элемент, проверяем что он добавился
		PhoneBook.addContact(ivan);
		assertEquals(1, PhoneBook.contacts.size());
		assertEquals(ivan.getFullName(), PhoneBook.contacts.get(0).getFullName());
	}

	@Test
	@DisplayName("Добавление существующего контакта")
	void addExistingContact() {
		assertEquals(0, PhoneBook.contacts.size());

		// Доавляем один элемент, проверяем что он добавился
		PhoneBook.addContact(ivan);
		assertEquals(1, PhoneBook.contacts.size());
		assertEquals(ivan.getFullName(), PhoneBook.contacts.get(0).getFullName());

		// Создадим лже-Ивана (его клона, который хочет именно красный кабриолет)
		// И проверим, что в телефонной книге остался старый
		Contact ivanClone = null;
		try {
			ivanClone = new Contact(ivanData.replace("кабриолет", "красный кабриолет"));
		} catch (InvalidPropertiesFormatException e) {
			// Не зайдет сюда
			e.printStackTrace();
		}

		var errors = PhoneBook.addContact(ivanClone);
		assert ivanClone != null;
		assertEquals(1, PhoneBook.contacts.size());
		assertNotEquals(ivanClone.getNote(), PhoneBook.contacts.get(0).getNote());
		assertEquals("* Контакт с таким ФИО уже существует", errors.get(0).getMessage());
	}

	@Test
	@DisplayName("Добавление контакта с неправильнвм телефоном")
	void addContactWithWrongPhone() {
		assertEquals(0, PhoneBook.contacts.size());

		// Порпробуем добавить Васю
		var errors = PhoneBook.addContact(vasily);
		assertEquals(0, PhoneBook.contacts.size());
		assertEquals("* Неверный формат номера телефона.", errors.get(0).getMessage());

		// Заставим Васю исправить свою ошибку
		try {
			vasily = new Contact(vasilyData.replace("ОО", "00"));
		} catch (InvalidPropertiesFormatException e) {
			// Не зайдет сюда
			e.printStackTrace();
		}
		PhoneBook.addContact(vasily);
		assertEquals(1, PhoneBook.contacts.size());
	}

	@Test
	@DisplayName("Добавление контакта без телефона")
	void addContactWithoutPhone() {
		assertEquals(0, PhoneBook.contacts.size());

		// Порпробуем добавить Вовочку
		var errors = PhoneBook.addContact(vladimir);
		assertEquals(0, PhoneBook.contacts.size());
		assertEquals("* Необходимо ввести хотя бы один номер телефона", errors.get(0).getMessage());

		// Отдадим Вовочке свой старый телефон,
		// Чтобы он не страдал, и запишем номер в книжку
		try {
			vladimir = new Contact(vladimirData.replace("к;", "к;88005553535"));
		} catch (InvalidPropertiesFormatException e) {
			// Не зайдет сюда
			e.printStackTrace();
		}
		PhoneBook.addContact(vladimir);
		assertEquals(1, PhoneBook.contacts.size());
	}

	@Test
	@DisplayName("Добавление контакта с неправильной фамилией")
	void addContactWithWrongLastName() {
		assertEquals(0, PhoneBook.contacts.size());

		// Порпробуем добавить Ивана Ивановича, сделав ошибку в его фамилии
		List<ValidationError> errors = null;
		try {
			errors = PhoneBook.addContact(new Contact(ivanData.replace("Иванов", "1ванов")));
		} catch (InvalidPropertiesFormatException e) {
			// Не зайдет сюда
			e.printStackTrace();
		}
		assert errors != null;
		assertEquals(0, PhoneBook.contacts.size());
		assertEquals("* Неверный формат ФИО.", errors.get(0).getMessage());

		// А теперь добавим Ивана Ивановича с фамилией
		PhoneBook.addContact(ivan);
		assertEquals(1, PhoneBook.contacts.size());
	}

	@Test
	@DisplayName("Добавление контакта без фамилии")
	void addContactWithoutLastName() {
		assertEquals(0, PhoneBook.contacts.size());

		// Порпробуем добавить Ивана Ивановича, стерев его фамилию
		List<ValidationError> errors = null;
		try {
			errors = PhoneBook.addContact(new Contact(ivanData.replace("Иванов", "")));
		} catch (InvalidPropertiesFormatException e) {
			// Не зайдет сюда
			e.printStackTrace();
		}
		assert errors != null;
		assertEquals(0, PhoneBook.contacts.size());
		assertEquals("* Обязательное поле", errors.get(1).getMessage());

		// А теперь добавим Ивана Ивановича с фамилией
		PhoneBook.addContact(ivan);
		assertEquals(1, PhoneBook.contacts.size());
	}

	@Test
	@DisplayName("Удаление контакта")
	void removeContact() {
		assertEquals(0, PhoneBook.contacts.size());

		// Добавим Ивана Ивановича
		PhoneBook.addContact(ivan);
		assertEquals(1, PhoneBook.contacts.size());

		// Удалим Ивана Ивановича (ой)
		PhoneBook.removeContact(ivan.getFullName());
		assertEquals(0, PhoneBook.contacts.size());
	}

	@Test
	@DisplayName("Получение контакта по имени")
	void getContact() {
		PhoneBook.addContact(ivan);
		var found = PhoneBook.getContact(ivan.getFullName());
		assertEquals(ivan, found);
	}

	@Test
	@DisplayName("Правильное редактирование контакта")
	void editCorrectContact() {
		PhoneBook.addContact(ivan);

		// Будем редактировать Ивана
		// Теперь он хочет красный кабриолет (кризис среднего возраста - не шутка)
		Contact ivanEdited = null;
		try {
			ivanEdited = new Contact(ivanData.replace("кабриолет", "красный кабриолет"));
		} catch (InvalidPropertiesFormatException e) {
			// Не зайдет сюда
			e.printStackTrace();
		}
		var errors = PhoneBook.editContact(ivan.getFullName(), ivanEdited);
		assertEquals(0, errors.size());
		assertEquals("Хочу красный кабриолет", PhoneBook.contacts.get(0).getNote());
	}

	@Test
	@DisplayName("Неправильное редактирование контакта")
	void editIncorrectContact() {
		PhoneBook.addContact(ivan);

		// Иван замечтался и вместо своего блатного номера случайно написал "красный кабриолет"
		Contact ivanEdited = null;
		try {
			ivanEdited = new Contact(ivanData.replace("+7(977)777-77-77", "красный кабриолет"));
		} catch (InvalidPropertiesFormatException e) {
			// Не зайдет сюда
			e.printStackTrace();
		}
		var errors = PhoneBook.editContact(ivan.getFullName(), ivanEdited);
		assertEquals(1, errors.size());
		assertEquals("* Неверный формат номера телефона.", errors.get(0).getMessage());
		assertEquals("+7(977)777-77-77", PhoneBook.contacts.get(0).getMobilePhoneNumber());
	}

	@Test
	@DisplayName("Импорт/экспорт")
	void exportImportContacts() {
		// Добавим несколько человек
		try {
			PhoneBook.addContact(new Contact(
					"Иван;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Петр;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Петр;;Петров;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Василий;;Васильев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Алексей;;Алексеев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		}

		assertEquals(5, PhoneBook.contacts.size());

		// Экспортируем
		try {
			PhoneBook.exportContacts("test.csv", true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Чистим
		PhoneBook.contacts.clear();

		// Импортируем
		try {
			PhoneBook.importContacts("test.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Контакты восстановились
		assertEquals(5, PhoneBook.contacts.size());
	}

	@Test
	@DisplayName("Импорт ломанного файла")
	void importContactsWithErrors() {
		// Импортируем
		try {
			PhoneBook.importContacts("testErrors.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Правильные контакты восстановились
		assertEquals(1, PhoneBook.contacts.size());
	}

	@Test
	@DisplayName("Фильтрация контактов")
	void filterContacts() {
		// Добавим несколько человек
		try {
			PhoneBook.addContact(new Contact(
					"Иван;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Петр;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Петр;;Петров;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Василий;;Васильев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Алексей;;Алексеев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		}

		// Найдем всех, у кого в фамилии или в имени есть Иван
		PhoneBook.filterContacts(c -> c.getFullName().toLowerCase().contains("иван"));

		// Таких должно быть двое
		assertEquals(2, PhoneBook.currentContacts.size());
	}

	@Test
	@DisplayName("Закрытие/открытие")
	void onClose() {
		// Добавим несколько человек
		try {
			PhoneBook.addContact(new Contact(
					"Иван;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Петр;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Петр;;Петров;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Василий;;Васильев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
			PhoneBook.addContact(new Contact(
					"Алексей;;Алексеев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;'Хочу кабриолет'"));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		}

		assertEquals(5, PhoneBook.contacts.size());

		// Сымитируем закрытие
		try {
			PhoneBook.onClose();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Чистим
		PhoneBook.contacts.clear();

		// Сымитируем открытие
		try {
			PhoneBook.onOpen();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Контакты восстановились
		assertEquals(5, PhoneBook.contacts.size());
	}
}