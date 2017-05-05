package controller;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import dao.Pedido;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import model.AlertDialog;
import model.Context;
import model.CustomCallable;
import model.ThreadTask;
import view.WindowManager;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.PasswordField;

public class PagamentoPendenciasController implements Initializable {

	@FXML AnchorPane apConteudo;
	@FXML Label lblDiasAtraso;
	@FXML Label lblCombustivelRestante;
	@FXML Label lblDistanciaExcedida;
	@FXML PasswordField txtCodigoSeguranca;
	
	final int CARTAO_CREDITO = 1, DINHEIRO = 2;
	Pedido info_pedido;	
	int forma_pagamento;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		forma_pagamento = Context.getIntData("formaPagamento");
		System.out.println("forma: " + forma_pagamento);
		if( forma_pagamento == DINHEIRO ) {
			txtCodigoSeguranca.setVisible(false);
		}
		
		int id_pedido = Context.getIntData("idPedido");
		
		if( id_pedido != -1 ) {
			
			ThreadTask<Pedido> get_info_pedido = new ThreadTask<Pedido>( new Callable<Pedido>() {
				
				@Override
				public Pedido call() throws Exception {
					// TODO Auto-generated method stub
					return new Pedido().buscar("id = ?", Arrays.asList( id_pedido ), Pedido.class).get(0);
				}
				
			}, new CustomCallable<Pedido>() {
				
				@Override
				public Pedido call() throws Exception {
					// TODO Auto-generated method stub
					info_pedido = (Pedido) getParametro();
					preencher_informacoes_pedido( info_pedido );
					return super.call();
				}
				
			});
			
			get_info_pedido.runWithProgress(apConteudo);
		}
	}

	public void preencher_informacoes_pedido( Pedido pedido ) {
		if( pedido == null ) return;
		
		Timestamp data_entrega = pedido.getDataEntrega();
		Timestamp data_entrega_efetuada = pedido.getDataEntregaEfetuada();
		
		if( data_entrega == null || data_entrega_efetuada == null ) return;
		
		long diferenca = data_entrega_efetuada.getTime() - data_entrega.getTime();		
		diferenca = TimeUnit.DAYS.convert(diferenca, TimeUnit.MILLISECONDS);
		double valorTotalAtraso = pedido.getValorDiaria().doubleValue() * diferenca;		
		lblDiasAtraso.setText( String.format("%d dias = R$%.2f", diferenca, valorTotalAtraso) );
		
		double combustivelRestante = pedido.getCombustivelRestante().doubleValue();
		double valorTotalCombustivelRestante = pedido.getValorCombustivel().doubleValue() * combustivelRestante;
		lblCombustivelRestante.setText( String.format("%.2f L = R$.2f", combustivelRestante, valorTotalCombustivelRestante) );
		
		int distanciaExcedida = pedido.getQuilometragemExcedida();
		lblDistanciaExcedida.setText( String.format("%d quilometros = %.2f", distanciaExcedida, pedido.getValorQuilometragem()) ); 
	}
	
	@FXML public void confirmarPagamento(ActionEvent event) {
		
		int id_status_pedido_concluido = 9;
		info_pedido.setIdStatusPedido(id_status_pedido_concluido);
		
		if( forma_pagamento == CARTAO_CREDITO ) {
			
			if( txtCodigoSeguranca.getText().trim().isEmpty() ) {
				AlertDialog.show("Erro", "Ocorreu um erro", "Digite o código de segurança", AlertType.ERROR);
			}
			
			info_pedido.setIdFormaPagamentoPendencias(CARTAO_CREDITO);			
		} else if( forma_pagamento == DINHEIRO ) {
			info_pedido.setIdFormaPagamentoPendencias(DINHEIRO);
		}
		
		ThreadTask<Boolean> task_pagamento_pendencias = new ThreadTask<Boolean>( new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				return info_pedido.atualizar();
			}
			
		}, new CustomCallable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				boolean resultado_alteracao = (boolean) getParametro();
				
				if( !resultado_alteracao ) {
					AlertDialog.show("Sucesso", "Operação bem sucedida", "Pagamento de pendências realizado!", AlertType.INFORMATION);					
					Context.addData("idPedido", info_pedido.getId());
					WindowManager.abrirJanela(WindowManager.get_stage_de_componente(apConteudo), "/view/Avaliacao.fxml", getClass());
				} else {
					AlertDialog.show("Erro", "Ocorreu um erro", "Houve uma falha ao tentar realizar o pagamento", AlertType.ERROR);
				}
				
				return super.call();
			}
			
		});
		
		task_pagamento_pendencias.runWithProgress(apConteudo);
	}
	
	@FXML public void cancelarPagamento(ActionEvent event) {
		Context.addData("pagamentoPendencia", false);
	}
}
