package dao;

import java.sql.Timestamp;

import model.DatabaseUtils;

public class CartaoCredito extends DatabaseUtils {
	private String nome_tabela = "tbl_cartao_credito";
	
	private Integer id, codigoSeguranca, idUsuario, idTipo;
	private String numero;
	private Timestamp vencimento;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoSeguranca() {
		return codigoSeguranca;
	}
	public void setCodigoSeguranca(Integer codigoSeguranca) {
		this.codigoSeguranca = codigoSeguranca;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Timestamp getVencimento() {
		return vencimento;
	}
	public void setVencimento(Timestamp vencimento) {
		this.vencimento = vencimento;
	}
	
	
}
