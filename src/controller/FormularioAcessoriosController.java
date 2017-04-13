package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.TipoVeiculo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Context;
import model.TableViewUtils;
import view.WindowManager;

public class FormularioAcessoriosController implements Initializable {

	@FXML TableView<List<Map>> tblAcessorios;
	@FXML TextField txtTitulo;
	@FXML CheckBox chkIncluir;
	@FXML Button btnCancelar;
	@FXML Button btnConcluido;
	
	List<Map> acessorios_incluidos;
	
	int idTipoVeiculo;
	Map<String, Object> acessorio_selecionado;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub			
		acessorios_incluidos = new ArrayList<Map>();
		
		idTipoVeiculo = Context.getIntData("idTipoVeiculo");
		
		if( idTipoVeiculo == -1 ) return;
		
		popular_tabela();
		tblAcessorios.setOnMouseClicked( new AcaoCliqueTabela() );
		chkIncluir.setOnAction( new AcaoCheckBoxInclusaoAcessorio() );
	}
	
	public void popular_tabela() {
		Map<String, String> colunas = new HashMap<String, String>();
		colunas.put("Nome do Acessório", "nome");
		
		TableViewUtils.preparar_tabela(tblAcessorios, colunas, new Callable<List<Map>>() {

			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				TipoVeiculo tipo_veiculo = new TipoVeiculo();
				tipo_veiculo.setId( idTipoVeiculo );
				
				return tipo_veiculo.getAcessorios();
			}
			
		});
	}

	private boolean is_acessorio_incluido(int id) {
		for( int i = 0; i < acessorios_incluidos.size(); ++i ) {
			if( (int) acessorios_incluidos.get(i).get("id") == id ) return true;
		}
		
		return false;
	}
	
	private List<Integer> get_lista_id_acessorios_incluidos() {
		List<Integer> lista_id = new ArrayList<>();
		
		for( int i = 0; i < acessorios_incluidos.size(); ++i ) {
			int id_acessorio = (int) acessorios_incluidos.get(i).get("id");
			
			lista_id.add( id_acessorio );
		}
		
		return lista_id;
	}
	
	private class AcaoCliqueTabela implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent arg0) {
			// TODO Auto-generated method stub
			acessorio_selecionado = (Map<String, Object>) tblAcessorios.getSelectionModel().getSelectedItem();
			
			txtTitulo.setText( (String) acessorio_selecionado.get("nome") );
			
			int id_acessorio = (int) acessorio_selecionado.get("id");
			
			if( is_acessorio_incluido( id_acessorio ) ) {
				chkIncluir.setSelected( true );
			} else {
				chkIncluir.setSelected( false );
			}
		}
	}
	
	private class AcaoCheckBoxInclusaoAcessorio implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
			if( acessorio_selecionado == null ) return;
			
			int id_acessorio_selecionado = (int) acessorio_selecionado.get("id");
			if( !is_acessorio_incluido( id_acessorio_selecionado ) ) {
				acessorios_incluidos.add( acessorio_selecionado );				
			} else {
				acessorios_incluidos.remove( acessorio_selecionado );				
			}
			
			acessorio_selecionado = null;
		}		
	}

	@FXML public void btnConcluido(ActionEvent event) {
		List<Integer> lista_id_acessorios = get_lista_id_acessorios_incluidos();
		
		Context.addData("listaIdAcessorios", lista_id_acessorios);
		WindowManager.fecharJanela( WindowManager.get_stage_de_componente(tblAcessorios) );
	}

	@FXML public void cancelar(ActionEvent event) {
		WindowManager.fecharJanela( WindowManager.get_stage_de_componente(tblAcessorios) );
	}
	
}
