package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Usuario;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.TableViewUtils;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;

public class UsuariosController implements Initializable {

	@FXML TextField txtNome;
	@FXML TextField txtSobrenome;
	@FXML TextField txtRg;
	@FXML TextField txtCpf;
	@FXML TableView tvResultados;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		preencherTabela();
	}

	public void preencherTabela() {
		Map<String, String> colunas = new LinkedHashMap<String, String>();
		colunas.put("Nome", "nomeCompleto");		
		colunas.put("RG", "rg");
		colunas.put("CPF", "cpf");
		colunas.put("Localização", "localizacao");
		
		TableViewUtils.preparar_tabela(tvResultados, colunas, new Callable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
								
				Map<String, Object> parametros_pesquisa = new LinkedHashMap<>();
				parametros_pesquisa.put("u.idTipoConta", "1");
				
				if( !txtNome.getText().trim().isEmpty() ) parametros_pesquisa.put("u.nome", txtNome.getText().trim());
				if( !txtSobrenome.getText().trim().isEmpty() ) parametros_pesquisa.put("u.sobrenome", txtSobrenome.getText().trim());
				if( !txtRg.getText().trim().isEmpty() ) parametros_pesquisa.put("u.rg", txtRg.getText().trim());
				if( !txtCpf.getText().trim().isEmpty() ) parametros_pesquisa.put("u.cpf", txtCpf.getText().trim());
				
				String where = "";
				List<Object> parametros_where = new ArrayList<>();							
				
				int i = 0;
				for( Map.Entry<String, Object> entry : parametros_pesquisa.entrySet() ) {									
					String key = entry.getKey();
					String value = (String) entry.getValue();
					
					if( i > 0 && i < parametros_pesquisa.size() ) {
						where += " AND ";
					}
					
					parametros_where.add(value);
					
					where += key + " LIKE ?";
					++i;
				}							
				
				System.out.println( where );
				
				List<Map> resultado = new Usuario().listar_usuarios(where, parametros_where); 
				return resultado;
			}
			
		});
	}

	@FXML public void buscar(ActionEvent event) {
		
		preencherTabela();
		
	}

}
