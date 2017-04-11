package dao;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.DatabaseUtils;

public class Funcionario extends DatabaseUtils {
	private String nome_tabela = "tbl_funcionario";
	private int id, idNivelAcesso, idAgencia, statusOnline;
	private String nome, credencial, senha, telefone, celular, email;

	public Funcionario() {

	}

	public Funcionario(String nome, String senha, String telefone, String celular, String email, int idNivelAcesso, int idAgencia) {
		this.nome = nome;
        this.senha = senha;
        this.telefone = telefone;
        this.celular = celular;
        this.email = email;
        this.idNivelAcesso = idNivelAcesso;
        this.idAgencia = idAgencia;
	}

	public List<Map> getFuncionarios() {
		String query = "SELECT f.id, f.nome, na.titulo AS nivelAcesso, a.titulo AS agencia, f.statusOnline ";
		query += "FROM tbl_funcionario AS f ";
		query += "INNER JOIN tbl_nivelacesso_juridico AS na ";
		query += "ON na.id = f.idNivelAcesso ";
		query += "INNER JOIN tbl_agencia AS a ";
		query += "ON a.id = f.idAgencia";

		ResultSet resultado = this.executarQuery( query );
		List<Map> funcionarios = this.get_list_from_result_set(resultado, Arrays.asList( "id", "nome", "nivelAcesso", "agencia", "statusOnline" ) );
		return funcionarios;
	}
	
	public List<Funcionario> getFuncionarios(int idEmpresa) {
        String query = "SELECT f.* ";
        query += "FROM tbl_funcionario AS f ";
        query += "INNER JOIN tbl_empresa AS e ";
        query += "ON e.id = ? ";
        query += "INNER JOIN tbl_agencia AS a ";
        query += "ON a.idEmpresa = e.id"; 
        
        ResultSet resultado = this.executarQuery(query, Arrays.asList( idEmpresa ));
        List<Funcionario> lista_funcionarios = this.get_list_from_result_set(resultado, Funcionario.class);
        
        return lista_funcionarios;
	}
	
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
	public int getIdNivelAcesso() {
		return idNivelAcesso;
	}
	public void setIdNivelAcesso(int idNivelAcesso) {
		this.idNivelAcesso = idNivelAcesso;
	}
	public int getIdAgencia() {
		return idAgencia;
	}
	public void setIdAgencia(int idAgencia) {
		this.idAgencia = idAgencia;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatusOnline() {
		return statusOnline;
	}

	public void setStatusOnline(int statusOnline) {
		this.statusOnline = statusOnline;
	}

	public String getCredencial() {
		return credencial;
	}

	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}


}
