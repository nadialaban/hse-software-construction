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

public class ExistsConflictViewController {

    public TextField lastNameExistingTextField;
    public Label noteImportedErrorLabel;
    public TextArea noteImportedTextArea;
    public Label contactImportedErrorLabel;
    public Label homePhoneNumberImportedErrorLabel;
    public Label mobilePhoneNumberImportedErrorLabel;
    public Label middleNameImportedErrorLabel;
    public Label lastNameImportedErrorLabel;
    public Label firstNameImportedErrorLabel;
    public DatePicker datePickerImported;
    public TextField addressImportedTextField;
    public TextField homePhoneNumberImportedTextField;
    public TextField mobilePhoneNumberImportedTextField;
    public TextField middleNameImportedTextField;
    public TextField firstNameImportedTextField;
    public TextField lastNameImportedTextField;
    public TextArea noteExistingTextArea;
    public DatePicker datePickerExisting;
    public TextField addressExistingTextField;
    public TextField homePhoneNumberExistingTextField;
    public TextField mobilePhoneNumberExistingTextField;
    public TextField middleNameExistingTextField;
    public TextField firstNameExistingTextField;

    private Contact oldContact = new Contact();
    private Contact newContact = new Contact();
    private String fullName = "";

    /**
     * Инициализация
     */
    public void initialize() {
        lastNameExistingTextField.setText(oldContact.getLastName());
        firstNameExistingTextField.setText(oldContact.getFirstName());
        middleNameExistingTextField.setText(oldContact.getMiddleName());

        mobilePhoneNumberExistingTextField.setText(oldContact.getMobilePhoneNumber());
        homePhoneNumberExistingTextField.setText(oldContact.getHomePhoneNumber());

        addressExistingTextField.setText(oldContact.getAddress());
        noteExistingTextArea.setText(oldContact.getNote());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        if (!oldContact.getBirthDate().equals("")) {
            datePickerExisting.setValue(LocalDate.parse(oldContact.getBirthDate(), formatter));
        }

        lastNameImportedTextField.setText(newContact.getLastName());
        firstNameImportedTextField.setText(newContact.getFirstName());
        middleNameImportedTextField.setText(newContact.getMiddleName());

        mobilePhoneNumberImportedTextField.setText(newContact.getMobilePhoneNumber());
        homePhoneNumberImportedTextField.setText(newContact.getHomePhoneNumber());

        addressImportedTextField.setText(newContact.getAddress());
        noteImportedTextArea.setText(newContact.getNote());

        if (!newContact.getBirthDate().equals("")) {
            datePickerImported.setValue(LocalDate.parse(newContact.getBirthDate(), formatter));
        }
    }

    public void setOldContact(Contact oldContact) {
        this.oldContact = oldContact;
    }

    public void setNewContact(Contact newContact) {
        this.newContact = newContact;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Если сохраняем старый, просто выходим из формы
     *
     * @param actionEvent - событие
     */
    public void saveExisting(ActionEvent actionEvent) {
        close(actionEvent);
    }

    /**
     * Сохранение импортированного контакта
     *
     * @param actionEvent - событие
     */
    public void saveImported(ActionEvent actionEvent) {
        String firstName = firstNameImportedTextField.getText(),
                middleName = middleNameImportedTextField.getText(),
                lastName = lastNameImportedTextField.getText();

        String mobilePhoneNumber = mobilePhoneNumberImportedTextField.getText(),
                homePhoneNumber = homePhoneNumberImportedTextField.getText();

        String address = addressImportedTextField.getText(),
                note = noteImportedTextArea.getText();
        Date birthDate = null;
        if (datePickerImported.getValue() != null) {
            birthDate = java.sql.Date.valueOf(datePickerImported.getValue());
        }

        newContact = new Contact(firstName, middleName, lastName,
                mobilePhoneNumber, homePhoneNumber, address, birthDate, note);
        newContact.setId(oldContact.getId());

        List<ValidationError> errors = PhoneBook.editContact(newContact);

        clearErrors();

        if (errors != null && !errors.isEmpty()) {
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
            try {
                Field field = this.getClass().getDeclaredField(error.getSender() + "ImportedErrorLabel");
                field.setAccessible(true);
                Object value = field.get(this);
                ((Label) value).setText(error.getMessage());

                Field fieldFrame = this.getClass()
                        .getDeclaredField(error.getSender() + "ImportedTextField");
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
                Field field = this.getClass().getDeclaredField(f + "ImportedErrorLabel");
                field.setAccessible(true);
                Object value = field.get(this);
                ((Label) value).setText("");

                Field fieldFrame = this.getClass().getDeclaredField(f + "ImportedTextField");
                fieldFrame.setAccessible(true);
                Object valueFrame = fieldFrame.get(this);
                ((TextField) valueFrame).setStyle("-fx-border-color: transparent;");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Не должно сюда заходить
                e.printStackTrace();
            }
        }

        noteImportedErrorLabel.setText("");
        contactImportedErrorLabel.setText("");

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
