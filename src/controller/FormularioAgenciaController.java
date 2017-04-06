package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.Agencia;
import dao.Cidade;
import dao.Estado;
import dao.PerfilNivelAcesso;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class FormularioAgenciaController implements Initializable {

	@FXML
	ComboBox<Estado> cbEstado;
	@FXML
	ComboBox<Cidade> cbCidade;
	@FXML
	ComboBox<PerfilNivelAcesso> cbPerfilNivelAcesso;
	@FXML
	TextField txtTituloAgencia;
	@FXML
	TextField txtTelefone;
	@FXML
	TextField txtEmail;
	@FXML
	TextField txtEndereco;
	@FXML
	Button btnSalvar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		Task<List<Estado>> task_estado = new Task<List<Estado>>() {

			@Override
			protected List<Estado> call() throws Exception {
				// TODO Auto-generated method stub
				return new Estado().buscar(Estado.class);				
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				
				cbEstado.getItems().addAll( getValue() );
			}
		};
		
		Thread thread_estado = new Thread(task_estado);
		thread_estado.start();
		
		Task<List<Cidade>> task_cidade = new Task<List<Cidade>>(){
			
			@Override
			protected List<Cidade> call() throws Exception {
				// TODO Auto-generated method stub
				return new Cidade().buscar(Cidade.class);
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				
				cbCidade.getItems().addAll( getValue() );
			}
		};
		
		Thread thread_cidade = new Thread(task_cidade);
		thread_cidade.start();
		
		Task<List<PerfilNivelAcesso>> task_perfil_nivel = new Task<List<PerfilNivelAcesso>>(){
			
			@Override
			protected List<PerfilNivelAcesso> call() throws Exception {
				// TODO Auto-generated method stub
				return new PerfilNivelAcesso().buscar(PerfilNivelAcesso.class);
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				
				cbPerfilNivelAcesso.getItems().addAll( getValue() );
			}
		};
		
		Thread thread_perfil_nivel = new Thread(task_perfil_nivel);
		thread_perfil_nivel.start();
	}

	@FXML
	public void salvar(ActionEvent event) {
		String titulo_agencia = txtTituloAgencia.getText().trim();
		String telefone_agencia = txtTelefone.getText().trim();
		String email_agencia = txtEmail.getText().trim();
		String endereco_agencia = txtEndereco.getText().trim();
				
		int idCidade = cbCidade.getSelectionModel().getSelectedItem().getId();
		
		int idUsuario = 2;
		Agencia agencia = new Agencia( titulo_agencia, telefone_agencia, email_agencia, endereco_agencia, idCidade, idUsuario );
		
		Task<Boolean> task_inserir_agencia = new Task<Boolean>(){
			
			@Override
			protected Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return agencia.inserir();
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				
				System.out.println( getValue() );
			}
		};
		
		Thread thread_insercao_agencia = new Thread(task_inserir_agencia);
		thread_insercao_agencia.start();
	}

}
