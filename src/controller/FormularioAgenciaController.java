package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.Cidade;
import dao.Estado;
import dao.PerfilNivelAcesso;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class FormularioAgenciaController implements Initializable {

	@FXML ComboBox<Estado> cbEstado;
	@FXML ComboBox<Cidade> cbCidade;
	@FXML ComboBox<PerfilNivelAcesso> cbPerfilNivelAcesso;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
			
		cbEstado.getItems().addAll( new Estado().buscar(Estado.class) );
		
		cbCidade.getItems().addAll( new Cidade().buscar(Cidade.class) );
					
		cbPerfilNivelAcesso.getItems().addAll( new PerfilNivelAcesso().buscar(PerfilNivelAcesso.class) );
	}

}
