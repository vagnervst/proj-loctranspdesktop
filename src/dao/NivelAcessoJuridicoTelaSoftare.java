package dao;

import model.DatabaseUtils;

public class NivelAcessoJuridicoTelaSoftare extends DatabaseUtils {
	private String nome_tabela = "nivel_acesso_juridico_tela_software";
	private Integer idNivelAcessoJuridico;
	private Integer idPermissaoJuridico;
	private Integer idTelaSoftware;
	
	public String getNome_tabela() {
		return nome_tabela;
	}
	public void setNome_tabela(String nome_tabela) {
		this.nome_tabela = nome_tabela;
	}
	public long getIdNivelAcessoJuridico() {
		return idNivelAcessoJuridico;
	}
	public void setIdNivelAcessoJuridico(Integer idNivelAcessoJuridico) {
		this.idNivelAcessoJuridico = idNivelAcessoJuridico;
	}
	public long getIdPermissaoJuridico() {
		return idPermissaoJuridico;
	}
	public void setIdPermissaoJuridico(Integer idPermissaoJuridico) {
		this.idPermissaoJuridico = idPermissaoJuridico;
	}
	public long getIdTelaSoftware() {
		return idTelaSoftware;
	}
	public void setIdTelaSoftware(Integer idTelaSoftware) {
		this.idTelaSoftware = idTelaSoftware;
	}
	
	
}
