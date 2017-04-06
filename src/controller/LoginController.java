package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import view.WindowManager;

public class LoginController implements Initializable {

	@FXML Button btnEntrar;
	@FXML AnchorPane apLogin;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML public void login() {
		WindowManager wm = new WindowManager();
		wm.abrirJanela( wm.get_stage_de_componente(btnEntrar) , "/view/Home.fxml" );
	}

}
