package controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Funcionario;
import dao.NivelAcessoJuridico;
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

public class NiveisAcessoController implements Initializable {

	@FXML TableView<List<Map>> tblNiveisAcesso;
	@FXML Button btnEditar;
	@FXML Button btnCadastrar;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		preparar_tabela_niveis();
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
					
					final int cod_tela_niveis_acesso = 4;
                    
                    boolean tela_niveis_acesso = false;
                    
					for( int i = 0; i < telas_permitidas.size(); ++i ) {

						Map<String, Object> tela = telas_permitidas.get(i);
						
						int codigo_tela = ((int) tela.get("cod"));
						
						if( codigo_tela == cod_tela_niveis_acesso ) {
							tela_niveis_acesso = true;
		
							if( tela.get("edicao") == null ) {
								btnEditar.setDisable(true);
							}
							
							if( tela.get("escrita") == null ) {
								btnCadastrar.setDisable(true);
							}
							
						}
					}

                    if( tela_niveis_acesso == false ) {
                    	btnEditar.setDisable(true);
                    	btnCadastrar.setDisable(true);
                    }                    
                    
					return super.call();
				}

			});
		}
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
				return new NivelAcessoJuridico().getNiveisAcesso(null, null);
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
