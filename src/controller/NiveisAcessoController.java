package controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.NivelAcessoJuridico;
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

public class NiveisAcessoController implements Initializable {

	@FXML TableView<List<Map>> tblNiveisAcesso;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		preparar_tabela_niveis();		
	}

	private void preparar_tabela_niveis() {
        Map<String, String> colunas = new LinkedHashMap<String, String>();
        colunas.put("cod", "id");
        colunas.put("Titulo", "titulo");        
        colunas.put("Funcionários Atribuidos", "qtdFuncionarios");
        
        TableViewUtils.preparar_tabela(tblNiveisAcesso, colunas, new Callable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return new NivelAcessoJuridico().getNiveisAcesso();
			}
		});
	}

	@FXML public void cadastrar(ActionEvent event) {
		abrir_formulario();
	}

	@FXML public void editar(ActionEvent event) {
		Map<String, Object> item_selecionado = (Map<String, Object>) tblNiveisAcesso.getSelectionModel().getSelectedItem();
		
		if( item_selecionado == null ) return;
		
		int id_nivel_selecionado = (int) item_selecionado.get("id");
		
		Context.addData("idNivelAcesso", id_nivel_selecionado);
		abrir_formulario();
	}

	public void abrir_formulario() {
		Stage janela_formulario = WindowManager.abrirModal("/view/FormularioNivelAcesso.fxml", getClass());
		
		janela_formulario.setOnCloseRequest( new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				// TODO Auto-generated method stub
				preparar_tabela_niveis();
			}
		} );
	}
}
