package controller;

import java.net.ConnectException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Empresa;
import dao.Funcionario;
import dao.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import model.AlertDialog;
import model.BCrypt;
import model.CustomCallable;
import model.Login;
import model.ThreadTask;
import view.WindowManager;
import javafx.event.ActionEvent;

public class LoginController implements Initializable {

	@FXML Button btnEntrar;
	@FXML AnchorPane apLogin;
	@FXML TextField txtEmail;
	@FXML PasswordField txtSenha;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub		
	}

	@FXML public void login() {
		String email = txtEmail.getText().trim();
		String senha = txtSenha.getText().trim();

		String host = email.substring( email.indexOf("@")+1, email.length() );

		if( host.indexOf(".") == -1 ) {			
			login_como_funcionario(email, host, senha);
		} else {			
			login_como_juridico(email, senha);
		}
	}

	public Funcionario find_funcionario(String credencial, List<Funcionario> lista_funcionarios) {
		for( Funcionario f : lista_funcionarios ) {
			System.out.println( f.getCredencial() );
			if( f.getCredencial().equals( credencial ) ) return f;
		}

		return null;
	}

	private void login_como_funcionario(String email, String host, String senha) {
		
		ThreadTask<List<Funcionario>> task_login = new ThreadTask<>( new Callable<List<Funcionario>>() {

			@Override
			public List<Funcionario> call() throws Exception {
				// TODO Auto-generated method stub3					
				return new Funcionario().buscar("credencial = ?", Arrays.asList( email ), Funcionario.class);					
			}
			
		}, new CustomCallable<List<Funcionario>>() {
			
			@Override
			public List<Funcionario> call() throws Exception {
				// TODO Auto-generated method stub
				List<Funcionario> funcionarios_encontrados = (List<Funcionario>) getParametro();
				
				if( funcionarios_encontrados != null && funcionarios_encontrados.size() == 1 ) {
					Funcionario funcionario_alvo = funcionarios_encontrados.get(0);
										
					String hash2a = Login.get_hash_preparada( funcionario_alvo.getSenha(), "2a" );
					
					if( BCrypt.checkpw(senha, hash2a) ) {
						Login.set_id_usuario( funcionario_alvo.getId(), Login.FUNCIONARIO );
						abrirHome();
					}
				}
				
				return super.call();
			}
			
		});
		
		task_login.runWithProgress(apLogin);
	}

	private void login_como_juridico(String email, String senha) {

		ThreadTask<List<Usuario>> task_login = new ThreadTask<>( new Callable<List<Usuario>>() {

			@Override
			public List<Usuario> call() throws Exception {
				// TODO Auto-generated method stub
				return new Usuario().buscar("email = ? && idTipoConta = ?", Arrays.asList( email, 2 ), Usuario.class);
			}

		}, new CustomCallable<List<Usuario>>() {

			@Override
			public List<Usuario> call() throws Exception {
				// TODO Auto-generated method stub
				List<Usuario> usuarios_encontrados = (List<Usuario>) getParametro();
				System.out.println( "id: " + usuarios_encontrados.size() );

				if( usuarios_encontrados.size() > 0 ) {
					Usuario usuario_alvo = usuarios_encontrados.get(0);

					String hash2a = Login.get_hash_preparada( usuario_alvo.getSenha(), "2a" );

					if( BCrypt.checkpw(senha, hash2a) ) {
						Login.set_id_usuario( usuario_alvo.getId(), Login.JURIDICO);
						abrirHome();
					}
				}

				return null;
			}
		});

		task_login.runWithProgress(apLogin);
	}

	private void abrirHome() {
		WindowManager.abrirJanela( WindowManager.get_stage_de_componente(txtEmail), "/view/Home.fxml", getClass());
	}

	@FXML public void abrirConfiguracoes(ActionEvent event) {
		WindowManager.abrirJanela( WindowManager.get_stage_de_componente(txtEmail), "/view/Configuracoes.fxml", getClass());
	}

}
