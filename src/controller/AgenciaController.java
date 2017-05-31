package controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Agencia;
import dao.Funcionario;
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
import view.WindowManager;
import javafx.scene.control.Button;

public class AgenciaController implements Initializable {

	@FXML TableView<List<Map>> tabelaAgencias;
	@FXML Button btnEditar;
	@FXML Button btnCadastrar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		preparar_tabela_agencias();
		controle_permissoes();
	}		
	
	public void preparar_tabela_agencias() {			
		Map<String, String> colunas = new LinkedHashMap<String, String>();
		colunas.put("cod", "id");
		colunas.put("Titulo", "titulo");
		colunas.put("Estado", "estado");
		colunas.put("Cidade", "cidade");
		colunas.put("Funcionarios", "funcionarios");
		colunas.put("Veículos", "veiculos");
		
		TableViewUtils.preparar_tabela(tabelaAgencias, colunas, new Callable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return new Agencia().getAgencias();
			}
		});
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
		
							if( tela.get("edicao") == null ) {
								btnEditar.setDisable(true);
							}
							
							if( tela.get("escrita") == null ) {
								btnCadastrar.setDisable(true);
							}
							
						}
					}

                    if( tela_agencias == false ) {
                    	btnEditar.setDisable(true);
                    	btnCadastrar.setDisable(true);
                    }                    
                    
					return super.call();
				}

			});
		}
	}
	
	@FXML public void abrirCadastrar(ActionEvent event) {
		abrir_formulario_agencia();
	}

	@FXML public void editarAgencia(ActionEvent event) {
		Map<String, Object> item_selecionado = (Map<String, Object>) tabelaAgencias.getSelectionModel().getSelectedItem();
		
		if( item_selecionado == null ) return;
		
		int id_agencia_selecionada = (int) item_selecionado.get("id");
		Context.addData( "idAgencia", id_agencia_selecionada );		
		
		abrir_formulario_agencia();
	}
	
	public void abrir_formulario_agencia() {
		Stage janela_formulario_agencia = WindowManager.abrirModal( "/view/FormularioAgencia.fxml", getClass());
		
		janela_formulario_agencia.setOnCloseRequest( new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub				
				preparar_tabela_agencias();
			}
		});
	}
}
