package controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import dao.Funcionario;
import javafx.concurrent.Task;
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

public class FuncionarioController implements Initializable {

	@FXML TableView<List<Map>> tabelaFuncionarios;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		preparar_tabela_funcionarios();
	}

	public void preparar_tabela_funcionarios() {				
		Task<List<Map>> task_agencias = new Task<List<Map>>() {

			@Override
			protected List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return new Funcionario().getFuncionarios();
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				
				Map<String, String> colunas = new LinkedHashMap<String, String>();
				colunas.put("cod", "id");
				colunas.put("Nome", "nome");
				colunas.put("Nível de Acesso", "nivelAcesso");
				colunas.put("Agência", "agencia");
				colunas.put("Status", "statusOnline");				
				TableViewUtils.setTableView(tabelaFuncionarios, colunas, getValue());
			}						
		};
		
		Thread thread_agencias = new Thread( task_agencias );
		thread_agencias.start();
	}
	
	@FXML public void cadastrar(ActionEvent event) {
		abrir_formulario_funcionario();
	}

	@FXML public void editar(ActionEvent event) {
		Map<String, Object> item_selecionado = (Map<String, Object>) tabelaFuncionarios.getSelectionModel().getSelectedItem();
		
		if( item_selecionado == null ) return;
		
		int id_funcionario_selecionado = (int) item_selecionado.get("id");
		Context.addData( "idFuncionario", id_funcionario_selecionado );		
		
		abrir_formulario_funcionario();
	}
	
	public void abrir_formulario_funcionario() {
		Stage janela_formulario_funcionario = WindowManager.abrirModal("/view/FormularioFuncionario.fxml", getClass());
		
		janela_formulario_funcionario.setOnCloseRequest( new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub				
				preparar_tabela_funcionarios();
			}
		});
	}
}
