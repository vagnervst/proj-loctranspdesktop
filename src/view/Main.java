package view;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {				
		WindowManager.abrirJanela(primaryStage, "/view/Login.fxml", getClass());
	}

	public static void main(String[] args) {
		launch(args);
	}
}
