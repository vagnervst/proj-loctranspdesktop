package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import dao.Empresa;
import dao.Funcionario;
import dao.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.BCrypt;
import view.WindowManager;

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
			if( f.getCredencial().equals( credencial ) ) return f;
		}
		
		return null;
	}
	
	private void login_como_funcionario(String email, String host, String senha) {
		List<Empresa> busca_empresa = new Empresa().buscar("nomeHost = ?", Arrays.asList( host ), Empresa.class);
		
		if( busca_empresa.size() > 0 ) {
			Empresa empresa_alvo = busca_empresa.get(0);
			
			List<Funcionario> funcionarios_da_empresa = new Funcionario().getFuncionarios( empresa_alvo.getId() );
						
			if( funcionarios_da_empresa.size() > 0 ) {
				Funcionario funcionario_alvo = find_funcionario(email, funcionarios_da_empresa);
				
				System.out.println( funcionario_alvo );
				if( funcionario_alvo == null ) return;
							
				if( BCrypt.checkpw(senha, funcionario_alvo.getSenha()) ) {
					System.out.println( "senha correta" );
					abrirHome();
				}
			}
		}
	}
	
	private void login_como_juridico(String email, String senha) {
		List<Usuario> busca_usuario = new Usuario().buscar("email = ?", Arrays.asList( email ), Usuario.class);
		
		if( busca_usuario.size() > 0 ) {
			Usuario usuario_alvo = busca_usuario.get(0);
			
			if( BCrypt.checkpw(senha, usuario_alvo.getSenha()) ) {
				abrirHome();
			}
		}
	}
	
	private void abrirHome() {
		WindowManager.abrirJanela( WindowManager.get_stage_de_componente(txtEmail), "/view/Home.fxml", getClass());
	}
	
}
