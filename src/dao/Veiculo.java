package dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.DatabaseUtils;

public class Veiculo extends DatabaseUtils {
	private String nome_tabela = "tbl_veiculo";
	private Integer id, idCategoriaVeiculo, idFabricante, idTipoCombustivel, idTipoVeiculo, idTransmissao;	
	private Integer ano, qtdPortas;
	private String nome, tipoMotor, codigo;
	private BigDecimal tanque;
	private Boolean visivel;

	public List<Veiculo> getVeiculos(String where) {
        String query = "SELECT v.* ";
        query += "FROM tbl_veiculo AS v ";
        query += "INNER JOIN tbl_tipoveiculo AS tv ";
        query += "ON tv.id = v.idTipoVeiculo ";
        query += "INNER JOIN tbl_categoriaveiculo AS cv ";
        query += "ON cv.id = v.idCategoriaVeiculo ";
        query += "INNER JOIN tbl_fabricanteveiculo AS fv ";
        query += "ON fv.id = v.idFabricante ";
        query += "LEFT JOIN tbl_tipocombustivel AS tc ";
        query += "ON tc.id = v.idTipoCombustivel ";
        query += "LEFT JOIN tbl_transmissaoveiculo AS t ";
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
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getIdCategoriaVeiculo() {
		return idCategoriaVeiculo;
	}

	public void setIdCategoriaVeiculo(Integer idCategoriaVeiculo) {
		this.idCategoriaVeiculo = idCategoriaVeiculo;
	}

	public Integer getIdFabricante() {
		return idFabricante;
	}

	public void setIdFabricante(Integer idFabricante) {
		this.idFabricante = idFabricante;
	}

	public Integer getIdTipoCombustivel() {
		return idTipoCombustivel;
	}

	public void setIdTipoCombustivel(Integer idTipoCombustivel) {
		this.idTipoCombustivel = idTipoCombustivel;
	}

	public Integer getIdTipoVeiculo() {
		return idTipoVeiculo;
	}

	public void setIdTipoVeiculo(Integer idTipoVeiculo) {
		this.idTipoVeiculo = idTipoVeiculo;
	}

	public Integer getIdTransmissao() {
		return idTransmissao;
	}

	public void setIdTransmissao(Integer idTransmissao) {
		this.idTransmissao = idTransmissao;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nome;
	}

	public BigDecimal getTanque() {
		return tanque;
	}

	public void setTanque(BigDecimal tanque) {
		this.tanque = tanque;
	}

	public Boolean getVisivel() {
		return visivel;
	}

	public void setVisivel(Boolean visivel) {
		this.visivel = visivel;
	}
}
