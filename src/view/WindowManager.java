package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowManager {
	
	public Stage get_stage_de_componente( Node componente ) {
		return (Stage) componente.getScene().getWindow();
	}
	
	public void abrirJanela(Stage stage_alvo, String caminho_fxml) {		
		try {
			Parent root = FXMLLoader.load( getClass().getResource( caminho_fxml ) );
			
			stage_alvo.setScene(new Scene(root));			
			stage_alvo.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void abrirModal(String caminho_fxml) {
		Stage stage = new Stage();
		try {
			Parent root = FXMLLoader.load( getClass().getResource( caminho_fxml ) );
			
			stage.setScene(new Scene(root));
			stage.initStyle(StageStyle.UTILITY);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
