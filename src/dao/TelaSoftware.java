package dao;

import model.DatabaseUtils;

public class TelaSoftware extends DatabaseUtils {
	private String nome_tabela = "tbl_tela_software";

	private Integer id;
	private String titulo;
	private boolean leitura, escrita, edicao, remocao;

	public String getNome_tabela() {
		return nome_tabela;
	}
	public void setNome_tabela(String nome_tabela) {
		this.nome_tabela = nome_tabela;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public boolean isLeitura() {
		return leitura;
	}
	public void setLeitura(boolean leitura) {
		this.leitura = leitura;
	}
	public boolean isEscrita() {
		return escrita;
	}
	public void setEscrita(boolean escrita) {
		this.escrita = escrita;
	}
	public boolean isEdicao() {
		return edicao;
	}
	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}
	public boolean isRemocao() {
		return remocao;
	}
	public void setRemocao(boolean remocao) {
		this.remocao = remocao;
	}

	@Override
	public String toString() {
		return this.titulo;
	}
}
