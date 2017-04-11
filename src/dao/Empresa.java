package dao;

import model.DatabaseUtils;

public class Empresa extends DatabaseUtils {
	private String nome_tabela = "tbl_empresa";
	private int id, idUsuarioJuridico;
	private String razaoSocial, nomeFantasia, cnpj, logomarca;
	
	public String getNome_tabela() {
		return nome_tabela;
	}
	public void setNome_tabela(String nome_tabela) {
		this.nome_tabela = nome_tabela;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdUsuarioJuridico() {
		return idUsuarioJuridico;
	}
	public void setIdUsuarioJuridico(int idUsuarioJuridico) {
		this.idUsuarioJuridico = idUsuarioJuridico;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getLogomarca() {
		return logomarca;
	}
	public void setLogomarca(String logomarca) {
		this.logomarca = logomarca;
	}
	
	
}
