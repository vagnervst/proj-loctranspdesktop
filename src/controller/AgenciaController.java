package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import view.WindowManager;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class AgenciaController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

	@FXML public void abrirCadastrar(ActionEvent event) {
		WindowManager wm = new WindowManager();
		wm.abrirModal("/view/FormularioAgencia.fxml");
	}
}
