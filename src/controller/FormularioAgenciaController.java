package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import dao.Agencia;
import dao.Cidade;
import dao.Estado;
import dao.Funcionario;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.ComboBoxUtils;
import model.Context;
import model.CustomCallable;
import model.Login;
import view.WindowManager;

public class FormularioAgenciaController implements Initializable {

	@FXML
	ComboBox<Estado> cbEstado;
	@FXML
	ComboBox<Cidade> cbCidade;
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
	@FXML
	Button btnRemover;

	Integer idEmpresa;
	Agencia agencia_selecionada = null;
	
	Integer id_agencia;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		set_status_formulario(false);
		Login.get_id_empresa(new CustomCallable<List<Map>>(){
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				set_status_formulario(true);
				idEmpresa = (Integer) this.getParametro();

				ComboBoxUtils.popular_combobox(cbEstado, new Estado());
				ComboBoxUtils.popular_combobox(cbCidade, new Cidade());

				id_agencia = Context.getIntData("idAgencia");

				if( id_agencia != null && id_agencia != -1 ) {
					agencia_selecionada = new Agencia().buscar("id = ?", Arrays.asList( id_agencia ), Agencia.class).get(0);

					txtTituloAgencia.setText( agencia_selecionada.getTitulo() );
					txtTelefone.setText( agencia_selecionada.getTelefone() );
					txtEmail.setText( agencia_selecionada.getEmail() );
					txtEndereco.setText( agencia_selecionada.getEndereco() );

					set_cidade( agencia_selecionada.getIdCidade() );
				}

				if( agencia_selecionada == null ) {
					btnRemover.setVisible(false);
				}

				return super.call();
			}
		});
		
		controle_permissoes();
	}
	
	public void controle_permissoes() {
		if( Login.get_tipo_conta() == Login.FUNCIONARIO ) {
			Funcionario funcionario = new Funcionario();
			funcionario.setId( Login.get_id_usuario() );

			funcionario.getPermissoes( new CustomCallable<List<Map>>() {

				@Override
				public List<Map> call() throws Exception {
					// TODO Auto-generated method stub
					List<Map> telas_permitidas = (List<Map>) getParametro();
					
					final int cod_tela_agencias = 3;
                    
                    boolean tela_agencias = false;
                    
					for( int i = 0; i < telas_permitidas.size(); ++i ) {

						Map<String, Object> tela = telas_permitidas.get(i);
						
						int codigo_tela = ((int) tela.get("cod"));
						
						if( codigo_tela == cod_tela_agencias ) {
							tela_agencias = true;
							
							if( tela.get("remocao") == null ) {
								btnRemover.setDisable(true);							
							}

							if( tela.get("edicao") == null && id_agencia != -1 ) {
								btnSalvar.setDisable(true);
							}
							
							if( tela.get("escrita") == null && id_agencia == -1 ) {
								btnSalvar.setDisable(true);
							}
							
						}
					}

                    if( tela_agencias == false ) {
                    	btnRemover.setDisable(true);
                    	btnSalvar.setDisable(true);
                    }                    
                    
					return super.call();
				}

			});
		}
	}
	
	private void set_status_formulario(boolean status) {
		cbEstado.setDisable(!status);
		cbCidade.setDisable(!status);
		txtTituloAgencia.setDisable(!status);
		txtEmail.setDisable(!status);
		txtTelefone.setDisable(!status);
		txtEndereco.setDisable(!status);
	}

	private void set_estado(Integer id_estado) {
		Task<Estado> task_estado_agencia = new Task<Estado>() {

			@Override
			protected Estado call() throws Exception {
				// TODO Auto-generated method stub
				return new Estado().buscar("id = ?", Arrays.asList( id_estado ), Estado.class).get(0);
			}

			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				cbEstado.getSelectionModel().select( getValue() );
			}

		};

		Thread thread_estado_agencia = new Thread( task_estado_agencia );
		thread_estado_agencia.start();
	}

	private void set_cidade(Integer id_cidade) {
		Task<Cidade> task_cidade_agencia = new Task<Cidade>() {

			@Override
			protected Cidade call() throws Exception {
				// TODO Auto-generated method stub
				return new Cidade().buscar("id = ?", Arrays.asList( id_cidade ), Cidade.class).get(0);
			}

			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				cbCidade.getSelectionModel().select( getValue() );
				set_estado( getValue().getIdEstado() );
			}
		};

		Thread thread_cidade_agencia = new Thread( task_cidade_agencia );
		thread_cidade_agencia.start();
	}

	@FXML
	public void salvar(ActionEvent event) {
		String titulo_agencia = txtTituloAgencia.getText().trim();
		String telefone_agencia = txtTelefone.getText().trim();
		String email_agencia = txtEmail.getText().trim();
		String endereco_agencia = txtEndereco.getText().trim();

		Integer idCidade = cbCidade.getSelectionModel().getSelectedItem().getId();
		System.out.println( "id usuairo: " + idEmpresa );
		Agencia agencia = new Agencia( titulo_agencia, telefone_agencia, email_agencia, endereco_agencia, idCidade, idEmpresa );

		Task<Boolean> task_inserir_agencia = new Task<Boolean>(){

			@Override
			protected Boolean call() throws Exception {
				// TODO Auto-generated method stub
				if( agencia_selecionada != null ) {
					agencia.setId( agencia_selecionada.getId() );
					return agencia.atualizar();
				} else {
					return ( agencia.inserir() != -1 )? true : false;
				}
			}

			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				WindowManager.fecharJanela( WindowManager.get_stage_de_componente( btnSalvar ) );
			}
		};

		Thread thread_insercao_agencia = new Thread(task_inserir_agencia);
		thread_insercao_agencia.start();
	}

	@FXML public void remover(ActionEvent event) {
		Task<Boolean> remover_agencia = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return agencia_selecionada.remover();
			}

			@Override
			protected void succeeded() {
				WindowManager.fecharJanela( WindowManager.get_stage_de_componente( btnSalvar ) );
			};
		};

		Thread thread_remover = new Thread( remover_agencia );
		thread_remover.start();
	}

	@FXML public void cancelar(ActionEvent event) {
		WindowManager.fecharJanela( WindowManager.get_stage_de_componente( btnSalvar ) );
	}
}
