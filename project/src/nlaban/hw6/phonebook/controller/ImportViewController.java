package nlaban.hw6.phonebook.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nlaban.hw6.phonebook.model.Contact;
import nlaban.hw6.phonebook.model.DBManager;
import nlaban.hw6.phonebook.model.PhoneBook;
import nlaban.hw6.phonebook.model.validator.ValidationError;

public class ImportViewController {

    public List<ValidationError> errors = new ArrayList<>();
    public VBox conflictBox;
    public Label wrongFormatLabel;
    public TextField path;
    public Label errorLabel;
    public TableView<ValidationError> table;
    public TableColumn<ValidationError, String> lineColumn;
    public TableColumn<ValidationError, String> fieldColumn;
    public TableColumn<ValidationError, String> errorColumn;
    private int unresolved = 0;

    /**
     * Инициализация
     */
    public void initialize() {
        conflictBox.setVisible(false);
        errorLabel.setVisible(false);
        lineColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        fieldColumn.setCellValueFactory(new PropertyValueFactory<>("sender"));
        errorColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
    }

    /**
     * Открытие файла
     *
     * @param actionEvent - событие
     */
    public void open(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Импорт контактов");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)",
                "*.csv");
        fc.getExtensionFilters().add(extFilter);
        File f = fc.showOpenDialog(((Button) actionEvent.getSource()).getScene().getWindow());
        path.setText(f != null? f.getPath() : "");
    }

    /**
     * Импорт контактов
     *
     * @param actionEvent - событие
     */
    public void importContacts(ActionEvent actionEvent) {
        try {
            errors = PhoneBook.importContacts(path.getText());
            unresolved = errors.stream().filter(c -> c.getFullName().equals("?")).toArray().length;
            errors.removeIf(c -> c.getFullName().equals("?"));

            if (!errors.isEmpty()) {
                conflictBox.setVisible(true);
                wrongFormatLabel.setText(wrongFormatLabel.getText() + unresolved);
            }

            showErrors(actionEvent);

        } catch (IOException e) {
            errorLabel.setVisible(true);
        }
    }

    /**
     * Отображение ошибок
     */
    public void showErrors(ActionEvent actionEvent) {
        if (errors.isEmpty()) {
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
        table.getItems().clear();
        table.getItems().addAll(errors);
    }

    /**
     * Решение конфликта
     *
     * @param actionEvent - событие
     */
    public void solveConflict(ActionEvent actionEvent) {
        var selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (selected.getMessage().equals("* Контакт с таким ФИО уже существует")) {
                alreadyExistsConflict(selected);
            } else {
                Stage editStage = new Stage();

                try {
                    FXMLLoader root = new FXMLLoader(EditViewController.class.getResource("/view/editView.fxml"));
                    HBox rootPane = root.load();

                    ((EditViewController) root.getController()).setContact(new Contact(selected.getContact()));
                    ((EditViewController) root.getController()).setEdit(false);

                    ((EditViewController) root.getController()).initialize();

                    editStage.setScene(new Scene(rootPane));
                    editStage.setTitle("Редактировать контакт");
                    editStage.initModality(Modality.APPLICATION_MODAL);
                    editStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }

            }

            errors.remove(selected);
            showErrors(actionEvent);
        }

    }

    /**
     * Существующий контакат
     *
     * @param selected - выбранный конфликт
     */
    private void alreadyExistsConflict(ValidationError selected) {
        Stage editStage = new Stage();

        try {
            FXMLLoader root = new FXMLLoader(
                    ExistsConflictViewController.class.getResource("/view/existsConflictView.fxml"));
            HBox rootPane = root.load();
            var old = DBManager.getContactByID(selected.getID());
            ((ExistsConflictViewController) root.getController()).setOldContact(old);
            ((ExistsConflictViewController) root.getController()).setFullName(selected.getFullName());
            ((ExistsConflictViewController) root.getController()).setNewContact(new Contact(selected.getContact()));

            ((ExistsConflictViewController) root.getController()).initialize();

            editStage.setResizable(false);
            editStage.setScene(new Scene(rootPane));
            editStage.setTitle("Решение конфликта");
            editStage.initModality(Modality.APPLICATION_MODAL);
            editStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Проигнорировать конфликт
     *
     * @param actionEvent - событие
     */
    public void ignoreConflict(ActionEvent actionEvent) {
        var selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            errors.remove(selected);
            showErrors(actionEvent);
        }
    }

    /**
     * Проигнорировать все конфликты
     *
     * @param actionEvent - событие
     */
    public void ignoreAllConflicts(ActionEvent actionEvent) {
        errors.clear();
        showErrors(actionEvent);
    }
}
