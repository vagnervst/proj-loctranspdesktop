package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WindowManager {
	
	public static Stage get_stage_de_componente( Node componente ) {
		return (Stage) componente.getScene().getWindow();
	}
	
	public static void abrirJanela(Stage stage_alvo, String caminho_fxml, Class<?> classe) {		
		try {
			Parent root = FXMLLoader.load( classe.getResource( caminho_fxml ) );
			
			stage_alvo.setScene(new Scene(root));			
			stage_alvo.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Stage abrirModal(String caminho_fxml, Class<?> classe) {
		Stage stage = new Stage();
		try {
			Parent root = FXMLLoader.load( classe.getResource( caminho_fxml ) );
			
			stage.setScene(new Scene(root));
			//stage.initStyle(StageStyle.UTILITY);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stage;
	}
	
	public static void fecharJanela(Stage stage) {
		stage.fireEvent( new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST) );
		stage.close();
	}
}
