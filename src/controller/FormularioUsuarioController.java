package controller;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.CartaoCredito;
import dao.Cidade;
import dao.Estado;
import dao.TipoCartaoCredito;
import dao.Usuario;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import model.AlertDialog;
import model.ComboBoxUtils;
import model.CustomCallable;
import model.ThreadTask;
import view.WindowManager;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;

public class FormularioUsuarioController implements Initializable {

	@FXML TextField txtNome;
	@FXML TextField txtSobrenome;
	@FXML DatePicker dtNascimento;
	@FXML ComboBox<Estado> cbEstado;
	@FXML ComboBox<Cidade> cbCidade;
	@FXML TextField txtRg;
	@FXML TextField txtCpf;
	@FXML ToggleGroup groupSexo;
	@FXML RadioButton rdFeminino;
	@FXML RadioButton rdMasculino;
	@FXML TextField txtTelefone;
	@FXML TextField txtCelular;
	@FXML TextField txtEmail;
	@FXML ComboBox<TipoCartaoCredito> cbTipoCartao;
	@FXML TextField txtNumeroCartao;
	@FXML DatePicker dtValidadeCartao;
	@FXML PasswordField txtSenha;
	@FXML PasswordField txtConfSenha;
	@FXML AnchorPane apConteudo;

	final int id_tipo_conta_fisico = 1;
	final int id_plano_conta_basico = 1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		cbCidade.setDisable(true);
		ComboBoxUtils.popular_combobox( cbEstado, new Estado() );
		ComboBoxUtils.popular_combobox( cbTipoCartao, new TipoCartaoCredito() );
		
		cbEstado.setOnAction( new AcaoComboBoxEstado() );
	}

	@FXML public void cancelar(ActionEvent event) {}

	@FXML public void salvar(ActionEvent event) {
		Usuario usuario = new Usuario();
		usuario.setNome( txtNome.getText().trim() );
		usuario.setSobrenome( txtSobrenome.getText().trim() );		
		usuario.setEmail( txtEmail.getText().trim() );
		usuario.setCelular( txtCelular.getText().trim() );
		usuario.setTelefone( txtTelefone.getText().trim() );
		usuario.setRG( txtRg.getText().trim() );
		usuario.setCPF( txtCpf.getText().trim() );
		usuario.setIdCidade( cbCidade.getSelectionModel().getSelectedItem().getId() );		
		usuario.setDataNascimento( Date.valueOf( dtNascimento.getValue() ) );
		usuario.setIdTipoConta( id_tipo_conta_fisico );
		usuario.setIdPlanoConta( id_plano_conta_basico );
		
		String sexo = ( groupSexo.getSelectedToggle()  == rdMasculino )? "m" : "f";
		usuario.setSexo( sexo );
				
		ThreadTask<Integer> task_inserir_cartao = new ThreadTask<Integer>( new Callable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				CartaoCredito cartao_credito = new CartaoCredito();
				
				int idTipoCartao = cbTipoCartao.getSelectionModel().getSelectedItem().getId();
				cartao_credito.setIdTipo(idTipoCartao);
				
				cartao_credito.setNumero( txtNumeroCartao.getText().trim() );
				
				Timestamp vencimento_cartao =  Timestamp.valueOf( dtValidadeCartao.getValue().atStartOfDay() );
				cartao_credito.setVencimento(vencimento_cartao);
				
				cartao_credito.setIdUsuario( usuario.getId() );
				return cartao_credito.inserir();
			}
			
		}, new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				int id_cartao_credito = (int) getParametro();
				
				if( id_cartao_credito == -1 ) {
					AlertDialog.show("Erro", "Ocorreu um erro", "Houve um erro ao tentar cadastrar o usuário", AlertType.ERROR);
				} else {
					AlertDialog.show("Sucesso", "Operação bem sucedida", "Usuário cadastrado com sucesso", AlertType.INFORMATION);
					WindowManager.fecharJanela( WindowManager.get_stage_de_componente( apConteudo ) );
				}
								
				return super.call();
			}
			
		});
		
		ThreadTask<Integer> task_inserir_usuario = new ThreadTask<Integer>( new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				return usuario.inserir();
			}
			
		}, new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				int idUsuarioInserido = (int) getParametro();
				
				if( idUsuarioInserido == -1 ) {
					AlertDialog.show("Erro", "Ocorreu um erro", "Houve um erro na inserção do usuário", AlertType.ERROR);					
				} else {
				
					usuario.setId( idUsuarioInserido );
									
					task_inserir_cartao.runWithProgress(apConteudo);
				}
				
				return super.call();
			}
			
		});
		
		task_inserir_usuario.runWithProgress(apConteudo);
	}

	private class AcaoComboBoxEstado implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			int id_estado_selecionado = cbEstado.getSelectionModel().getSelectedItem().id;
			
			ComboBoxUtils.popular_combobox( cbCidade, "idEstado = ?", Arrays.asList( id_estado_selecionado ), new Cidade() );
			cbCidade.setDisable(false);
		}
		
	}
	
}
