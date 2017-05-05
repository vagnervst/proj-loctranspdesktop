package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Pedido;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.util.converter.LocalDateTimeStringConverter;
import model.Context;
import model.CustomCallable;
import model.ThreadTask;
import view.WindowManager;

public class ConfirmarEntregaController implements Initializable {

	@FXML Label lblDataEntregaEfetuada;
	@FXML Label lblDataEntrega;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		LocalDateTime data_hora_atual = LocalDateTime.now();		
		lblDataEntregaEfetuada.setText( data_local(data_hora_atual) );	
		get_data_entrega_pedido();
		
	}
	
	private String data_local( LocalDateTime data ) {
		LocalDateTimeStringConverter f = new LocalDateTimeStringConverter( FormatStyle.LONG, FormatStyle.SHORT );
		return f.toString( data );
	}
	
	public void get_data_entrega_pedido() {
		
		ThreadTask<Pedido> task_info_pedido = new ThreadTask<Pedido>( new Callable<Pedido>() {
			
			@Override
			public Pedido call() throws Exception {
				// TODO Auto-generated method stub
				int idPedido = Context.getIntData("idPedido");
				return new Pedido().buscar("id = ?", Arrays.asList( idPedido ), Pedido.class).get(0);
			}
			
		}, new CustomCallable<Pedido>() {
			
			@Override
			public Pedido call() throws Exception {
				// TODO Auto-generated method stub
				Pedido pedido_encontrado = (Pedido) getParametro();
				
				lblDataEntrega.setText( data_local( pedido_encontrado.getDataEntrega().toLocalDateTime() ) );
				return super.call();
			}
			
		});
		
		task_info_pedido.run();
	}
	
	@FXML public void cancelarEntrega(ActionEvent event) {
		Context.addData("confirmacaoEntrega", false);
		WindowManager.fecharJanela( WindowManager.get_stage_de_componente(lblDataEntrega) );
	}

	@FXML public void confirmarEntrega(ActionEvent event) {
		Context.addData("confirmacaoEntrega", true);
		WindowManager.fecharJanela( WindowManager.get_stage_de_componente(lblDataEntrega) );
	}

}
