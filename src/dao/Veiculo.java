package dao;

import java.sql.ResultSet;
import java.util.List;

import model.DatabaseUtils;

public class Veiculo extends DatabaseUtils {
	private String nome_tabela = "tbl_veiculo";
	private Integer id, ano, qtdPortas, idCategoriaVeiculo, idFabricante, idTipoCombustivel, idTipoVeiculo, idTransmissao;
	private String nome, tipoMotor, codigo;
	
	public List<Veiculo> getVeiculos(String where) {
        String query = "SELECT v.* ";        
        query += "FROM tbl_veiculo AS v ";
        query += "INNER JOIN tbl_tipoveiculo AS tv ";
        query += "ON tv.id = v.idTipoVeiculo ";
        query += "INNER JOIN tbl_categoriaveiculo AS cv ";
        query += "ON cv.id = v.idCategoriaVeiculo ";
        query += "INNER JOIN tbl_fabricanteveiculo AS fv ";
        query += "ON fv.id = v.idFabricante ";
        query += "INNER JOIN tbl_tipocombustivel AS tc ";
        query += "ON tc.id = v.idTipoCombustivel ";
        query += "INNER JOIN tbl_transmissaoveiculo AS t ";
        query += "ON t.id = v.idTransmissao";
        
        if( where != null ) {
        	query += " WHERE " + where;
        }
        
        ResultSet resultados = this.executarQuery(query);
        return this.get_list_from_result_set(resultados, Veiculo.class);
	}
	
	public String getNome_tabela() {
		return nome_tabela;
	}
	public void setNome_tabela(String nome_tabela) {
		this.nome_tabela = nome_tabela;
	}	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipoMotor() {
		return tipoMotor;
	}
	public void setTipoMotor(String tipoMotor) {
		this.tipoMotor = tipoMotor;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getQtdPortas() {
		return qtdPortas;
	}

	public void setQtdPortas(int qtdPortas) {
		this.qtdPortas = qtdPortas;
	}

	public int getIdCategoriaVeiculo() {
		return idCategoriaVeiculo;
	}

	public void setIdCategoriaVeiculo(int idCategoriaVeiculo) {
		this.idCategoriaVeiculo = idCategoriaVeiculo;
	}

	public int getIdFabricante() {
		return idFabricante;
	}

	public void setIdFabricante(int idFabricante) {
		this.idFabricante = idFabricante;
	}

	public int getIdTipoCombustivel() {
		return idTipoCombustivel;
	}

	public void setIdTipoCombustivel(int idTipoCombustivel) {
		this.idTipoCombustivel = idTipoCombustivel;
	}

	public int getIdTipoVeiculo() {
		return idTipoVeiculo;
	}

	public void setIdTipoVeiculo(int idTipoVeiculo) {
		this.idTipoVeiculo = idTipoVeiculo;
	}

	public int getIdTransmissao() {
		return idTransmissao;
	}

	public void setIdTransmissao(int idTransmissao) {
		this.idTransmissao = idTransmissao;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nome;
	}
}
