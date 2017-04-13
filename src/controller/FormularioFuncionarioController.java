package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Agencia;
import dao.Empresa;
import dao.Funcionario;
import dao.NivelAcessoJuridico;
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
import model.ThreadTask;
import view.WindowManager;

public class FormularioFuncionarioController implements Initializable {

	@FXML ComboBox<Agencia> cbAgencia;
	@FXML ComboBox<NivelAcessoJuridico> cbNivelAcesso;
	@FXML TextField txtNomeFuncionario;
	@FXML TextField txtSenhaFuncionario;
	@FXML TextField txtConfSenhaFuncionario;
	@FXML TextField txtTelefoneFuncionario;
	@FXML TextField txtCelularFuncionario;
	@FXML TextField txtEmailFuncionario;
	@FXML TextField txtCredencialFuncionario;
	@FXML TextField txtHostEmpresa;	
	@FXML Button btnRemover;
	@FXML Button btnSalvar;	
	
	Funcionario funcionario_selecionado = null;

	int idEmpresa;		

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		set_status_formulario(false);
		Login.get_id_empresa(new CustomCallable<Integer>() {
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub						
				idEmpresa = (int) this.getParametro();										
				
				ThreadTask<Empresa> info_empresa = new ThreadTask<Empresa>(new Callable<Empresa>() {

					@Override
					public Empresa call() throws Exception {
						// TODO Auto-generated method stub
						return new Empresa().buscar("id = ?", Arrays.asList( idEmpresa ), Empresa.class).get(0);
					}
					
				}, new CustomCallable<Empresa>() {
					
					@Override
					public Empresa call() throws Exception {
						// TODO Auto-generated method stub						
						Empresa objEmpresa = (Empresa) getParametro();
						txtHostEmpresa.setText( objEmpresa.getNomeHost() );
						txtHostEmpresa.setDisable( true );
						
						return super.call();
					}
					
				});
				
				info_empresa.run();
				
				ComboBoxUtils.popular_combobox(cbAgencia, "idEmpresa = ?", Arrays.asList( idEmpresa ), new Agencia());			
				Login.get_id_juridico(new CustomCallable<Integer>() {
					@Override
					public Integer call() throws Exception {
						// TODO Auto-generated method stub
						set_status_formulario(true);
						int idUsuario = (int) getParametro();
						
						ComboBoxUtils.popular_combobox(cbNivelAcesso, "idUsuario = ?", Arrays.asList( idUsuario ), new NivelAcessoJuridico());
						
						return super.call();
					}
				});
						
				int id_funcionario = Context.getIntData("idFuncionario");
		
				if( id_funcionario != -1 ) {
					funcionario_selecionado = new Funcionario().buscar("id = ?", Arrays.asList( id_funcionario ), Funcionario.class).get(0);
		
		            txtNomeFuncionario.setText( funcionario_selecionado.getNome() );
		            txtTelefoneFuncionario.setText( funcionario_selecionado.getTelefone() );
		            txtCredencialFuncionario.setText( funcionario_selecionado.getCredencial() );
		            txtCelularFuncionario.setText( funcionario_selecionado.getCelular() );
		            txtEmailFuncionario.setText( funcionario_selecionado.getEmail() );
		            
					set_agencia( funcionario_selecionado.getIdAgencia() );
				}
		
				if( funcionario_selecionado == null ) {
					btnRemover.setVisible(false);
				}
								
				return super.call();
			}
		});
	}
	
	private void set_status_formulario(boolean status) {
        txtNomeFuncionario.setDisable( !status );
        txtSenhaFuncionario.setDisable( !status );
        txtConfSenhaFuncionario.setDisable( !status );
        txtTelefoneFuncionario.setDisable( !status );
        txtCelularFuncionario.setDisable( !status );
        txtEmailFuncionario.setDisable( !status );
        cbAgencia.setDisable( !status );
        cbNivelAcesso.setDisable( !status );
        btnSalvar.setDisable( !status );
        btnRemover.setDisable( !status );
	}
	
	private void set_agencia(int idAgencia) {
		Task<Agencia> task_set_agencia = new Task<Agencia>() {

			@Override
			protected Agencia call() throws Exception {
				// TODO Auto-generated method stub
				return new Agencia().buscar("id = ?", Arrays.asList( idAgencia ), Agencia.class).get(0);
			}

			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				cbAgencia.getSelectionModel().select( getValue() );				

				cbNivelAcesso.getSelectionModel().select( new NivelAcessoJuridico().buscar("id = ?",
						Arrays.asList( funcionario_selecionado.getIdNivelAcesso() ),
						NivelAcessoJuridico.class).get(0) );
			}
		};

		Thread thread = new Thread( task_set_agencia );
		thread.start();
	}

	@FXML public void salvar(ActionEvent event) {
        String nomeFuncionario = txtNomeFuncionario.getText().trim();
        String senhaFuncionario = txtSenhaFuncionario.getText().trim();
        String confSenhaFuncionario = txtConfSenhaFuncionario.getText().trim();
        String telefoneFuncionario = txtTelefoneFuncionario.getText().trim();
        String celularFuncionario = txtCelularFuncionario.getText().trim();
        String emailFuncionario = txtEmailFuncionario.getText().trim();
        String credencialFuncionario = txtCredencialFuncionario.getText().trim();
        
        int idAgencia = cbAgencia.getSelectionModel().getSelectedItem().getId();
        int idNivelAcesso = cbNivelAcesso.getSelectionModel().getSelectedItem().getId();

        if( !senhaFuncionario.equals(confSenhaFuncionario) ) return;

		Funcionario funcionario = new Funcionario(nomeFuncionario, senhaFuncionario, credencialFuncionario, 
				telefoneFuncionario, celularFuncionario, emailFuncionario, idNivelAcesso, idAgencia, idEmpresa);

		Task<Boolean> task_salvar = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {
				// TODO Auto-generated method stub
				if( funcionario_selecionado != null ) {
					funcionario.setId( funcionario_selecionado.getId() );
					return funcionario.atualizar();
				} else {
					return ( funcionario.inserir() != -1 )? true : false;
				}
			}

			@Override
			protected void succeeded() {
				WindowManager.fecharJanela( WindowManager.get_stage_de_componente(txtNomeFuncionario) );
			};
		};

		Thread thread_salvar = new Thread( task_salvar );
		thread_salvar.start();
	}

	@FXML public void cancelar(ActionEvent event) {
		WindowManager.fecharJanela( WindowManager.get_stage_de_componente(txtNomeFuncionario) );
	}

	@FXML public void remover(ActionEvent event) {
		Task<Boolean> task_remover = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return funcionario_selecionado.remover();
			}

			@Override
			protected void succeeded() {
				WindowManager.fecharJanela( WindowManager.get_stage_de_componente( btnRemover ) );
			};
		};

		Thread thread_remover = new Thread( task_remover );
		thread_remover.start();
	}

}
