package controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Pedido;
import javafx.fxml.Initializable;
import view.WindowManager;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Context;
import model.TableViewUtils;

public class SolicitacoesController implements Initializable {

	@FXML TableView tabelaPedidos;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		preencher_tabela();
	}
	
	public void preencher_tabela() {
		Map<String, String> colunas_propriedades = new LinkedHashMap<>();
		colunas_propriedades.put("cod", "id");
		colunas_propriedades.put("veiculo", "modeloVeiculo");
		colunas_propriedades.put("agencia", "tituloAgencia");
		colunas_propriedades.put("status", "statusPedido");
		
		TableViewUtils.preparar_tabela(tabelaPedidos, colunas_propriedades, new Callable<List<Map>>() {
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return new Pedido().listar_pedidos();
			}
		});
	}
	
	private void abrirFormularioSolicitacao() {
		Stage formularioPedido = WindowManager.abrirModal("/view/FormularioSolicitacao.fxml", getClass());
		formularioPedido.setOnCloseRequest( e -> {
			preencher_tabela();
		});
	}
	
	@FXML public void abrirFormularioSolicitacao(ActionEvent event) {
		abrirFormularioSolicitacao();
	}

	@FXML public void visualizarSolicitacao(ActionEvent event) {
		Map pedidoSelecionado = (Map) tabelaPedidos.getSelectionModel().getSelectedItem();
		
		if( pedidoSelecionado == null ) return;
		
		int idPedido = (int) pedidoSelecionado.get("id");
		Context.addData("idPedido", idPedido);
		
		abrirFormularioSolicitacao();
	}

}
