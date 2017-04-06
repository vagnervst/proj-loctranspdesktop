package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import view.WindowManager;

public class HomeController implements Initializable {

	@FXML
	Button btnPublicacoes;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	@FXML
	public void abrirPublicacoes(ActionEvent event) {
		WindowManager wm = new WindowManager();
		wm.abrirModal("/view/Publicacoes.fxml");
	}

	@FXML
	public void abrirGerencia(ActionEvent event) {
		WindowManager wm = new WindowManager();
		wm.abrirModal("/view/Gerencia.fxml");
	}

}
