package controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import dao.Pedido;
import dao.Veiculo;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import model.AlertDialog;
import model.Context;
import model.CustomCallable;
import model.ThreadTask;
import javafx.scene.layout.AnchorPane;

public class FormularioDefinirPendenciasController implements Initializable {

	@FXML Label lblDiasAtraso;	
	@FXML Slider slCombustivel;
	@FXML Label lblValorCombustivel;
	@FXML TextField txtQuilometragemExcedida;
	@FXML AnchorPane apConteudo;
	
	Pedido info_pedido;	
	Veiculo info_veiculo;	
	
	double quantidadeCombustivelRestante = 0;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		slCombustivel.setMin(0);
		slCombustivel.setValue(0);
		slCombustivel.setShowTickLabels(true);
		slCombustivel.setShowTickMarks(true);
		slCombustivel.setMinorTickCount(0);
		slCombustivel.setMajorTickUnit(1);
		slCombustivel.setSnapToTicks(true);
		slCombustivel.setMax(8);
		
		slCombustivel.valueProperty().addListener( new ValorCombustivelAlterado<>() );
		
		txtQuilometragemExcedida.lengthProperty().addListener( new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				int maximo_caracteres = 5;
				if( txtQuilometragemExcedida.getText().length() > maximo_caracteres ) {
					txtQuilometragemExcedida.setText( txtQuilometragemExcedida.getText().trim().substring(0, maximo_caracteres) );
				}
			}
			
		});
		
		int idPedido = Context.getIntData("idPedido");
		
		if( idPedido != -1 ) {
			
			ThreadTask<Veiculo> task_busca_info_veiculo = new ThreadTask<Veiculo>( new Callable<Veiculo>() {
				
				@Override
				public Veiculo call() throws Exception {
					// TODO Auto-generated method stub
					return new Veiculo().buscar("id = ?", Arrays.asList( info_pedido.getIdVeiculo() ), Veiculo.class).get(0);
				}
				
			}, new CustomCallable<Veiculo>() {
				
				@Override
				public Veiculo call() throws Exception {
					// TODO Auto-generated method stub
					info_veiculo = (Veiculo) getParametro();
					
					preencher_informacoes_pedido(info_pedido);
					
					return super.call();
				}
				
			});
			
			ThreadTask<Pedido> task_busca_info_pedido = new ThreadTask<Pedido>( new Callable<Pedido>() {
				
				@Override
				public Pedido call() throws Exception {
					// TODO Auto-generated method stub
					return new Pedido().buscar("id = ?", Arrays.asList( idPedido ), Pedido.class).get(0);
				}
				
			}, new CustomCallable<Pedido>() {
				
				@Override
				public Pedido call() throws Exception {
					// TODO Auto-generated method stub
					info_pedido = (Pedido) getParametro();
					
					task_busca_info_veiculo.run();
					return super.call();
				}
				
			});
			
			task_busca_info_pedido.runWithProgress(apConteudo);
		}
	}

	public void preencher_informacoes_pedido(Pedido pedido) {
		Timestamp data_entrega = pedido.getDataEntrega();
		Timestamp data_entrega_efetuada = pedido.getDataEntregaEfetuada();			
		
		System.out.println( data_entrega );
		System.out.println( data_entrega_efetuada );
		if( data_entrega == null || data_entrega_efetuada == null ) return;
		
		long diferenca = data_entrega_efetuada.getTime() - data_entrega.getTime();
		
		diferenca = TimeUnit.DAYS.convert(diferenca, TimeUnit.MILLISECONDS);
		double valorTotalAtraso = pedido.getValorDiaria().doubleValue() * diferenca;
		
		lblDiasAtraso.setText( String.format("%d dias = R$%.2f", diferenca, valorTotalAtraso) );			
		
		if( pedido.getCombustivelRestante() != null ) {		
			double combustivelRestante = pedido.getCombustivelRestante().doubleValue();
			double representacao_oitavos = (combustivelRestante / info_veiculo.getTanque().doubleValue()) * 8;		
			slCombustivel.setValue(representacao_oitavos);
		}
		
		if( pedido.getQuilometragemExcedida() != null ) {		
			txtQuilometragemExcedida.setText( String.valueOf( pedido.getQuilometragemExcedida() ) );		
		}
	}
	
	@FXML public void salvarDefinicao(ActionEvent event) {
		
		try {
			int id_status_aguardando_pagamento_pendencias = 8;
			int quilometragemExcedida = Integer.valueOf( txtQuilometragemExcedida.getText().trim() );
			info_pedido.setCombustivelRestante( BigDecimal.valueOf(quantidadeCombustivelRestante) );
			info_pedido.setQuilometragemExcedida( quilometragemExcedida );
			info_pedido.setIdStatusPedido(id_status_aguardando_pagamento_pendencias);
			
			ThreadTask<Boolean> task_definir_pendencias = new ThreadTask<Boolean>( new Callable<Boolean>() {
				
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
						AlertDialog.show("Sucesso", "Operação bem sucedida", "Definição de pendências bem sucedida", AlertType.INFORMATION);
					} else {
						AlertDialog.show("Erro", "Houve um erro", "Ocorreu um erro na definição de pendências", AlertType.ERROR);
					}
					
					return super.call();
				}
				
			});
			
			task_definir_pendencias.runWithProgress(apConteudo);
			
		} catch( NumberFormatException e ) {
			AlertDialog.show("Erro", "Houve um erro", "Quilometragem excedida invalida", AlertType.ERROR);
		}
	}

	@FXML public void cancelarDefinicao(ActionEvent event) {
		
	}
	
	private class ValorCombustivelAlterado<Double> implements ChangeListener<Double> {

		@Override
		public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
			// TODO Auto-generated method stub					
			double percentualCombustivel = (double) newValue;
			double valorCombustivel = info_pedido.getValorCombustivel().doubleValue();
			double tanqueVeiculo = info_veiculo.getTanque().doubleValue();
									
			quantidadeCombustivelRestante = (tanqueVeiculo * ((percentualCombustivel/8)/100))*100;
			System.out.println("Tanque: " + tanqueVeiculo + " qtd: " + quantidadeCombustivelRestante);			
			
			double totalCombustivel = quantidadeCombustivelRestante * valorCombustivel;
			
			lblValorCombustivel.setText( String.format("%.2f litros = R$%.2f", quantidadeCombustivelRestante, totalCombustivel) );
		}
	}
}
