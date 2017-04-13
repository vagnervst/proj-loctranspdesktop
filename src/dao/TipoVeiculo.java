package dao;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.DatabaseUtils;

public class TipoVeiculo extends DatabaseUtils {
	private String nome_tabela = "tbl_tipoveiculo";
	private int id;
	private String titulo;
	
	public List<Map> getAcessorios() {
        String query = "SELECT a.id, a.nome ";
        query += "FROM acessorioveiculo_tipoveiculo AS av ";
        query += "INNER JOIN tbl_acessorioveiculo AS a ";
        query += "ON a.id = av.idAcessorio ";
        query += "INNER JOIN tbl_tipoveiculo AS t ";
        query += "ON t.id = av.idTipoVeiculo ";
        query += "WHERE t.id = " + this.id;
        
        ResultSet resultado = this.executarQuery(query);
        return this.get_list_from_result_set(resultado, Arrays.asList( "id", "nome" ));
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
