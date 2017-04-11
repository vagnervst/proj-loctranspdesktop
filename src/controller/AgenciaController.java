package controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Agencia;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Context;
import model.TableViewUtils;
import view.WindowManager;

public class AgenciaController implements Initializable {

	@FXML TableView<List<Map>> tabelaAgencias;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		preparar_tabela_agencias();
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
