package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.AlertDialog;
import model.CustomCallable;
import model.Server;
import model.ThreadTask;
import view.WindowManager;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

public class FormularioConfiguracoes implements Initializable {

	@FXML TextField txtEnderecoSite;
	@FXML TextField txtHostBanco;
	@FXML TextField txtUsuarioBanco;
	@FXML PasswordField txtSenhaBanco;
	@FXML TextField txtNomeBanco;
	@FXML TextField txtPortaWeb;
	@FXML TextField txtPortaBanco;
	@FXML AnchorPane apConteudo;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		txtEnderecoSite.setText( Server.address );
		txtHostBanco.setText( Server.host );
		txtUsuarioBanco.setText( Server.usuario );
		txtSenhaBanco.setText( Server.senha );
		txtNomeBanco.setText( Server.banco );		
		txtPortaBanco.setText( Server.portaBanco );
		txtPortaWeb.setText( "80" );
	}

	@FXML public void testarConexao(ActionEvent event) {
		
		ThreadTask<List<String>> task_conexoes = new ThreadTask<List<String>>( new Callable<List<String>>() {

			@Override
			public List<String> call() throws Exception {
				// TODO Auto-generated method stub
				String enderecoSite = txtEnderecoSite.getText().toString().trim();
				int portaWeb = Integer.valueOf( txtPortaWeb.getText().toString().trim() );
				
				List<String> mensagens = new ArrayList<>();				
					
				enderecoSite = enderecoSite.replace("/", "");
																	
				try {
					Socket s = new Socket( enderecoSite, portaWeb );
					mensagens.add("Conexão com o servidor Web bem sucedida.");			
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					mensagens.add("Conexão com o servidor Web falha.");			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					mensagens.add("Conexão com o servidor Web falha.");
				}						
				
				String hostBanco = txtHostBanco.getText().toString().trim();
				String nomeBanco = txtNomeBanco.getText().toString().trim();
				String senhaBanco = txtSenhaBanco.getText().toString().trim();
				String usuarioBanco = txtUsuarioBanco.getText().toString().trim();
				String portaBancoDados = txtPortaBanco.getText().toString().trim();
				
				String string_conexao = "jdbc:mysql://" + hostBanco + ":" + portaBancoDados + "/" + nomeBanco + "?user=" + usuarioBanco + "&password=" + senhaBanco;
				
				try {			
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					
					DriverManager.registerDriver( new com.mysql.jdbc.Driver() );									
					java.sql.Connection conexao = DriverManager.getConnection( string_conexao );
					
					mensagens.add("Conexão com o servidor de Banco de Dados bem sucedida.");										
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					mensagens.add("Conexão com o servidor de Banco de Dados falha.");
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					mensagens.add("Conexão com o servidor de Banco de Dados falha.");
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					mensagens.add("Conexão com o servidor de Banco de Dados falha.");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					mensagens.add("Conexão com o servidor de Banco de Dados falha.");
				}
				
				return mensagens;
			}
			
		}, new CustomCallable<List<String>>() {
			
			@Override
			public List<String> call() throws Exception {
				// TODO Auto-generated method stub
				List<String> mensagens = (List<String>) getParametro();
				
				String mensagem = "";
				for( int i = 0; i < mensagens.size(); ++i ) {
					mensagem += mensagens.get(i) + System.lineSeparator();
				}
				
				AlertDialog.show("Resultado", "Resultado das tentativas de conexão", mensagem, AlertType.INFORMATION);
				
				return super.call();
			}
			
		});	
		
		task_conexoes.runWithProgress(apConteudo);
	}

	@FXML public void cancelar(ActionEvent event) {
		WindowManager.abrirJanela(WindowManager.get_stage_de_componente(apConteudo), "/view/Login.fxml", getClass());
	}

	@FXML public void salvar(ActionEvent event) {
		String enderecoSite = txtEnderecoSite.getText().toString().trim();
		
		String hostBanco = txtHostBanco.getText().toString().trim();
		String nomeBanco = txtNomeBanco.getText().toString().trim();
		String senhaBanco = txtSenhaBanco.getText().toString().trim();
		String usuarioBanco = txtUsuarioBanco.getText().toString().trim();
		String portaBancoDados = txtPortaBanco.getText().toString().trim();
		
		Server.address = enderecoSite;
		Server.host = hostBanco;
		Server.portaBanco = portaBancoDados;
		Server.usuario = usuarioBanco;
		Server.senha = senhaBanco;		
		Server.banco = nomeBanco;
		
		WindowManager.abrirJanela(WindowManager.get_stage_de_componente(apConteudo), "/view/Login.fxml", getClass());
	}

}
