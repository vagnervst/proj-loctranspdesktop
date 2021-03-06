package dao;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.DatabaseUtils;

public class Agencia extends DatabaseUtils {
	private final String nome_tabela = "tbl_agencia";
	
	private Integer id, idCidade, idEmpresa;
	private String titulo, telefone, email, endereco;
	
	public Agencia() {
		
	}
	
	public Agencia(String titulo, String telefone, String email, String endereco, Integer idCidade, Integer idEmpresa) {
		this.titulo = titulo;
		this.telefone = telefone;
		this.email = email;
		this.endereco = endereco;
		this.idCidade = idCidade;		
		this.idEmpresa = idEmpresa;
	}
	
	public List<Map> getAgencias() {
		String query = "SELECT a.id, a.titulo, e.nome AS estado, c.nome AS cidade, "; 
		query += "(SELECT COUNT(id) FROM tbl_funcionario WHERE idAgencia = a.id) AS funcionarios, "; 
		query += "(SELECT COUNT(id) FROM tbl_publicacao WHERE idAgencia = a.id) AS veiculos ";
		query += "FROM tbl_agencia AS a ";
		query += "INNER JOIN tbl_cidade AS c ";
		query += "ON c.id = a.idCidade ";
		query += "INNER JOIN tbl_estado AS e ";
		query += "ON e.id = c.idEstado";
		
		ResultSet resultados = this.executarQuery(query);
		List<Map> agencias = this.get_list_from_result_set(resultados, Arrays.asList( "id", "titulo", "estado", "cidade", "funcionarios", "veiculos" ) );
		
		return agencias;
	}
	
	public Integer getIdCidade() {
		return idCidade;
	}
	
	public void setIdCidade(Integer idCidade) {
		this.idCidade = idCidade;
	}
	
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
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
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.titulo;
	}
}
