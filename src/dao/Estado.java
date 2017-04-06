package dao;

import model.DatabaseUtils;

public class Estado extends DatabaseUtils {
	String nome_tabela = "tbl_estado";

	public int id;
	public String nome;
	public int idPais;
	
	@Override
	public String toString() {
		return this.nome;
	}
}
