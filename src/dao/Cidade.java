package dao;

import model.DatabaseUtils;

public class Cidade extends DatabaseUtils {
	private String nome_tabela = "tbl_cidade";

	private Integer id;
	private String nome;
	private Integer idEstado;
	
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

	public Integer getId() {
		return id;
	}

	public Integer getIdEstado() {
		return idEstado;
	}
}
