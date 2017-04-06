package dao;

import model.DatabaseUtils;

public class Cidade extends DatabaseUtils {
	String nome_tabela = "tbl_cidade";

	public int id;
	public String nome;
	public int idEstado;
	
	@Override
	public String toString() {
		return this.nome;
	}
}
