package model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class TableViewUtils {
	
	public static <T extends DatabaseUtils> void preparar_tabela(TableView tabela, Map<String, String> colunas, Callable<List<Map>> func) {
		Task<List<Map>> task = new Task<List<Map>>() {

			@Override
			protected List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return func.call();
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();								
				
				TableViewUtils.setTableView(tabela, colunas, getValue());
			}						
		};
		
		Thread thread = new Thread( task );
		thread.start();
	}
	
	public static void setTableView(TableView tabela, Map<String, String> coluna_chave, List<Map> dados) {
		tabela.getItems().clear();
		tabela.getColumns().clear();
		
		Callback<TableColumn<Map, String>, TableCell<Map, String>>
        cellFactoryForMap = new Callback<TableColumn<Map, String>,
            TableCell<Map, String>>() {
                @Override
                public TableCell call(TableColumn p) {
                    return new TextFieldTableCell(new StringConverter() {
                        @Override
                        public String toString(Object t) {                        	
                            return (t != null)? t.toString() : "";
                        }
                        @Override
                        public Object fromString(String string) {
                            return string;
                        }                                    
                    });
                }
	    };
		
		for( Entry entry : coluna_chave.entrySet() ) {
			TableColumn<Map, String> coluna = new TableColumn<>( String.valueOf( entry.getKey() ) );
			coluna.setCellValueFactory( new MapValueFactory( entry.getValue() ) );
			
			tabela.getColumns().add( coluna );
			coluna.setCellFactory( cellFactoryForMap );
		}
		
		ObservableList lista = FXCollections.observableArrayList( dados ); 
		tabela.setItems( lista );
	}
}