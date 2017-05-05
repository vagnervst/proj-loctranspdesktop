package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Empresa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.CustomCallable;
import model.Login;
import model.ThreadTask;
import view.WindowManager;

public class HomeController implements Initializable {

	@FXML
	Button btnPublicacoes;
	@FXML Label lblNomeEmpresa;
	@FXML Label lblNomeFuncionario;
	@FXML Label lblNivelAcesso;
	@FXML Label lblAgencia;

	Empresa empresa_alvo;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		ThreadTask<List<Empresa>> task_empresa = new ThreadTask<List<Empresa>>(new Callable<List<Empresa>>() {
			
			@Override
			public List<Empresa> call() throws Exception {
				// TODO Auto-generated method stub
				return new Empresa().buscar("idUsuarioJuridico = ?", Arrays.asList( Login.get_id_usuario() ), Empresa.class);
			}
			
		}, new CustomCallable<List<Empresa>>() {
			
			@Override
			public List<Empresa> call() throws Exception {
				// TODO Auto-generated method stub
				if( ((List<Empresa>) this.getParametro()).get(0) != null ) {
					empresa_alvo = (Empresa) ((List<Empresa>) this.getParametro()).get(0);
					
					lblNomeEmpresa.setText( empresa_alvo.getNomeFantasia() );
				}
				return super.call();
			}
			
		});					

		if( Login.get_tipo_conta() == Login.JURIDICO ) {
			lblNomeFuncionario.setVisible(false);
		}
		
	}

	@FXML
	public void abrirPublicacoes(ActionEvent event) {
		WindowManager.abrirModal("/view/Publicacoes.fxml", getClass());
	}

	@FXML
	public void abrirGerencia(ActionEvent event) {
		WindowManager.abrirModal("/view/Gerencia.fxml", getClass());
	}

	@FXML public void abrirSolicitacoes(ActionEvent event) {
		WindowManager.abrirModal("/view/Solicitacoes.fxml", getClass());
	}
}
