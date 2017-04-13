package dao;

import java.sql.ResultSet;
import java.util.List;

import model.DatabaseUtils;

public class CombustivelVeiculo extends DatabaseUtils {
	private String nome_tabela = "tbl_tipocombustivel";
	private int id;
	private String nome;
	
	public List<CombustivelVeiculo> getCombustiveis(int id_tipo_veiculo) {
        String query = "SELECT tc.* ";
        query += "FROM tipoveiculo_tipocombustivel AS tvc ";
        query += "INNER JOIN tbl_tipoveiculo AS tv ";
        query += "ON tv.id = tvc.idTipoVeiculo ";
        query += "INNER JOIN tbl_tipocombustivel AS tc ";
        query += "ON tc.id = tvc.idTipoCombustivel ";
        query += "WHERE tv.id = " + id_tipo_veiculo;
        
        ResultSet resultados = this.executarQuery(query);
        return this.get_list_from_result_set(resultados, CombustivelVeiculo.class);
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
