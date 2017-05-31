package dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.DatabaseUtils;

public class Usuario extends DatabaseUtils {
	private String nome_tabela = "tbl_usuario";
	
	private Integer id, idTipoConta, idCidade, idPlanoConta, idLicencaDesktop;
	private String nome, sobrenome, telefone, celular, email, senha, RG, CPF, fotoPerfil;
	private String sexo;
	private BigDecimal saldo;
	private Date dataNascimento;
	
	public List<Map> listar_usuarios(String where, List<Object> parametros) {
		String sql = "SELECT u.nome, u.sobrenome, CONCAT(u.nome, \" \", u.sobrenome) AS nomeCompleto, u.rg, u.cpf, CONCAT(e.nome, \", \", c.nome) AS localizacao ";
		sql += "FROM tbl_usuario AS u ";
		sql += "INNER JOIN tbl_cidade AS c ";
		sql += "ON c.id = u.idCidade ";
		sql += "INNER JOIN tbl_estado AS e ";
		sql += "ON e.id = c.idEstado";
				
		if( where != null ) {
			sql += " WHERE " + where;
		}
		
		System.out.println( sql );
		ResultSet resultado = this.executarQuery(sql, parametros);		
		return this.get_list_from_result_set(resultado, Arrays.asList( "nome", "sobrenome", "nomeCompleto", "rg", "cpf", "localizacao" ));
	}
	
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
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
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getFotoPerfil() {
		return fotoPerfil;
	}
	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public Integer getIdCidade() {
		return idCidade;
	}
	public void setIdCidade(Integer idCidade) {
		this.idCidade = idCidade;
	}
	public Integer getIdTipoConta() {
		return idTipoConta;
	}
	public void setIdTipoConta(Integer idTipoConta) {
		this.idTipoConta = idTipoConta;
	}
	public Integer getIdPlanoConta() {
		return idPlanoConta;
	}
	public void setIdPlanoConta(Integer idPlanoConta) {
		this.idPlanoConta = idPlanoConta;
	}
	public Integer getIdLicencaDesktop() {
		return idLicencaDesktop;
	}
	public void setIdLicencaDesktop(Integer idLicencaDesktop) {
		this.idLicencaDesktop = idLicencaDesktop;
	}
	public String getRG() {
		return RG;
	}
	public void setRG(String rG) {
		RG = rG;
	}
	public String getCPF() {
		return CPF;
	}
	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
}
