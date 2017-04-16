package dao;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.DatabaseUtils;

public class NivelAcessoJuridico extends DatabaseUtils {
	private String nome_tabela = "tbl_nivelacesso_juridico";
	private Integer id, idUsuario;
	private String titulo;
	
	public List<Map> getNiveisAcesso() {
        String query = "SELECT n.id, n.titulo, (SELECT COUNT(f.id) FROM tbl_funcionario AS f WHERE f.idNivelAcesso = n.id) AS qtdFuncionarios ";
        query += "FROM tbl_nivelacesso_juridico AS n";
        
        ResultSet resultado = this.executarQuery(query);
        List<Map> lista_niveis_acesso = this.get_list_from_result_set(resultado, Arrays.asList( "id", "titulo", "qtdFuncionarios" ));
        System.out.println(lista_niveis_acesso);
        return lista_niveis_acesso;
	}
	
	public List<Map> getTelasPermitidas() {
        String query = "SELECT t.id, t.titulo, ";
        query += "( SELECT CASE WHEN nt2.idPermissaoJuridico = 1 THEN 1 ELSE 0 END FROM nivel_acesso_juridico_tela_software AS nt2 WHERE nt2.idNivelAcessoJuridico = n.id AND nt2.idPermissaoJuridico = 1 AND nt2.idTelaSoftware = t.id ) AS leitura, ";
        query += "( SELECT CASE WHEN nt2.idPermissaoJuridico = 2 THEN 1 ELSE 0 END FROM nivel_acesso_juridico_tela_software AS nt2 WHERE nt2.idNivelAcessoJuridico = n.id AND nt2.idPermissaoJuridico = 2 AND nt2.idTelaSoftware = t.id ) AS escrita, ";
        query += "( SELECT CASE WHEN nt2.idPermissaoJuridico = 3 THEN 1 ELSE 0 END FROM nivel_acesso_juridico_tela_software AS nt2 WHERE nt2.idNivelAcessoJuridico = n.id AND nt2.idPermissaoJuridico = 3 AND nt2.idTelaSoftware = t.id ) AS edicao, ";
        query += "( SELECT CASE WHEN nt2.idPermissaoJuridico = 4 THEN 1 ELSE 0 END FROM nivel_acesso_juridico_tela_software AS nt2 WHERE nt2.idNivelAcessoJuridico = n.id AND nt2.idPermissaoJuridico = 4 AND nt2.idTelaSoftware = t.id ) AS remocao ";
        query += "FROM tbl_tela_software AS t ";
        query += "INNER JOIN nivel_acesso_juridico_tela_software AS nt ";
        query += "ON nt.idTelaSoftware = t.id ";
        query += "INNER JOIN tbl_nivelacesso_juridico AS n ";
        query += "ON n.id = nt.idNivelAcessoJuridico ";
        query += "WHERE n.id = " + this.id + " ";
        query += "GROUP BY t.id";
        
        ResultSet resultado = this.executarQuery(query);
        List<Map> telas = this.get_list_from_result_set(resultado, Arrays.asList( "id", "titulo", "leitura", "escrita", "edicao", "remocao" ));
        
        return telas;
	}
	
	public long relacionar_a_tela(int idTela, int idPermissao) {
		NivelAcessoJuridicoTelaSoftare relacionamento = new NivelAcessoJuridicoTelaSoftare();
		
		relacionamento.setIdNivelAcessoJuridico( this.id );
		relacionamento.setIdTelaSoftware( idTela );
		relacionamento.setIdPermissaoJuridico( idPermissao );
		
		return relacionamento.inserir();
	}
	
	public boolean limpar_relacionamentos_a_telas() {
		String query = "DELETE FROM nivel_acesso_juridico_tela_software ";
		query += "WHERE idNivelAcessoJuridico = " + this.id;
		
		return this.executarQueryAlteracao( query );
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}	
	public String getNome_tabela() {
		return nome_tabela;
	}
	public void setNome_tabela(String nome_tabela) {
		this.nome_tabela = nome_tabela;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.titulo;
	}
}
