package view;
	
import java.io.IOException;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Teste;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		Teste t = new Teste();
		t.id = 1;
		t.titulo = "Vagner S";
				
		t.inserir();
		
		WindowManager wm = new WindowManager();
		wm.abrirJanela(primaryStage, "/view/Login.fxml");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
