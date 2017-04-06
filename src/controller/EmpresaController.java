package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import view.WindowManager;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class EmpresaController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@FXML public void abrirAgencias(ActionEvent event) {
		WindowManager wm = new WindowManager();
		wm.abrirModal("/view/Agencias.fxml");
	}    
         
	@FXML public void abrirFuncionarios(ActionEvent event) {
		WindowManager wm = new WindowManager();
		wm.abrirModal("/view/Funcionarios.fxml"); 
	}

}