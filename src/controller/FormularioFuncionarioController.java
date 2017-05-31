package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.BCrypt;
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
	@FXML PasswordField txtSenhaFuncionario;
	@FXML PasswordField txtConfSenhaFuncionario;
	@FXML TextField txtTelefoneFuncionario;
	@FXML TextField txtCelularFuncionario;
	@FXML TextField txtEmailFuncionario;
	@FXML TextField txtCredencialFuncionario;
	@FXML TextField txtHostEmpresa;
	@FXML Button btnRemover;
	@FXML Button btnSalvar;

	Funcionario funcionario_selecionado = null;

	Integer idEmpresa;
	int id_funcionario;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		set_status_formulario(false);

		Login.get_id_empresa(new CustomCallable<Integer>() {
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				idEmpresa = (Integer) this.getParametro();

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

				id_funcionario = Context.getIntData("idFuncionario");

				if( id_funcionario != -1 ) {
					funcionario_selecionado = new Funcionario().buscar("id = ?", Arrays.asList( id_funcionario ), Funcionario.class).get(0);

		            txtNomeFuncionario.setText( funcionario_selecionado.getNome() );
		            txtTelefoneFuncionario.setText( funcionario_selecionado.getTelefone() );
		            txtCredencialFuncionario.setText( funcionario_selecionado.getCredencial().split("@")[0] );
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
					
					final int cod_tela_funcionarios = 1;
                    
                    boolean tela_funcionarios = false;
                    
					for( int i = 0; i < telas_permitidas.size(); ++i ) {

						Map<String, Object> tela = telas_permitidas.get(i);
						
						int codigo_tela = ((int) tela.get("cod"));
						
						if( codigo_tela == cod_tela_funcionarios ) {
							tela_funcionarios = true;
							
							if( tela.get("remocao") == null ) {
								btnRemover.setDisable(true);							
							}

							if( tela.get("edicao") == null && id_funcionario != -1 ) {
								btnSalvar.setDisable(true);
							}
							
							if( tela.get("escrita") == null && id_funcionario == -1 ) {
								btnSalvar.setDisable(true);
							}
							
						}
					}

                    if( tela_funcionarios == false ) {
                    	btnRemover.setDisable(true);
                    	btnSalvar.setDisable(true);
                    }
                    
                    
					return super.call();
				}

			});
		}
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

	private void set_agencia(long idAgencia) {
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
        String credencialFuncionario = txtCredencialFuncionario.getText().trim() + '@' + txtHostEmpresa.getText().trim();

        Integer idAgencia = cbAgencia.getSelectionModel().getSelectedItem().getId();
        Integer idNivelAcesso = cbNivelAcesso.getSelectionModel().getSelectedItem().getId();

        Funcionario funcionario = new Funcionario();
        
        if( !senhaFuncionario.isEmpty() ) {
        	if( !senhaFuncionario.equals(confSenhaFuncionario) ) return;
        	
        	String hash = BCrypt.hashpw(senhaFuncionario, BCrypt.gensalt());
        	hash = Login.get_hash_preparada(hash, "2y");
        	funcionario.setSenha(hash);        	
        }                                      		
		
		funcionario.setNome(nomeFuncionario);
		funcionario.setCredencial(credencialFuncionario);
		funcionario.setTelefone(telefoneFuncionario);
		funcionario.setCelular(celularFuncionario);
		funcionario.setEmail(emailFuncionario);
		funcionario.setIdNivelAcesso(idNivelAcesso);
		funcionario.setIdAgencia(idAgencia);
		funcionario.setIdEmpresa(idEmpresa);
		
		ThreadTask<Boolean> thread_salvar = new ThreadTask<Boolean>(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				if( funcionario_selecionado != null )
				{
					funcionario.setId( funcionario_selecionado.getId() );					
					funcionario.setStatusOnline( null );
					return funcionario.atualizar();
				}
				else
				{
					return ( funcionario.inserir() != -1 )? true : false;
				}
			}

		}, new CustomCallable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				WindowManager.fecharJanela( WindowManager.get_stage_de_componente(txtNomeFuncionario) );
				return super.call();
			}

		});

		thread_salvar.run();
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
