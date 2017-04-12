package dao;

import model.DatabaseUtils;

public class NivelAcessoJuridicoTelaSoftare extends DatabaseUtils {
	private String nome_tabela = "nivel_acesso_juridico_tela_software";
	private int idNivelAcessoJuridico;
	private int idPermissaoJuridico;
	private int idTelaSoftware;
	
	public String getNome_tabela() {
		return nome_tabela;
	}
	public void setNome_tabela(String nome_tabela) {
		this.nome_tabela = nome_tabela;
	}
	public int getIdNivelAcessoJuridico() {
		return idNivelAcessoJuridico;
	}
	public void setIdNivelAcessoJuridico(int idNivelAcessoJuridico) {
		this.idNivelAcessoJuridico = idNivelAcessoJuridico;
	}
	public int getIdPermissaoJuridico() {
		return idPermissaoJuridico;
	}
	public void setIdPermissaoJuridico(int idPermissaoJuridico) {
		this.idPermissaoJuridico = idPermissaoJuridico;
	}
	public int getIdTelaSoftware() {
		return idTelaSoftware;
	}
	public void setIdTelaSoftware(int idTelaSoftware) {
		this.idTelaSoftware = idTelaSoftware;
	}
	
	
}
