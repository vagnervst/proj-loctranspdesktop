package dao;

import model.DatabaseUtils;

public class TipoCartaoCredito extends DatabaseUtils {
	private String nome_tabela = "tbl_tipo_cartao_credito";
	
	private Integer id, qtdDigitosSeguranca;
	private String titulo;
	private Boolean visivel;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQtdDigitosSeguranca() {
		return qtdDigitosSeguranca;
	}
	public void setQtdDigitosSeguranca(Integer qtdDigitosSeguranca) {
		this.qtdDigitosSeguranca = qtdDigitosSeguranca;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Boolean getVisivel() {
		return visivel;
	}
	public void setVisivel(Boolean visivel) {
		this.visivel = visivel;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.titulo;
	}
}
