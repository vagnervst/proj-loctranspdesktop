package model;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressStage {
	
	private ProgressIndicator progress;
	private Stage stage;	
	
	public void show() {
		progress = new ProgressIndicator(-1);
		
		Group root = new Group();
		Scene scene = new Scene(root, 70, 70);
		scene.setFill( Color.TRANSPARENT );
		
		stage = new Stage();
		stage.initModality( Modality.NONE );
		stage.initStyle( StageStyle.TRANSPARENT );
		stage.setAlwaysOnTop(true);
		
		progress.setBackground( Background.EMPTY );
		
		scene.setRoot( progress );
		
		stage.setScene( scene );
		stage.show();
	}
	
	public void hide() {
		if( progress == null ) return;
				
		stage.close();
	}
}
