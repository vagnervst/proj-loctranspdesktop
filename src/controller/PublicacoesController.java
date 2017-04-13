package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import view.WindowManager;

public class PublicacoesController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	@FXML
	public void abrirCadastroPublicacao(ActionEvent event) {
		WindowManager.abrirModal("/view/FormularioPublicacao.fxml", getClass());
	}

}
