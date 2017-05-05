package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import model.AlertDialog;
import model.Context;
import view.WindowManager;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class PagamentoDiariasController implements Initializable {

	@FXML VBox boxCodigoSeguranca;
	@FXML PasswordField txtCodigoSegurancaCartao;
	@FXML Button btnConfirmar;
	@FXML Label lblTitulo;
	
	private final int CARTAO_CREDITO = 1, DINHEIRO = 2;
	int forma_pagamento;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		forma_pagamento = Context.getIntData("formaPagamento");
		if( forma_pagamento == DINHEIRO ) {
			lblTitulo.setText("Pagamento em Dinheiro");
			btnConfirmar.setText("Confirmar Recebimento");
			btnConfirmar.setPrefWidth(150);
			boxCodigoSeguranca.setVisible(false);
		}
	}
	
	public boolean verificarCodigoSeguranca(String codigo_seguranca) {
		if( codigo_seguranca.length() >= 3 ) {
			return true;
			//chamar api para verificação de codigo de segurança...
		}
		
		return false;
	}
	
	@FXML public void confirmarPagamento(ActionEvent event) {
		
		boolean pagamento_confirmado = false;
		if( forma_pagamento == CARTAO_CREDITO ) {
			String codigo_seguranca = txtCodigoSegurancaCartao.getText();
			
			if( !verificarCodigoSeguranca( codigo_seguranca ) ) {
				AlertDialog.show("Erro", "Houve um erro", "Código de Segurança incorreto", AlertType.ERROR);
				return;
			}
			
			pagamento_confirmado = true;			
		} else if( forma_pagamento == DINHEIRO ) {
			pagamento_confirmado = true;			
		}
		
		Context.addData("isPagamentoConfirmado", pagamento_confirmado);
		WindowManager.fecharJanela( WindowManager.get_stage_de_componente(btnConfirmar) );
		
	}

}
