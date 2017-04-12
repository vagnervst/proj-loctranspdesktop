package model;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ConfirmDialog {
	
	public static boolean show(String titulo, String cabecalho, String conteudo) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
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
