package model;

import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;

public class ComboBoxUtils {
	
	public static <T extends DatabaseUtils> void popular_combobox(ComboBox combo_box, T t) {
		
		Task<List<? extends DatabaseUtils>> task_get_dados = new Task<List<? extends DatabaseUtils>>() {

			@Override
			protected List<? extends DatabaseUtils> call() throws Exception {
				// TODO Auto-generated method stub				
				return t.buscar( t.getClass() );
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();				
				combo_box.getItems().clear();
				combo_box.getItems().addAll( getValue() );				
			}
		};
		
		Thread thread = new Thread( task_get_dados );
		thread.start();
	}
	
	public static <T extends DatabaseUtils> void popular_combobox(ComboBox combo_box, List<T> t) {
		combo_box.getItems().clear();
		System.out.println( combo_box.getItems().size() );
		combo_box.getItems().addAll( t );
	}
	
	public static <T extends DatabaseUtils> void popular_combobox(ComboBox combo_box, String where, List<Object> parametros, T t) {
		
		Task<List<? extends DatabaseUtils>> task_get_dados = new Task<List<? extends DatabaseUtils>>() {

			@Override
			protected List<? extends DatabaseUtils> call() throws Exception {
				// TODO Auto-generated method stub				
				return t.buscar(where, parametros, t.getClass() );
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				combo_box.getItems().clear();
				combo_box.getItems().addAll( getValue() );				
			}
		};
		
		Thread thread = new Thread( task_get_dados );
		thread.start();
	}
	
	public static <T extends DatabaseUtils> void selecionar_item_combobox(ComboBox combo_box, String where, List<Object> parametros, T t) {
		
		Task task_get_dados = new Task() {

			@Override
			protected Object call() throws Exception {
				// TODO Auto-generated method stub				
				return t.buscar( where, parametros, t.getClass() ).get(0);
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				combo_box.getSelectionModel().select( getValue() );
			}
		};
		
		Thread thread = new Thread( task_get_dados );
		thread.start();
	}
}
