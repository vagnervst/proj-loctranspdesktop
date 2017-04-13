package model;

import java.util.concurrent.Callable;

public class  CustomCallable<T> implements Callable<T> {

	private Object parametro;

	public Object getParametro() {
		return this.parametro;
	}

	public void putParametro(Object valor) {
		this.parametro = valor;
	}

	@Override
	public T call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
