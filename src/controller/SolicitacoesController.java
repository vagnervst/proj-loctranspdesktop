package controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Funcionario;
import dao.Pedido;
import javafx.fxml.Initializable;
import view.WindowManager;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Context;
import model.CustomCallable;
import model.Login;
import model.TableViewUtils;
import javafx.scene.control.Button;

public class SolicitacoesController implements Initializable {

	@FXML TableView tabelaPedidos;
	@FXML Button btnNovaSolicitacao;
	@FXML Button btnVisualizarSolicitacao;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		preencher_tabela();
		controle_permissoes();
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
	
	public void controle_permissoes() {
		
		if( Login.get_tipo_conta() == Login.FUNCIONARIO ) {
			
			Funcionario funcionario = new Funcionario();
			funcionario.setId( Login.get_id_usuario() );
			
			funcionario.getPermissoes( new CustomCallable<List<Map>>() {
				
				@Override
				public List<Map> call() throws Exception {
					// TODO Auto-generated method stub
					List<Map> telas_permitidas = (List<Map>) getParametro();					
														
					final int cod_tela_solicitacoes = 6;
					boolean tela_encontrada = false;
					for( int i = 0; i < telas_permitidas.size(); ++i ) {
						
						Map<String, Object> tela = telas_permitidas.get(i);
						
						if( ((int) tela.get("cod")) == cod_tela_solicitacoes ) {
							tela_encontrada = true;
							
							if( tela.get("escrita") == null ) {
								btnNovaSolicitacao.setDisable(true);
							}																											
							
							if( tela.get("leitura") == null ) {
								btnVisualizarSolicitacao.setDisable(true);
							}
						}						
						
					}
					
					if( tela_encontrada == false ) {
						btnNovaSolicitacao.setDisable(true);
						btnVisualizarSolicitacao.setDisable(true);
					}
					
					return super.call();
				}
															
			});
		}
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
