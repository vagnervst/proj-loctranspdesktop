package model;

import java.util.concurrent.Callable;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class ThreadTask<T> {

	private Callable<T> action;
	private CustomCallable<T> resultfunc;
	private Thread thread;
	private StackPane progressPane;
	private Pane conteudo_a_ocultar;
	
	public ThreadTask( Callable<T> action, CustomCallable<T> call ) {
		this.action = action;
		this.resultfunc = call;
	}
	
	public void run() {
		Task task = new Task(){

			@Override
			protected Object call() throws Exception {
				// TODO Auto-generated method stub
				return action.call();
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				try {
					if( resultfunc != null ) {					
						resultfunc.putParametro( this.get() );				
						resultfunc.call();					
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
					
		thread = new Thread( task );
		thread.start();		
	}
	
	public void runWithProgress(Pane conteudo_a_ocultar) {
		Task task = new Task(){

			@Override
			protected Object call() throws Exception {
				// TODO Auto-generated method stub				
				return action.call();
			}
			
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
				try {														
					conteudo_a_ocultar.getChildren().remove( conteudo_a_ocultar.getChildren().indexOf( progressPane ) );
					
					for( int i = 0; i < conteudo_a_ocultar.getChildren().size(); ++i) {
						conteudo_a_ocultar.getChildren().get(i).setVisible(true);						
					}
					
					if( resultfunc != null ) {					
						resultfunc.putParametro( this.get() );				
						resultfunc.call();					
					}					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		
		for( int i = 0; i < conteudo_a_ocultar.getChildren().size(); ++i) {
			conteudo_a_ocultar.getChildren().get(i).setVisible(false);					
		}
		
		progressPane = new StackPane();		
		ProgressIndicator progress = new ProgressIndicator();
		progress.setMaxWidth(100);
		progress.setMaxHeight(100);
		progressPane.setAlignment( Pos.CENTER );		
		progressPane.getChildren().add( progress );
		progressPane.setPrefSize( conteudo_a_ocultar.getPrefWidth(), conteudo_a_ocultar.getPrefHeight() );
		conteudo_a_ocultar.getChildren().add( progressPane );		
		
		thread = new Thread( task );
		thread.start();
	}
	
	public boolean isRunning() {
		if( thread != null )
			return thread.isAlive();
		
		return false;
	}
}
