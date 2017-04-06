package dao;

import model.DatabaseUtils;

public class Agencia extends DatabaseUtils {
	private final String nome_tabela = "tbl_agencia";
	
	private int id, idCidade, idUsuario;
	private String titulo, telefone, email, endereco;
	
	public Agencia(String titulo, String telefone, String email, String endereco, int idCidade, int idUsuario) {
		this.titulo = titulo;
		this.telefone = telefone;
		this.email = email;
		this.endereco = endereco;
		this.idCidade = idCidade;
		this.idUsuario = idUsuario;
	}
	
	public int getIdCidade() {
		return idCidade;
	}
	
	public void setIdCidade(int idCidade) {
		this.idCidade = idCidade;
	}
	
	public int getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public int getId() {
		return id;
	}
	
	
}
