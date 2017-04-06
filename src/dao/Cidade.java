package dao;

import model.DatabaseUtils;

public class Cidade extends DatabaseUtils {
	private String nome_tabela = "tbl_cidade";

	private int id;
	private String nome;
	private int idEstado;
	
	@Override
	public String toString() {
		return this.nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public int getIdEstado() {
		return idEstado;
	}
}
