package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import view.WindowManager;

public class EmpresaController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@FXML public void abrirAgencias(ActionEvent event) {
		WindowManager.abrirModal("/view/Agencias.fxml", getClass());
	}

	@FXML public void abrirFuncionarios(ActionEvent event) {
		WindowManager.abrirModal("/view/Funcionarios.fxml", getClass());
	}

	@FXML public void abrirPerfisNivelAcesso(ActionEvent event) {
		WindowManager.abrirModal("/view/NiveisAcesso.fxml", getClass());
	}
	
	@FXML public void abrirEstatisticasPublicacao(ActionEvent event) {
		WindowManager.abrirModal("/view/RelatorioPublicacao.fxml", getClass());
	}

	@FXML public void abrirEstatisticasLocacao(ActionEvent event) {
		WindowManager.abrirModal("/view/RelatorioLocacoes.fxml", getClass());
	}

}