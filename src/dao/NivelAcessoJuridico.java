package dao;

import model.DatabaseUtils;

public class NivelAcessoJuridico extends DatabaseUtils {
	private String nome_tabela = "tbl_nivelacesso_juridico";
	private int id, idPerfilNivelAcesso;
	private String titulo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdPerfilNivelAcesso() {
		return idPerfilNivelAcesso;
	}
	public void setIdPerfilNivelAcesso(int idPerfilNivelAcesso) {
		this.idPerfilNivelAcesso = idPerfilNivelAcesso;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.titulo;
	}
}
