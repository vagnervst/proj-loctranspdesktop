package controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import view.WindowManager;

public class PerfisNivelAcessoController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
        preparar_tabela_perfis();
	}
	
	private void preparar_tabela_perfis() {
        Map<String, String> colunas = new LinkedHashMap<String, String>();
        colunas.put("cod", "id");
        colunas.put("Titulo", "titulo");
        colunas.put("Agências Atribuidas", "qtdAgencias");
        colunas.put("Funcionários Atribuidos", "qtdFuncionarios");
	}
	
	@FXML public void cadastrar(ActionEvent event) {
		abrir_formulario();
	}

	@FXML public void editar(ActionEvent event) {}
	
	public void abrir_formulario() {
		Stage janela_formulario = WindowManager.abrirModal("/view/FormularioNivelAcesso.fxml", getClass());
	}
}
