package dao;

import model.DatabaseUtils;

public class CNH extends DatabaseUtils {
	private String nome_tabela = "tbl_cnh";
	private Integer id, numeroRegistro, idUsuario;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNumeroRegistro() {
		return numeroRegistro;
	}
	public void setNumeroRegistro(Integer numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf( this.numeroRegistro );
	}
}
