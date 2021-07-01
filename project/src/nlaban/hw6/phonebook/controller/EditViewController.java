package nlaban.hw6.phonebook.controller;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nlaban.hw6.phonebook.model.Contact;
import nlaban.hw6.phonebook.model.PhoneBook;
import nlaban.hw6.phonebook.model.validator.ValidationError;

public class EditViewController {

	public TextField lastNameTextField;
	public TextField firstNameTextField;
	public TextField middleNameTextField;
	public TextField mobilePhoneNumberTextField;
	public TextField homePhoneNumberTextField;
	public TextField addressTextField;
	public TextArea noteTextArea;
	public DatePicker datePicker;
	public Label lastNameErrorLabel;
	public Label firstNameErrorLabel;
	public Label middleNameErrorLabel;
	public Label mobilePhoneNumberErrorLabel;
	public Label homePhoneNumberErrorLabel;
	public Label noteErrorLabel;
	public Label contactErrorLabel;

	private boolean edit;
	private Contact contact;

	public EditViewController() {
		edit = false;
		this.contact = new Contact();
	}

	/**
	 * Инициализация
	 */
	public void initialize() {
		lastNameTextField.setText(contact.getLastName());
		firstNameTextField.setText(contact.getFirstName());
		middleNameTextField.setText(contact.getMiddleName());

		mobilePhoneNumberTextField.setText(contact.getMobilePhoneNumber());
		homePhoneNumberTextField.setText(contact.getHomePhoneNumber());

		addressTextField.setText(contact.getAddress());
		noteTextArea.setText(contact.getNote());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		if (contact.getBirthDate() != null && !contact.getBirthDate().equals("")) {
			datePicker.setValue(LocalDate.parse(contact.getBirthDate(), formatter));
		}
	}

	public void setContact(Contact contact) {
		this.contact = contact;
		System.out.println(this.contact.getId());
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	/**
	 * Сохранение контакта
	 *
	 * @param actionEvent - событие
	 */
	public void save(ActionEvent actionEvent) {
		String firstName = firstNameTextField.getText(),
				middleName = middleNameTextField.getText(),
				lastName = lastNameTextField.getText();

		String mobilePhoneNumber = mobilePhoneNumberTextField.getText(),
				homePhoneNumber = homePhoneNumberTextField.getText();

		String address = addressTextField.getText(),
				note = noteTextArea.getText();
		Date birthDate =
				datePicker.getValue() != null ? java.sql.Date.valueOf(datePicker.getValue()) : null;

		var newContact = new Contact(firstName, middleName, lastName,
				mobilePhoneNumber, homePhoneNumber, address, birthDate, note);
		newContact.setId(contact.getId());

		List<ValidationError> errors;
		if (edit) {
			errors = PhoneBook.editContact(newContact);
		} else {
			errors = PhoneBook.addContact(newContact);
		}

		clearErrors();

		if (!errors.isEmpty()) {
			showErrors(errors);
		} else {
			close(actionEvent);
		}

	}

	/**
	 * Отображение ошибок
	 *
	 * @param errors - ошибки
	 */
	private void showErrors(List<ValidationError> errors) {
		for (var error : errors) {
			if (error.getSender().equals("contact"))
				continue;
			try {
				Field field = this.getClass().getDeclaredField(error.getSender() + "ErrorLabel");
				field.setAccessible(true);
				Object value = field.get(this);
				((Label) value).setText(error.getMessage());

				Field fieldFrame = this.getClass().getDeclaredField(error.getSender() + "TextField");
				fieldFrame.setAccessible(true);
				Object valueFrame = fieldFrame.get(this);
				((TextField) valueFrame).setStyle("-fx-border-color: #FF0000;");

			} catch (NoSuchFieldException | IllegalAccessException e) {
				// Не должно сюда заходить
				e.printStackTrace();
			}

		}
	}

	/**
	 * Отчистка полей с ощибками
	 */
	public void clearErrors() {
		var fieldNames = new String[]{"lastName", "firstName", "middleName", "mobilePhoneNumber",
				"homePhoneNumber"};

		for (var f : fieldNames) {
			try {
				Field field = this.getClass().getDeclaredField(f + "ErrorLabel");
				field.setAccessible(true);
				Object value = field.get(this);
				((Label) value).setText("");

				Field fieldFrame = this.getClass().getDeclaredField(f + "TextField");
				fieldFrame.setAccessible(true);
				Object valueFrame = fieldFrame.get(this);
				((TextField) valueFrame).setStyle("-fx-border-color: transparent;");
			} catch (NoSuchFieldException | IllegalAccessException e) {
				// Не должно сюда заходить
				e.printStackTrace();
			}
		}

		noteErrorLabel.setText("");
		contactErrorLabel.setText("");

	}

	/**
	 * Закрытие окна
	 *
	 * @param actionEvent - событие
	 */
	public void close(ActionEvent actionEvent) {
		Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
		stage.close();
	}
}
