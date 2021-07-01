package nlaban.hw5.phonebook.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nlaban.hw5.phonebook.model.Contact;
import nlaban.hw5.phonebook.model.PhoneBook;

public class MainViewController {

	public TextField searchField;
	public TableView<Contact> table;
	public TableColumn<Contact, String> lNameColumn;
	public TableColumn<Contact, String> fNameColumn;
	public TableColumn<Contact, String> mNameColumn;
	public TableColumn<Contact, String> phonesColumn;
	public TableColumn<Contact, String> addressColumn;
	public TableColumn<Contact, String> dateColumn;
	public TableColumn<Contact, String> noteColumn;
	public Button deleteButton;

	/**
	 * Инициализация
	 */
	public void initialize() {
		fNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		mNameColumn.setCellValueFactory(new PropertyValueFactory<>("middleName"));
		lNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		phonesColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumbers"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));

		try {
			PhoneBook.onOpen();
		} catch (IOException e) {
			e.printStackTrace();
		}

		showContacts();
	}

	/**
	 * Отображение контактов в таблице
	 */
	public void showContacts() {
		table.getItems().clear();
		table.getItems().addAll(PhoneBook.getCurrentContacts());
	}

	/**
	 * Создание контакта
	 *
	 * @param actionEvent - событие
	 */
	public void createContact(ActionEvent actionEvent) {
		Stage editStage = new Stage();

		try {
			FXMLLoader root = new FXMLLoader(
					EditViewController.class.getResource("/view/editView.fxml"));
			Parent rootPane = root.load();

			editStage.setResizable(false);
			editStage.setScene(new Scene(rootPane));
			editStage.setTitle("Добавить контакт");
			editStage.initModality(Modality.APPLICATION_MODAL);
			editStage.showAndWait();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		showContacts();
	}

	/**
	 * Редактирование контакта
	 *
	 * @param actionEvent - событие
	 */
	public void editSelectedContact(ActionEvent actionEvent) {
		var selected = table.getSelectionModel().getSelectedItem();

		if (selected != null) {
			Stage editStage = new Stage();

			try {
				FXMLLoader root = new FXMLLoader(
						EditViewController.class.getResource("/view/editView.fxml"));
				Parent rootPane = root.load();

				editStage.setResizable(false);
				((EditViewController) root.getController()).setContact(selected);
				((EditViewController) root.getController()).setFullName(selected.getFullName());
				((EditViewController) root.getController()).setEdit(true);

				((EditViewController) root.getController()).initialize();

				editStage.setScene(new Scene(rootPane));
				editStage.setTitle("Редактировать контакт");
				editStage.initModality(Modality.APPLICATION_MODAL);
				editStage.showAndWait();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

			showContacts();
		}
	}

	/**
	 * Удаление контакта
	 *
	 * @param actionEvent - событие
	 */
	public void deleteSelectedContact(ActionEvent actionEvent) {
		var selected = table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			PhoneBook.removeContact(selected.getFullName());
			showContacts();
		}
	}

	/**
	 * Обработчик закрытия
	 *
	 * @param actionEvent - событие (нажатие на кнопку)
	 */
	public void exit(ActionEvent actionEvent) {
		try {
			PhoneBook.onClose();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Stage stage = (Stage) deleteButton.getScene().getWindow();
		stage.close();

	}

	/**
	 * Импорт контактов из файла
	 *
	 * @param actionEvent - событие
	 */
	public void importContacts(ActionEvent actionEvent) {
		Stage importStage = new Stage();

		try {
			FXMLLoader root = new FXMLLoader(
					ImportViewController.class.getResource("/view/importView.fxml"));
			Parent rootPane = root.load();
			importStage.setResizable(false);
			importStage.setResizable(false);
			importStage.setScene(new Scene(rootPane));
			importStage.setTitle("Импортировать контакты");
			importStage.initModality(Modality.APPLICATION_MODAL);
			importStage.showAndWait();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		showContacts();
	}

	/**
	 * Экспорт из файлов
	 *
	 * @param actionEvent - событие
	 */
	public void exportContact(ActionEvent actionEvent) {
		Stage exportStage = new Stage();

		try {
			FXMLLoader root = new FXMLLoader(
					ExportViewController.class.getResource("/view/exportView.fxml"));
			Parent rootPane = root.load();
			exportStage.setResizable(false);
			exportStage.setResizable(false);
			exportStage.setScene(new Scene(rootPane));
			exportStage.setTitle("Экспортировать контакты");
			exportStage.initModality(Modality.APPLICATION_MODAL);
			exportStage.showAndWait();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Открытие справки
	 *
	 * @param actionEvent - собыие
	 */
	public void about(ActionEvent actionEvent) {
		Stage aboutStage = new Stage();

		try {
			Parent root = FXMLLoader.load(
					AboutViewController.class.getResource("/view/aboutView.fxml"));
			aboutStage.setScene(new Scene(root));
			aboutStage.setResizable(false);
			aboutStage.setTitle("Справка");
			aboutStage.setMinHeight(150);
			aboutStage.setMinWidth(300);
			aboutStage.initModality(Modality.APPLICATION_MODAL);
			aboutStage.showAndWait();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Обработчик поиска
	 *
	 * @param actionEvent - событие (нажатие на кнопку)
	 */
	public void searchContacts(ActionEvent actionEvent) {
		var text = searchField.getText().toLowerCase();
		PhoneBook.filterContacts(c -> c.getFullName().toLowerCase().contains(text));
		showContacts();
	}

	/**
	 * Обработчик поиска
	 *
	 * @param keyEvent - событие
	 */
	public void onKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.ENTER)) {
			var text = searchField.getText().toLowerCase();
			PhoneBook.filterContacts(c -> c.getFullName().toLowerCase().contains(text));
			showContacts();
		}
	}

}
