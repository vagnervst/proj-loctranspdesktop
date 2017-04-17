package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Funcionario;
import dao.Publicacao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Context;
import model.CustomCallable;
import model.Login;
import model.TableViewUtils;
import model.ThreadTask;
import view.WindowManager;

public class PublicacoesController implements Initializable {

	@FXML TableView<List<Map>> tblPublicacoes;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		preparar_tabela_publicacoes();
	}
	
	public void preparar_tabela_publicacoes() {
		Map<String, String> colunas = new LinkedHashMap<>();
		colunas.put("cod", "id");
		colunas.put("titulo", "titulo");
		colunas.put("Tipo", "tipoVeiculo");
		colunas.put("Valor da di�ria", "valorDiaria");
		colunas.put("Valor do Combust�vel", "valorCombustivel");
		colunas.put("Loca��es", "locacoes");
		colunas.put("Status", "statusPublicacao");
		
		if( Login.get_tipo_conta() == Login.JURIDICO ) {
						
			Login.get_id_empresa(new CustomCallable<Integer>() {
				
				@Override
				public Integer call() throws Exception {
					// TODO Auto-generated method stub
					Integer id_empresa = (Integer) this.getParametro();
					
					TableViewUtils.preparar_tabela(tblPublicacoes, colunas, new Callable<List<Map>>() {
						
						@Override
						public List<Map> call() throws Exception {
							// TODO Auto-generated method stub
							return new Publicacao().getPublicacoes("a.idEmpresa = " + id_empresa);
						}
					});
					
					return super.call();
				}
				
			});
		} else if( Login.get_tipo_conta() == Login.FUNCIONARIO ) {
			
			ThreadTask<Funcionario> task_busca_funcionario = new ThreadTask<Funcionario>(new Callable<Funcionario>() {
				
				@Override
				public Funcionario call() throws Exception {
					// TODO Auto-generated method stub
					return new Funcionario().buscar("id = ?", Arrays.asList( Login.get_id_usuario() ), Funcionario.class).get(0);
				}
				
			}, new CustomCallable<Funcionario>() {
				
				@Override
				public Funcionario call() throws Exception {
					// TODO Auto-generated method stub
					Funcionario funcionario_atual = (Funcionario) this.getParametro();
					long id_agencia = funcionario_atual.getIdAgencia();
					
					TableViewUtils.preparar_tabela(tblPublicacoes, colunas, new Callable<List<Map>>() {
						
						@Override
						public List<Map> call() throws Exception {
							// TODO Auto-generated method stub
							return new Publicacao().getPublicacoes("p.idAgencia = " + id_agencia);
						}
					});
					
					return super.call();
				}
				
			});
			
			task_busca_funcionario.run();
		}
	}
	
	public void abrirFormularioPublicacao() {
		Stage formPublicacao = WindowManager.abrirModal("/view/FormularioPublicacao.fxml", getClass());
		
		formPublicacao.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				preparar_tabela_publicacoes();
			}
		});
	}
	
	@FXML
	public void abrirCadastroPublicacao(ActionEvent event) {
		abrirFormularioPublicacao();
	}

	@FXML public void editarPublicacao(ActionEvent event) {
		Map publicacao_selecionada = (Map) tblPublicacoes.getSelectionModel().getSelectedItem();
		
		if( publicacao_selecionada == null ) return;
		
		Long id_publicacao = Integer.toUnsignedLong( (int) publicacao_selecionada.get("id") );
		
		Context.addData("idPublicacao", id_publicacao);
		abrirFormularioPublicacao();
	}

}
