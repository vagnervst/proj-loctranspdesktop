package dao;

import java.sql.ResultSet;
import java.util.List;

import model.DatabaseUtils;

public class FabricanteVeiculo extends DatabaseUtils {
	private String nome_tabela = "tbl_fabricanteveiculo";
	private int id;
	private String nome;
	
	public List<FabricanteVeiculo> getFabricantes(long idTipoVeiculo) {
        String query = "SELECT f.id, f.nome ";
        query += "FROM fabricanteveiculo_tipoveiculo AS ft ";
        query += "INNER JOIN tbl_fabricanteveiculo AS f ";
        query += "ON f.id = ft.idFabricante ";
        query += "INNER JOIN tbl_tipoveiculo AS t ";
        query += "ON t.id = ft.idTipo ";
        query += "WHERE t.id = " + idTipoVeiculo;
        
        ResultSet resultado = this.executarQuery(query);
        return this.get_list_from_result_set(resultado, FabricanteVeiculo.class);
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nome;
	}
}
