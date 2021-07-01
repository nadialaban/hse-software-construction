package nlaban.hw6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/mainView.fxml"));
		primaryStage.setTitle("Телефонная книга");
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(200);
		primaryStage.setScene(new Scene(root, 800, 500));
		primaryStage.show();
	}
}
