package dao;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import model.CustomCallable;
import model.DatabaseUtils;
import model.Login;
import model.ThreadTask;

public class Funcionario extends DatabaseUtils {
	private String nome_tabela = "tbl_funcionario";
	private Integer id, idNivelAcesso, idAgencia, idEmpresa;
	Integer statusOnline;
	private String nome, credencial, senha, telefone, celular, email;

	public Funcionario() {

	}

	public Funcionario(String nome, String senha, String credencial, String telefone, String celular, String email, Integer idNivelAcesso, Integer idAgencia, Integer idEmpresa) {
		this.nome = nome;
        this.senha = senha;
        this.credencial = credencial;
        this.telefone = telefone;
        this.celular = celular;
        this.email = email;
        this.idNivelAcesso = idNivelAcesso;
        this.idAgencia = idAgencia;
        this.idEmpresa = idEmpresa;
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
        
        ResultSet resultado = this.executarQuery(query, Arrays.asList( idEmpresa ));
        List<Funcionario> lista_funcionarios = this.get_list_from_result_set(resultado, Funcionario.class);
        
        return lista_funcionarios;
	}
	
	public void getPermissoes( CustomCallable<List<Map>> callable ) {
					
		ThreadTask<Funcionario> info_funcionario = new ThreadTask<Funcionario>( new Callable<Funcionario>() {
			
			@Override
			public Funcionario call() throws Exception {
				// TODO Auto-generated method stub											
				return new Funcionario().buscar("id = ?", Arrays.asList( id ), Funcionario.class).get(0);
			}
			
		}, new CustomCallable<Funcionario>() {
			
			@Override
			public Funcionario call() throws Exception {
				// TODO Auto-generated method stub
				Funcionario funcionario_encontrado = (Funcionario) getParametro();
				
				ThreadTask<List<Map>> telas_permitidas = new ThreadTask<List<Map>>( new Callable<List<Map>>() {
					
					@Override
					public List<Map> call() throws Exception {
						// TODO Auto-generated method stub
						NivelAcessoJuridico nivel_acesso_funcionario = new NivelAcessoJuridico();
						nivel_acesso_funcionario.setId( funcionario_encontrado.getIdNivelAcesso() );
						
						return nivel_acesso_funcionario.getTelasPermitidas();
					}
					
				}, callable);
				
				telas_permitidas.run();
				
				return super.call();
			}
			
		});
		
		info_funcionario.run();	
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
	public Integer getIdNivelAcesso() {
		return idNivelAcesso;
	}
	public void setIdNivelAcesso(Integer idNivelAcesso) {
		this.idNivelAcesso = idNivelAcesso;
	}
	public Integer getIdAgencia() {
		return idAgencia;
	}
	public void setIdAgencia(Integer idAgencia) {
		this.idAgencia = idAgencia;
	}
	public String getNome() {
		return this.nome;
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

	public Integer getStatusOnline() {
		return statusOnline;
	}

	public void setStatusOnline(Integer statusOnline) {
		this.statusOnline = statusOnline;
	}

	public String getCredencial() {
		return credencial;
	}

	public void setCredencial(String credencial) {
		this.credencial = credencial;
	}

	public Integer getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}


}
