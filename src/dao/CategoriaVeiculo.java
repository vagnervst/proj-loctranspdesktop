package dao;

import java.math.BigDecimal;

import model.DatabaseUtils;

public class CategoriaVeiculo extends DatabaseUtils {
	private String nome_tabela = "tbl_categoriaveiculo";
	private int id, idTipoVeiculo;
	private BigDecimal percentualLucro, valorMinimoVeiculo;
	private String nome;
	
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
	public int getIdTipoVeiculo() {
		return idTipoVeiculo;
	}
	public void setIdTipoVeiculo(int idTipoVeiculo) {
		this.idTipoVeiculo = idTipoVeiculo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}	
	public BigDecimal getPercentualLucro() {
		return percentualLucro;
	}
	public void setPercentualLucro(BigDecimal percentualLucro) {
		this.percentualLucro = percentualLucro;
	}
	public BigDecimal getValorMinimoVeiculo() {
		return valorMinimoVeiculo;
	}
	public void setValorMinimoVeiculo(BigDecimal valorMinimoVeiculo) {
		this.valorMinimoVeiculo = valorMinimoVeiculo;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nome;
	}
}
