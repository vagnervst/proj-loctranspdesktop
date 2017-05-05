package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Usuario;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AlertDialog;
import model.BCrypt;
import model.Context;
import model.CustomCallable;
import model.Login;
import model.ThreadTask;
import view.WindowManager;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

public class LoginLocatarioController implements Initializable {

	@FXML TextField txtEmail;
	@FXML PasswordField txtSenha;
	@FXML VBox vbConteudo;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	@FXML public void executarLogin(ActionEvent event) {
		String email = txtEmail.getText().trim();
		String senha = txtSenha.getText().trim();		
		
		ThreadTask<Usuario> task_login = new ThreadTask<Usuario>(new Callable<Usuario>() {
			
			@Override
			public Usuario call() throws Exception {
				// TODO Auto-generated method stub				
				Usuario buscaUsuarioLocatario = new Usuario();
				List<Usuario> usuario_encontrado = buscaUsuarioLocatario.buscar("email = ?", Arrays.asList( email ), Usuario.class);				
				
				return ( usuario_encontrado.size() == 1 )? usuario_encontrado.get(0) : new Usuario();				
			}
			
		}, new CustomCallable<Usuario>() {
			
			@Override
			public Usuario call() throws Exception {
				// TODO Auto-generated method stub
				
				Usuario usuarioLocatario = (Usuario) getParametro();				
				
				if( usuarioLocatario.getId() != null ) {
					
					String hash_preparada = Login.get_hash_preparada(usuarioLocatario.getSenha(), "2a");
					
					if( BCrypt.checkpw(senha, hash_preparada) ) {
						Context.addData("idUsuarioLocatario", usuarioLocatario.getId());
												
						WindowManager.fecharJanela( WindowManager.get_stage_de_componente(txtEmail) );
					} else {
						AlertDialog.show("Falha", "Houve uma falha ao tentar entrar", "Email ou senha inválidos", AlertType.ERROR);
					}
										
				} else {
					AlertDialog.show("Falha", "Houve uma falha ao tentar entrar", "Email ou senha inválidos", AlertType.ERROR);
				}
				
				return super.call();
			}
			
		});
		
		task_login.run();
	}

}
