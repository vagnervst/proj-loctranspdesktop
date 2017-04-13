package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import dao.Empresa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Login;
import view.WindowManager;

public class HomeController implements Initializable {

	@FXML
	Button btnPublicacoes;
	@FXML Label lblNomeEmpresa;
	@FXML Label lblNomeFuncionario;
	@FXML Label lblNivelAcesso;
	@FXML Label lblAgencia;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		Empresa empresa_alvo = new Empresa();
		empresa_alvo = empresa_alvo.buscar("idUsuarioJuridico = ?", Arrays.asList( Login.get_id_usuario() ), Empresa.class).get(0);

		if( Login.get_tipo_conta() == Login.JURIDICO ) {
			lblNomeFuncionario.setVisible(false);
		}

		lblNomeEmpresa.setText( empresa_alvo.getNomeFantasia() );
	}

	@FXML
	public void abrirPublicacoes(ActionEvent event) {
		WindowManager.abrirModal("/view/Publicacoes.fxml", getClass());
	}

	@FXML
	public void abrirGerencia(ActionEvent event) {
		WindowManager.abrirModal("/view/Gerencia.fxml", getClass());
	}
}
