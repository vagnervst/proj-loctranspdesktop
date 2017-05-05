package dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.DatabaseUtils;

public class Pedido extends DatabaseUtils {
	private String nome_tabela = "tbl_pedido";
	
	private Integer id; 
	private BigDecimal valorDiaria, valorCombustivel, valorQuilometragem, combustivelRestante;
	private Boolean localRetiradaLocador, localDevolucaoLocador, localRetiradaLocatario, localDevolucaoLocatario;
	private Boolean solicitacaoRetiradaLocador, solicitacaoDevolucaoLocador, solicitacaoRetiradaLocatario, solicitacaoDevolucaoLocatario;
	private Integer quilometragemExcedida;
	private Integer idPublicacao, idUsuarioLocador, idUsuarioLocatario, idStatusPedido, idTipoPedido, idFormaPagamento, idFormaPagamentoPendencias, idFuncionario, idCnh, idVeiculo, idAgencia;
	private Timestamp dataRetirada, dataEntrega, dataEntregaEfetuada;
		
	public List<Map> listar_pedidos() {
		String query = "SELECT p.id, v.nome AS modeloVeiculo, a.titulo AS tituloAgencia, s.titulo AS statusPedido ";
		query += "FROM tbl_pedido AS p ";
		query += "INNER JOIN tbl_veiculo AS v ";
		query += "ON v.id = p.idVeiculo ";
		query += "LEFT JOIN tbl_agencia AS a ";
		query += "ON a.id = p.idAgencia ";
		query += "INNER JOIN tbl_statuspedido AS s ";
		query += "ON s.id = p.idStatusPedido";
		
		ResultSet resultado = this.executarQuery(query);
		return this.get_list_from_result_set(resultado, Arrays.asList( "id", "modeloVeiculo", "tituloAgencia", "statusPedido" ));		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean getLocalRetiradaLocador() {
		return localRetiradaLocador;
	}
	public void setLocalRetiradaLocador(boolean localRetiradaLocador) {
		this.localRetiradaLocador = localRetiradaLocador;
	}
	public boolean getLocalDevolucaoLocador() {
		return localDevolucaoLocador;
	}
	public void setLocalDevolucaoLocador(boolean localDevolucaoLocador) {
		this.localDevolucaoLocador = localDevolucaoLocador;
	}
	public boolean getLocalRetiradaLocatario() {
		return localRetiradaLocatario;
	}
	public void setLocalRetiradaLocatario(boolean localRetiradaLocatario) {
		this.localRetiradaLocatario = localRetiradaLocatario;
	}
	public boolean getLocalDevolucaoLocatario() {
		return localDevolucaoLocatario;
	}
	public void setLocalDevolucaoLocatario(boolean localDevolucaoLocatario) {
		this.localDevolucaoLocatario = localDevolucaoLocatario;
	}
	public boolean getSolicitacaoRetiradaLocador() {
		return solicitacaoRetiradaLocador;
	}
	public void setSolicitacaoRetiradaLocador(boolean solicitacaoRetiradaLocador) {
		this.solicitacaoRetiradaLocador = solicitacaoRetiradaLocador;
	}
	public boolean getSolicitacaoDevolucaoLocador() {
		return solicitacaoDevolucaoLocador;
	}
	public void setSolicitacaoDevolucaoLocador(boolean solicitacaoDevolucaoLocador) {
		this.solicitacaoDevolucaoLocador = solicitacaoDevolucaoLocador;
	}
	public boolean getSolicitacaoRetiradaLocatario() {
		return solicitacaoRetiradaLocatario;
	}
	public void setSolicitacaoRetiradaLocatario(boolean solicitacaoRetiradaLocatario) {
		this.solicitacaoRetiradaLocatario = solicitacaoRetiradaLocatario;
	}
	public boolean getSolicitacaoDevolucaoLocatario() {
		return solicitacaoDevolucaoLocatario;
	}
	public void setSolicitacaoDevolucaoLocatario(boolean solicitacaoDevolucaoLocatario) {
		this.solicitacaoDevolucaoLocatario = solicitacaoDevolucaoLocatario;
	}
	public BigDecimal getCombustivelRestante() {
		return combustivelRestante;
	}
	public void setCombustivelRestante(BigDecimal combustivelRestante) {
		this.combustivelRestante = combustivelRestante;
	}
	public Integer getQuilometragemExcedida() {
		return quilometragemExcedida;
	}
	public void setQuilometragemExcedida(Integer quilometragemExcedida) {
		this.quilometragemExcedida = quilometragemExcedida;
	}
	public Integer getIdPublicacao() {
		return idPublicacao;
	}
	public void setIdPublicacao(Integer idPublicacao) {
		this.idPublicacao = idPublicacao;
	}
	public Integer getIdUsuarioLocador() {
		return idUsuarioLocador;
	}
	public void setIdUsuarioLocador(Integer idUsuarioLocador) {
		this.idUsuarioLocador = idUsuarioLocador;
	}
	public Integer getIdUsuarioLocatario() {
		return idUsuarioLocatario;
	}
	public void setIdUsuarioLocatario(Integer idUsuarioLocatario) {
		this.idUsuarioLocatario = idUsuarioLocatario;
	}
	public Integer getIdStatusPedido() {
		return idStatusPedido;
	}
	public void setIdStatusPedido(Integer idStatusPedido) {
		this.idStatusPedido = idStatusPedido;
	}
	public Integer getIdTipoPedido() {
		return idTipoPedido;
	}
	public void setIdTipoPedido(Integer idTipoPedido) {
		this.idTipoPedido = idTipoPedido;
	}
	public Integer getIdFormaPagamento() {
		return idFormaPagamento;
	}
	public void setIdFormaPagamento(Integer idFormaPagamento) {
		this.idFormaPagamento = idFormaPagamento;
	}
	public Integer getIdFormaPagamentoPendencias() {
		return idFormaPagamentoPendencias;
	}
	public void setIdFormaPagamentoPendencias(Integer idFormaPagamentoPendencias) {
		this.idFormaPagamentoPendencias = idFormaPagamentoPendencias;
	}
	public Integer getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Integer idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public Integer getIdCnh() {
		return idCnh;
	}
	public void setIdCnh(Integer idCnh) {
		this.idCnh = idCnh;
	}
	public Integer getIdVeiculo() {
		return idVeiculo;
	}
	public void setIdVeiculo(Integer idVeiculo) {
		this.idVeiculo = idVeiculo;
	}
	public Timestamp getDataRetirada() {
		return dataRetirada;
	}
	public void setDataRetirada(Timestamp dataRetirada) {
		this.dataRetirada = dataRetirada;
	}
	public Timestamp getDataEntrega() {
		return dataEntrega;
	}
	public void setDataEntrega(Timestamp dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
	public Timestamp getDataEntregaEfetuada() {
		return dataEntregaEfetuada;
	}
	public void setDataEntregaEfetuada(Timestamp dataEntregaEfetuada) {
		this.dataEntregaEfetuada = dataEntregaEfetuada;
	}
	public BigDecimal getValorDiaria() {
		return valorDiaria;
	}
	public void setValorDiaria(BigDecimal valorDiaria) {
		this.valorDiaria = valorDiaria;
	}
	public BigDecimal getValorCombustivel() {
		return valorCombustivel;
	}
	public void setValorCombustivel(BigDecimal valorCombustivel) {
		this.valorCombustivel = valorCombustivel;
	}
	public BigDecimal getValorQuilometragem() {
		return valorQuilometragem;
	}
	public void setValorQuilometragem(BigDecimal valorQuilometragem) {
		this.valorQuilometragem = valorQuilometragem;
	}

	public Integer getIdAgencia() {
		return idAgencia;
	}

	public void setIdAgencia(Integer idAgencia) {
		this.idAgencia = idAgencia;
	}		
}
