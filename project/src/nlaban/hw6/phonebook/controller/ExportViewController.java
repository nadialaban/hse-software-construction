package nlaban.hw6.phonebook.controller;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nlaban.hw6.phonebook.model.PhoneBook;

public class ExportViewController {

	public Label errorLabel;
	public TextField path;

	public void exportContacts(ActionEvent actionEvent) {
		try {
			PhoneBook.exportContacts(path.getText(), false);
			Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
			stage.close();
		} catch (IOException e) {
			errorLabel.setVisible(true);
		}
	}

	public void open(ActionEvent actionEvent) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Импорт контактов");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)",
				"*.csv");
		fc.getExtensionFilters().add(extFilter);
		File f = fc.showSaveDialog(((Button) actionEvent.getSource()).getScene().getWindow());
		path.setText(f != null ? f.getPath() : "");
	}
}
