package model;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertDialog {
	
	public static boolean show(String titulo, String cabecalho, String conteudo, AlertType tipo) {
		Alert alert = new Alert( tipo );
		alert.setTitle( titulo );
		alert.setHeaderText( cabecalho );
		alert.setContentText( conteudo );
		
		Optional<ButtonType> resultado = alert.showAndWait();
		if( resultado.get() == ButtonType.OK ) {
			return true;
		} else {
			return false;
		}
	}
}
