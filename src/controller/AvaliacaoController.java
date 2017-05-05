package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Avaliacao;
import dao.Pedido;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import model.AlertDialog;
import model.Context;
import model.CustomCallable;
import model.ThreadTask;
import view.WindowManager;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;

public class AvaliacaoController implements Initializable {

	@FXML AnchorPane apConteudo;
	@FXML Slider slNotaAvaliacao;
	@FXML TextArea txtAvaliacao;
	
	Pedido pedido;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		slNotaAvaliacao.setMin(0);
		slNotaAvaliacao.setValue(0);
		slNotaAvaliacao.setShowTickLabels(true);
		slNotaAvaliacao.setShowTickMarks(true);
		slNotaAvaliacao.setMinorTickCount(0);
		slNotaAvaliacao.setMajorTickUnit(1);
		slNotaAvaliacao.setSnapToTicks(true);
		slNotaAvaliacao.setMax(5);
		
		int id_pedido = Context.getIntData("idPedido");
		
		ThreadTask<Pedido> task_get_pedido = new ThreadTask<Pedido>( new Callable<Pedido>() {
			
			@Override
			public Pedido call() throws Exception {
				// TODO Auto-generated method stub
				return new Pedido().buscar("id = ?", Arrays.asList( id_pedido ), Pedido.class).get(0);
			}
			
		}, new CustomCallable<Pedido>() {
			
			@Override
			public Pedido call() throws Exception {
				// TODO Auto-generated method stub
				pedido = (Pedido) getParametro();				
				return super.call();
			}
			
		});
		
		if( id_pedido != -1 ) {
			task_get_pedido.runWithProgress(apConteudo);
		}
	}

	@FXML public void confirmarAvaliacao(ActionEvent event) {
		if( pedido == null ) return;
		
		int nota = (int) slNotaAvaliacao.getValue();
		String comentario = txtAvaliacao.getText().trim();
		
		Avaliacao avaliacao_a_inserir = new Avaliacao();
		avaliacao_a_inserir.setIdUsuarioAvaliador( pedido.getIdUsuarioLocador() );
		avaliacao_a_inserir.setIdUsuarioAvaliado( pedido.getIdUsuarioLocatario() );
		avaliacao_a_inserir.setMensagem( comentario );		
		avaliacao_a_inserir.setNota( nota );
		
		ThreadTask<Integer> task_avaliar = new ThreadTask<Integer>(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				return avaliacao_a_inserir.inserir();
			}
			
		}, new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				int id_inserido = (int) getParametro();
				
				if( id_inserido != -1 ) {
					AlertDialog.show("Sucesso", "Operação bem sucedida", "Avaliação enviada com sucesso", AlertType.INFORMATION);
					Context.addData("pagamentoPendencia", true);
					WindowManager.fecharJanela( WindowManager.get_stage_de_componente(apConteudo) );
				} else {
					AlertDialog.show("Erro", "Houve um erro", "Não foi possível enviar a avaliação", AlertType.ERROR);
				}
				
				return super.call();
			}
			
		});
		
		task_avaliar.runWithProgress(apConteudo);
	}

}
