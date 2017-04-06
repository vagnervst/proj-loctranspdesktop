package dao;

import model.DatabaseUtils;

public class Estado extends DatabaseUtils {
	private String nome_tabela = "tbl_estado";

	public int id;
	public String nome;
	public int idPais;

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

	public int getIdPais() {
		return idPais;
	}
}
