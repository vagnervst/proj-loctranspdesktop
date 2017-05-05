package dao;

import java.sql.Timestamp;

import model.DatabaseUtils;

public class Avaliacao extends DatabaseUtils {
	private String nome_tabela = "tbl_avaliacao";
	
	private Integer id, nota, idUsuarioAvaliador, idUsuarioAvaliado;
	private Timestamp data;
	private String mensagem;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNota() {
		return nota;
	}
	public void setNota(Integer nota) {
		this.nota = nota;
	}
	public Integer getIdUsuarioAvaliador() {
		return idUsuarioAvaliador;
	}
	public void setIdUsuarioAvaliador(Integer idUsuarioAvaliador) {
		this.idUsuarioAvaliador = idUsuarioAvaliador;
	}
	public Integer getIdUsuarioAvaliado() {
		return idUsuarioAvaliado;
	}
	public void setIdUsuarioAvaliado(Integer idUsuarioAvaliado) {
		this.idUsuarioAvaliado = idUsuarioAvaliado;
	}
	public Timestamp getData() {
		return data;
	}
	public void setData(Timestamp data) {
		this.data = data;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	
}
