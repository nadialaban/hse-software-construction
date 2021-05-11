package nlaban.hw6.phonebook.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AboutViewController {

	public ImageView img;

	public void close(ActionEvent actionEvent) {
		if (!img.isVisible()) {
			img.setVisible(true);
			((Button) actionEvent.getSource()).setText("Закрыть :)");
		} else {
			Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
			stage.close();
		}
	}

}
