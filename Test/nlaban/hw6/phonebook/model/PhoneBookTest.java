package nlaban.hw6.phonebook.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import nlaban.hw6.phonebook.model.validator.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Тестирование телефонной книги")
class PhoneBookTest {

    String ivanData = "Иван;Иванович;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий";
    Contact ivan;

    String vasilyData = "Вася;;Пупкин;88ОО5553535;;Покровский бульвар, д. 11;25 мар. 2000;Комментарий";
    Contact vasily;

    String vladimirData = "Вовочка;;Школьник;;;;05 янв. 2010;Комментарий";
    Contact vladimir;

    @BeforeEach
    void setUp() {
        DBManager.createDB();
        DBManager.clearDB();

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
        assertEquals(0, PhoneBook.getContacts().size());

        // Доавляем один элемент, проверяем что он добавился в бд
        PhoneBook.addContact(ivan);
        assertEquals(1, PhoneBook.getContacts().size());
        assertEquals(ivan.getFullName(), PhoneBook.getContacts().get(0).getFullName());
    }

    @Test
    @DisplayName("Добавление существующего контакта")
    void addExistingContact() {
        assertEquals(0, PhoneBook.getContacts().size());

        // Доавляем один элемент, проверяем что он добавился
        PhoneBook.addContact(ivan);
        assertEquals(1, PhoneBook.getContacts().size());
        assertEquals(ivan.getFullName(), PhoneBook.getContacts().get(0).getFullName());

        // Создадим копию Ивана с другрим комментарием
        // И проверим, что в телефонной книге остался старый
        Contact ivanClone = null;
        try {
            ivanClone = new Contact(ivanData.replace("Комментарий", "Другой комментарий"));
        } catch (InvalidPropertiesFormatException e) {
            // Не зайдет сюда
            e.printStackTrace();
        }

        var errors = PhoneBook.addContact(ivanClone);
        assert ivanClone != null;
        assertEquals(1, PhoneBook.getContacts().size());
        assertNotEquals(ivanClone.getNote(), PhoneBook.getContacts().get(0).getNote());
        assertEquals("* Контакт с таким ФИО уже существует", errors.get(0).getMessage());
    }

    @Test
    @DisplayName("Добавление контакта с неправильнвм телефоном")
    void addContactWithWrongPhone() {
        assertEquals(0, PhoneBook.getContacts().size());

        // Порпробуем добавить контакт с неправильным номером телефона (OO вместо 00)
        var errors = PhoneBook.addContact(vasily);
        assertEquals(0, PhoneBook.getContacts().size());
        assertEquals("* Неверный формат номера телефона.", errors.get(0).getMessage());

        // Исправляем
        try {
            vasily = new Contact(vasilyData.replace("ОО", "00"));
        } catch (InvalidPropertiesFormatException e) {
            // Не зайдет сюда
            e.printStackTrace();
        }
        PhoneBook.addContact(vasily);
        assertEquals(1, PhoneBook.getContacts().size());
    }

    @Test
    @DisplayName("Добавление контакта без телефона")
    void addContactWithoutPhone() {
        assertEquals(0, PhoneBook.getContacts().size());

        // Порпробуем добавить контакт без номеров
        var errors = PhoneBook.addContact(vladimir);
        assertEquals(0, PhoneBook.getContacts().size());
        assertEquals("* Необходимо ввести хотя бы один номер телефона", errors.get(0).getMessage());

        // Дообавляем номер телефона
        try {
            vladimir = new Contact(vladimirData.replace("к;", "к;88005553535"));
        } catch (InvalidPropertiesFormatException e) {
            // Не зайдет сюда
            e.printStackTrace();
        }
        PhoneBook.addContact(vladimir);
        assertEquals(1, PhoneBook.getContacts().size());
    }

    @Test
    @DisplayName("Добавление контакта с неправильной фамилией")
    void addContactWithWrongLastName() {
        assertEquals(0, PhoneBook.getContacts().size());

        // Порпробуем добавить контакт, сделав ошибку в его фамилии
        List<ValidationError> errors = null;
        try {
            errors = PhoneBook.addContact(new Contact(ivanData.replace("Иванов", "1ванов")));
        } catch (InvalidPropertiesFormatException e) {
            // Не зайдет сюда
            e.printStackTrace();
        }
        assert errors != null;
        assertEquals(0, PhoneBook.getContacts().size());
        assertEquals("* Неверный формат ФИО.", errors.get(0).getMessage());

        // А теперь добавим контакт с правильной фамилией
        PhoneBook.addContact(ivan);
        assertEquals(1, PhoneBook.getContacts().size());
    }

    @Test
    @DisplayName("Добавление контакта без фамилии")
    void addContactWithoutLastName() {
        assertEquals(0, PhoneBook.getContacts().size());

        // Порпробуем добавить контакт без фамилии
        List<ValidationError> errors = null;
        try {
            errors = PhoneBook.addContact(new Contact(ivanData.replace("Иванов", "")));
        } catch (InvalidPropertiesFormatException e) {
            // Не зайдет сюда
            e.printStackTrace();
        }
        assert errors != null;
        assertEquals(0, PhoneBook.getContacts().size());
        assertEquals("* Обязательное поле", errors.get(1).getMessage());

        // А теперь добавим контакт с фамилией
        PhoneBook.addContact(ivan);
        assertEquals(1, PhoneBook.getContacts().size());
    }

    @Test
    @DisplayName("Удаление контакта")
    void removeContact() {
        assertEquals(0, PhoneBook.getContacts().size());

        // Добавим контакт
        PhoneBook.addContact(ivan);
        assertEquals(1, PhoneBook.getContacts().size());

        // Удалим контакт
        PhoneBook.removeContact(ivan.getId());
        assertEquals(0, PhoneBook.getContacts().size());
    }

    @Test
    @DisplayName("Получение контакта по имени")
    void getContact() {
        PhoneBook.addContact(ivan);
        var found = DBManager.getContactByFullName(ivan.getFirstName(), ivan.getMiddleName(), ivan.getLastName());
        assertEquals(ivan.getFullName(), found.getFullName());
    }

    @Test
    @DisplayName("Правильное редактирование контакта")
    void editCorrectContact() {
        PhoneBook.addContact(ivan);

        // Отредактируем комментарий
        Contact ivanEdited = null;
        try {
            ivanEdited = new Contact(ivanData.replace("Комментарий", "Отредактированный комментарий"));
        } catch (InvalidPropertiesFormatException e) {
            // Не зайдет сюда
            e.printStackTrace();
        }
        ivanEdited.setId(ivan.getId());
        var errors = PhoneBook.editContact(ivanEdited);
        assertEquals(0, errors.size());
        assertEquals("Отредактированный комментарий", PhoneBook.getContacts().get(0).getNote());
    }

    @Test
    @DisplayName("Неправильное редактирование контакта")
    void editIncorrectContact() {
        PhoneBook.addContact(ivan);

        // Изменим номер
        Contact ivanEdited = null;
        try {
            ivanEdited = new Contact(ivanData.replace("+7(977)777-77-77", "не номер"));
        } catch (InvalidPropertiesFormatException e) {
            // Не зайдет сюда
            e.printStackTrace();
        }
        ivanEdited.setId(ivan.getId());
        var errors = PhoneBook.editContact(ivanEdited);
        assertEquals(1, errors.size());
        assertEquals("* Неверный формат номера телефона.", errors.get(0).getMessage());
        assertEquals("+7(977)777-77-77", PhoneBook.getContacts().get(0).getMobilePhoneNumber());
    }

    @Test
    @DisplayName("Импорт/экспорт")
    void exportImportContacts() {
        // Добавим несколько человек
        try {
            PhoneBook.addContact(new Contact(
                    "Иван;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
            PhoneBook.addContact(new Contact(
                    "Петр;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
            PhoneBook.addContact(new Contact(
                    "Петр;;Петров;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
            PhoneBook.addContact(new Contact(
                    "Василий;;Васильев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
            PhoneBook.addContact(new Contact(
                    "Алексей;;Алексеев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        }

        assertEquals(5, PhoneBook.getContacts().size());

        // Экспортируем
        try {
            PhoneBook.exportContacts("test.csv", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Чистим
        PhoneBook.getContacts().clear();

        // Импортируем
        try {
            PhoneBook.importContacts("test.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Контакты восстановились
        assertEquals(5, PhoneBook.getContacts().size());
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
        assertEquals(1, PhoneBook.getContacts().size());
    }

    @Test
    @DisplayName("Фильтрация контактов")
    void filterContacts() {
        // Добавим несколько человек
        try {
            PhoneBook.addContact(new Contact(
                    "Иван;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
            PhoneBook.addContact(new Contact(
                    "Петр;;Иванов;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
            PhoneBook.addContact(new Contact(
                    "Петр;;Петров;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
            PhoneBook.addContact(new Contact(
                    "Василий;;Васильев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
            PhoneBook.addContact(new Contact(
                    "Алексей;;Алексеев;+7(977)777-77-77;88005553535;Красная площадь, д. 1;01 янв. 1990;Комментарий"));
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        }

        // Найдем всех, у кого в фамилии или в имени есть Иван
        PhoneBook.filterContacts("Иван");

        // Таких должно быть двое
        assertEquals(2, PhoneBook.currentContacts.size());
    }

}