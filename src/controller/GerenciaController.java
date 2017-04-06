package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import view.WindowManager;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class GerenciaController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML public void abrirEmpresa(ActionEvent event) {
		WindowManager wm = new WindowManager();
		wm.abrirModal("/view/Empresa.fxml");
	}
}
