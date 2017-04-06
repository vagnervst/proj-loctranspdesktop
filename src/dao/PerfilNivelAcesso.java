package dao;

import model.DatabaseUtils;

public class PerfilNivelAcesso extends DatabaseUtils {
	public String nome_tabela = "tbl_perfil_nivel_acesso_juridico";
	
	public int id;
	public String nome;
	public int idUsuario;
	
	@Override
	public String toString() {
		return this.nome;
	}
}
