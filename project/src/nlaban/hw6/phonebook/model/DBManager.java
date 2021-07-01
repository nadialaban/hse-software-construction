package nlaban.hw6.phonebook.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    static String DERBY_URL = "jdbc:derby:phonebook-db;create=true";

    public static void setDBName(String name) {
        DERBY_URL.replace("phonebook-db", name);
    }


    // Запросы
    static final String CREATE_QUERY = "CREATE TABLE phonebook  (" +
            "id                     bigint CONSTRAINT phonebook_pk PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
            "firstname              varchar(255) NOT NULL," +
            "lastname               varchar(255) NOT NULL," +
            "middlename             varchar(255)," +
            "mobile_phone_number    varchar(20)," +
            "home_phone_number      varchar(20)," +
            "address                varchar(255)," +
            "note                   varchar(255)," +
            "birthdate              date," +
            "CONSTRAINT fullname UNIQUE (firstname, lastname, middlename))";
    static final String INSERT_QUERY = "INSERT INTO phonebook (" +
            "firstname,lastname,middlename," +
            "mobile_phone_number,home_phone_number," +
            "address,note,birthdate) " +
            "VALUES (?,?,?,?,?,?,?,?)";
    static final String UPDATE_QUERY = "UPDATE phonebook " +
            "SET firstname=?, lastname=?, middlename=?, " +
            "mobile_phone_number=?, home_phone_number=?," +
            "address=?, note=?, birthdate=? " +
            "WHERE id=?";
    static final String GET_BY_FN_QUERY = "SELECT * FROM phonebook WHERE firstname=? AND lastname=? AND middlename=?";
    static final String GET_BY_ID_QUERY = "SELECT * FROM phonebook WHERE id=?";
    static final String DELETE_QUERY = "DELETE FROM phonebook WHERE id=?";
    static final String FILTER_QUERY = "SELECT * FROM phonebook WHERE firstname LIKE ? OR lastname LIKE ? OR middlename LIKE ?";


    /**
     * Создание БД
     */
    public static void createDB() {
        try (Connection connection = DriverManager.getConnection(DERBY_URL);
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_QUERY);
        } catch (SQLException e) {
            System.out.println("Table already exists");
        }
    }

    /**
     * Удалить все из БД
     */
    public static void clearDB() {
        try (Connection connection = DriverManager.getConnection(DERBY_URL);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM phonebook");
        } catch (SQLException e) {
            System.out.println("Some SQL problems");
        }
    }

    /**
     * Получение всех контактов из БД
     *
     * @return список контактов
     */
    public static List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DERBY_URL);
             Statement statement = connection.createStatement()) {
            var result = statement.executeQuery("SELECT * FROM phonebook");
            while (result.next()) {
                contacts.add(new Contact(result));
            }
        } catch (SQLException e) {
            System.out.println("Some SQL problems");
        }
        return contacts;
    }

    /**
     * Фильтрация всех контактов из БД
     *
     * @return список контактов
     */
    public static List<Contact> filterContacts(String str) {
        List<Contact> contacts = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DERBY_URL);
             PreparedStatement statement = connection.prepareStatement(FILTER_QUERY)) {
            var pattern = "%" + str + "%";
            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);

            var result = statement.executeQuery();
            while (result.next()) {
                contacts.add(new Contact(result));
            }
        } catch (SQLException e) {
            System.out.print("Some SQL problems");
        }
        return contacts;
    }

    /**
     * Получение контакта по имени
     *
     * @param firstname  - имя
     * @param middlename - отчество
     * @param lastname   - фамилия
     * @return контакт
     */
    public static Contact getContactByFullName(String firstname, String middlename, String lastname) {
        try (Connection connection = DriverManager.getConnection(DERBY_URL);
             PreparedStatement statement = connection.prepareStatement(GET_BY_FN_QUERY)) {
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, middlename);

            var res = statement.executeQuery();

            if (res.next()) {
                return new Contact(res);
            }
        } catch (SQLException e) {
            System.out.print("Some SQL problems");
        }
        return null;
    }

    /**
     * Получение контакта по имени
     *
     * @param id - имя
     * @return контакт
     */
    public static Contact getContactByID(long id) {
        try (Connection connection = DriverManager.getConnection(DERBY_URL);
             PreparedStatement statement = connection.prepareStatement(GET_BY_ID_QUERY)) {
            statement.setLong(1, id);

            var res = statement.executeQuery();

            if (res.next()) {
                return new Contact(res);
            }
        } catch (SQLException e) {
            System.out.print("Some SQL problems");
        }
        return null;
    }

    /**
     * Удаление контакта
     *
     * @param id - идентификатор удаленного контакта
     */
    public static void deleteContact(long id) {
        try (Connection connection = DriverManager.getConnection(DERBY_URL);
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Some SQL problems");
        }
    }

    /**
     * Добавление контакта
     *
     * @param contact - контакт
     * @return id добавленного контакта и -1 при ошибке
     */
    public static long addContact(Contact contact) {
        try (Connection connection = DriverManager.getConnection(DERBY_URL);
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            setStatementValues(contact, statement);
            statement.executeUpdate();
            var added_contact = getContactByFullName(contact.getFirstName(), contact.getMiddleName(), contact.getLastName());

            if (added_contact != null)
                return added_contact.getId();
        } catch (SQLException e) {
            System.out.println("Some SQL problems");
        }
        return -1;
    }

    /**
     * Обновление контакта
     *
     * @param contact - контакт
     */
    public static void updateContact(Contact contact) {
        try (Connection connection = DriverManager.getConnection(DERBY_URL);
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            setStatementValues(contact, statement);
            statement.setLong(9, contact.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.print("Some SQL problems");
        }
    }

    /**
     * Заполнение запроса
     *
     * @param contact   - контакт
     * @param statement - запрос
     * @throws SQLException при ошибке в запросе
     */
    private static void setStatementValues(Contact contact, PreparedStatement statement) throws SQLException {
        statement.setString(1, contact.getFirstName());
        statement.setString(2, contact.getLastName());
        statement.setString(3, contact.getMiddleName());
        statement.setString(4, contact.getMobilePhoneNumber());
        statement.setString(5, contact.getHomePhoneNumber());
        statement.setString(6, contact.getAddress());
        statement.setString(7, contact.getNote());
        var date = contact.getBD() == null ? null : Date.valueOf(contact.getBD());
        statement.setDate(8, date);
    }

}
