package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	private Connection conn = null;
	private String host = "127.0.0.1", usuario = "root", senha = "root", banco = "dbcityshare";

	public Database() {
		this.abrirConexao();
	}

	public Connection abrirConexao() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String conexao = "jdbc:mysql://" + this.host + ":3306/" + this.banco + "?user=" + this.usuario + "&password=" + this.senha;

		try { 
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			this.conn = DriverManager.getConnection(conexao);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this.getConexao();
	}

	public void fecharConexao() {
		if( this.getConexao() != null ) {
			try {
				this.getConexao().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Connection getConexao() {
		return conn;
	}
}
