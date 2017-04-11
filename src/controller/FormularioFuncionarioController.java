package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import dao.Agencia;
import dao.Funcionario;
import dao.NivelAcessoJuridico;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.ComboBoxUtils;
import model.Context;
import view.WindowManager;

public class FormularioFuncionarioController implements Initializable {

	@FXML TextField txtNomeFuncionario;
	@FXML TextField txtSenhaFuncionario;
	@FXML TextField txtConfSenhaFuncionario;
	@FXML TextField txtTelefoneFuncionario;
	@FXML TextField txtCelularFuncionario;
	@FXML TextField txtEmailFuncionario;
	@FXML ComboBox<Agencia> cbAgencia;
	@FXML ComboBox<NivelAcessoJuridico> cbNivelAcesso;
	@FXML Button btnRemover;

	Funcionario funcionario_selecionado = null;

	int idUsuario = 2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		ComboBoxUtils.popular_combobox(cbAgencia, "idUsuario = ?", Arrays.asList( idUsuario ), new Agencia());
		cbNivelAcesso.setDisable(true);

		cbAgencia.setOnAction( new filtrar_nivel_acesso());

		int id_funcionario = Context.getIntData("idFuncionario");

		if( id_funcionario != -1 ) {
			funcionario_selecionado = new Funcionario().buscar("id = ?", Arrays.asList( id_funcionario ), Funcionario.class).get(0);

            txtNomeFuncionario.setText( funcionario_selecionado.getNome() );
            txtTelefoneFuncionario.setText( funcionario_selecionado.getTelefone() );
            txtCelularFuncionario.setText( funcionario_selecionado.getCelular() );
            txtEmailFuncionario.setText( funcionario_selecionado.getEmail() );

			set_agencia( funcionario_selecionado.getIdAgencia() );
		}

		if( funcionario_selecionado == null ) {
			btnRemover.setVisible(false);
		}
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
				int id_perfil_nivel_acesso = getValue().getIdPerfilNivelAcesso();

				cbNivelAcesso.getSelectionModel().select( new NivelAcessoJuridico().buscar("id = ?",
						Arrays.asList( funcionario_selecionado.getIdNivelAcesso() ),
						NivelAcessoJuridico.class).get(0) );
			}
		};

		Thread thread = new Thread( task_set_agencia );
		thread.start();
	}

	private class filtrar_nivel_acesso implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			int id_perfil_nivel_acesso = cbAgencia.getSelectionModel().getSelectedItem().getIdPerfilNivelAcesso();

			ComboBoxUtils.popular_combobox(cbNivelAcesso, "idPerfilNivelAcesso = ?", Arrays.asList( id_perfil_nivel_acesso ), new NivelAcessoJuridico());
			cbNivelAcesso.setDisable(false);
		}

	}

	@FXML public void salvar(ActionEvent event) {
        String nomeFuncionario = txtNomeFuncionario.getText().trim();
        String senhaFuncionario = txtSenhaFuncionario.getText().trim();
        String confSenhaFuncionario = txtConfSenhaFuncionario.getText().trim();
        String telefoneFuncionario = txtTelefoneFuncionario.getText().trim();
        String celularFuncionario = txtCelularFuncionario.getText().trim();
        String emailFuncionario = txtEmailFuncionario.getText().trim();

        int idAgencia = cbAgencia.getSelectionModel().getSelectedItem().getId();
        int idNivelAcesso = cbNivelAcesso.getSelectionModel().getSelectedItem().getId();

        if( !senhaFuncionario.equals(confSenhaFuncionario) ) return;

		Funcionario funcionario = new Funcionario(nomeFuncionario, senhaFuncionario, telefoneFuncionario, celularFuncionario, emailFuncionario, idNivelAcesso, idAgencia);

		Task<Boolean> task_salvar = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {
				// TODO Auto-generated method stub
				if( funcionario_selecionado != null ) {
					funcionario.setId( funcionario_selecionado.getId() );
					return funcionario.atualizar();
				} else {
					return funcionario.inserir();
				}
			}

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

			protected void succeeded() {
				WindowManager.fecharJanela( WindowManager.get_stage_de_componente( btnRemover ) );
			};
		};

		Thread thread_remover = new Thread( task_remover );
		thread_remover.start();
	}

}
