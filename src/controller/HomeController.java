package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.WindowManager;
import javafx.event.ActionEvent;

public class HomeController implements Initializable {

	@FXML Button btnPublicacoes;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@FXML public void abrirPublicacoes(ActionEvent event) {
		WindowManager wm = new WindowManager();
		wm.abrirModal("/view/Publicacoes.fxml");
	}

}
