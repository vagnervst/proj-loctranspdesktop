package model;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Relatorios extends DatabaseUtils {

	public List<Map> relatorio_veiculos_mais_publicados( String where ) {
		String query = "SELECT v.nome, COUNT(p.idVeiculo) AS qtd ";
		query += "FROM tbl_publicacao AS p ";
		query += "INNER JOIN tbl_veiculo AS v ";
		query += "ON v.id = p.idVeiculo ";
		query += "INNER JOIN tbl_agencia AS a ";
		query += "ON a.id = p.idAgencia ";
		query += "INNER JOIN tbl_empresa AS e ";
		query += "ON e.id = a.idEmpresa ";
        
		if( where != null ) {
			query += "WHERE " + where;
		}
		
		query += " GROUP BY p.idVeiculo";
		
		ResultSet resultado = this.executarQuery(query);
		List<Map> lista_veiculos = this.get_list_from_result_set(resultado, Arrays.asList( "nome", "qtd" ));
		return lista_veiculos;
	}
	
	public List<Map> relatorio_publicacoes_agencia( String where ) {
        String query = "SELECT a.titulo, COUNT(p.id) AS qtdPublicacoes ";
        query += "FROM tbl_publicacao AS p ";
        query += "INNER JOIN tbl_agencia AS a ";
        query += "ON a.id = p.idAgencia ";
        query += "INNER JOIN tbl_empresa AS e ";
        query += "ON e.id = a.idEmpresa ";
        
        if( where != null ) {
            query += "WHERE " + where + " ";
        }
        
        query += "GROUP BY a.id ";
        
        System.out.println( query );
        
        ResultSet resultado = this.executarQuery(query);
        List<Map> lista_agencias = this.get_list_from_result_set(resultado, Arrays.asList( "titulo", "qtdPublicacoes" ));
        return lista_agencias;
	}
	
	public List<Map> relatorio_publicacoes_funcionario( String where ) {
        String query = "SELECT f.nome, COUNT(p.id) AS qtdPublicacoes ";
        query += "FROM tbl_publicacao AS p ";
        query += "INNER JOIN tbl_funcionario AS f ";
        query += "ON f.id = p.idFuncionario ";
        
        if( where != null ) {
            query += "WHERE " + where + " ";
        }
        
        query += "GROUP BY f.id ";
        
        ResultSet resultado = this.executarQuery(query);
        List<Map> lista_funcionarios = this.get_list_from_result_set(resultado, Arrays.asList("nome", "qtdPublicacoes"));        
        return lista_funcionarios;
	}
	
	public Map relatorio_publicacoes_disponiveis_indisponiveis( String where ) {
        String query = "SELECT ";
        query += "( SELECT COUNT(p.id)  ";
        query += "FROM tbl_publicacao AS p  ";
        query += "INNER JOIN tbl_agencia AS a ";
        query += "ON a.id = p.idAgencia ";
        query += "INNER JOIN tbl_empresa AS e ";
        query += "ON e.id = a.idEmpresa ";
        query += "WHERE p.idStatusPublicacao = 1 ";
        
        if( where != null ) {
            query += "AND " + where + " ";
        }
        
        query += ") AS publicacoesDisponiveis, ";
        query += "( SELECT COUNT(p.id)  ";
        query += "FROM tbl_publicacao AS p  ";
        query += "INNER JOIN tbl_agencia AS a ";
        query += "ON a.id = p.idAgencia ";
        query += "INNER JOIN tbl_empresa AS e ";
        query += "ON e.id = a.idEmpresa ";
        query += "WHERE p.idStatusPublicacao = 2 ";
        
        if( where != null ) {
            query += "AND " + where + " ";
        }
        
        query += ") AS publicacoesIndisponiveis ";
        
        ResultSet resultado = this.executarQuery(query);
        Map lista_publicacoes = this.get_list_from_result_set(resultado, Arrays.asList( "publicacoesDisponiveis", "publicacoesIndisponiveis" )).get(0);
        
        return lista_publicacoes;
	}
	
	public List<Map> relatorio_veiculos_alugados( String where ) {
        String query = "SELECT v.nome, COUNT(v.id) AS qtdAlugados ";
        query += "FROM tbl_pedido AS p ";
        query += "INNER JOIN tbl_publicacao AS pu ";
        query += "ON pu.id = p.idPublicacao ";
        query += "INNER JOIN tbl_agencia AS a ";
        query += "ON a.id = pu.idAgencia ";
        query += "INNER JOIN tbl_empresa AS e ";
        query += "ON e.id = a.idEmpresa ";
        query += "INNER JOIN tbl_veiculo AS v ";
        query += "ON v.id = p.idVeiculo ";
        
        if( where != null ) {
            query += "WHERE " + where + " ";
        }
        
        query += "GROUP BY v.id";
        
        ResultSet resultado = this.executarQuery(query);
        List<Map> lista_veiculos = this.get_list_from_result_set(resultado, Arrays.asList( "nome", "qtdAlugados" ));
        
        return lista_veiculos;
	}
	
	public List<Map> relatorio_locacao_diaria( String where ) {
        String query = "SELECT pu.valorDiaria, COUNT(p.id) AS qtdPedidos ";
        query += "FROM tbl_pedido AS p ";
        query += "INNER JOIN tbl_publicacao AS pu ";
        query += "ON pu.id = p.idPublicacao ";
        query += "INNER JOIN tbl_agencia AS a ";
        query += "ON a.id = pu.idAgencia ";
        query += "INNER JOIN tbl_empresa AS e ";
        query += "ON e.id = a.idEmpresa ";
        
        if( where != null ) {
            query += "WHERE " + where + " ";
        }
        
        query += "GROUP BY pu.valorDiaria";               
        
        ResultSet resultado = this.executarQuery( query );
        List<Map> lista_diarias = this.get_list_from_result_set( resultado, Arrays.asList( "valorDiaria", "qtdPedidos" ) );
        return lista_diarias;
	}
	
}
