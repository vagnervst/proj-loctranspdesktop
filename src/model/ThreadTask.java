package model;

import java.util.concurrent.Callable;

import javafx.concurrent.Task;

public class ThreadTask<T> {

	Callable<T> action;
	CustomCallable<T> resultfunc;

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
					resultfunc.putParametro( this.get() );
					resultfunc.call();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		
		Thread thread = new Thread( task );
		thread.start();
	}
}
