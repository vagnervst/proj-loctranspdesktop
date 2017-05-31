package dao;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DatabaseUtils;
import model.Server;

public class Publicacao extends DatabaseUtils {
	private String nome_tabela = "tbl_publicacao";

	private String titulo, descricao, imagemPrincipal, imagemA, imagemB, imagemC, imagemD;
	private Integer id, idStatusPublicacao, idAgencia, idUsuario, idFuncionario, idVeiculo;
	private Integer quilometragemAtual, limiteQuilometragem;
	private Boolean disponivelOnline;
	private BigDecimal valorDiaria, valorCombustivel, valorQuilometragem, valorVeiculo;
	private Timestamp dataPublicacao;

    public List<Map> getPublicacoes(String where) {
        String query = "SELECT p.id, p.titulo, t.titulo AS tipoVeiculo, p.valorDiaria, p.valorCombustivel, (SELECT COUNT(idPublicacao) FROM tbl_pedido WHERE idPublicacao = p.id) AS locacoes, st.titulo AS statusPublicacao ";
        query += "FROM tbl_publicacao AS p ";
        query += "INNER JOIN tbl_veiculo AS v ";
        query += "ON v.id = p.idVeiculo ";
        query += "INNER JOIN tbl_tipoveiculo AS t ";
        query += "ON t.id = v.idTipoVeiculo ";
        query += "INNER JOIN tbl_statuspublicacao AS st ";
        query += "ON st.id = p.idStatusPublicacao ";
        query += "INNER JOIN tbl_agencia AS a ";
        query += "ON a.id = p.idAgencia";

        if( where != null ) {
        	query += " WHERE " + where;
        }

        ResultSet resultado = this.executarQuery(query);
        return this.get_list_from_result_set(resultado, Arrays.asList( "id", "titulo", "tipoVeiculo", "valorDiaria", "valorCombustivel", "locacoes", "statusPublicacao"));
    }

	public boolean eliminar_relacionamentos_a_acessorios() {
		String query = "DELETE FROM publicacao_acessorioveiculo ";
		query += "WHERE idPublicacao = " + this.id;

		return this.executarQueryAlteracao(query);
	}

	public boolean relacionar_a_acessorio(Integer idAcessorio) {
		String query = "INSERT INTO publicacao_acessorioveiculo(idPublicacao, idAcessorio) ";
		query += "VALUES(" + this.id + ", " + idAcessorio + ")";

		return this.executarQueryAlteracao(query);
	}

	public List<Integer> get_id_acessorios_relacionados() {
		List<Integer> acessorios = new ArrayList<>();

		String query = "SELECT idPublicacao, idAcessorio ";
		query += "FROM publicacao_acessorioveiculo ";
		query += "WHERE idPublicacao = " + this.id;

		List<Map> acessorios_encontrados = this.get_list_from_result_set( this.executarQuery(query), Arrays.asList( "idPublicacao", "idAcessorio" ));

		for( int i = 0; i < acessorios_encontrados.size(); ++i ) {
			Integer id_acessorio = (Integer) acessorios_encontrados.get(i).get("idAcessorio");

			acessorios.add( id_acessorio );
		}

		return acessorios;
	}

	private File download_imagem(String url) {
		File imagem = new File(url);

		return imagem;
	}

	public Map<String, File> get_imagens() {

		String path = Server.address + "img/uploads/publicacoes/";
		Map<String, File> lista_imagens = new HashMap<String, File>();

		File imagem_principal = download_imagem( path + this.imagemPrincipal );

		File imagem_a = download_imagem( path + this.imagemA  );
		File imagem_b = download_imagem( path + this.imagemB  );
		File imagem_c = download_imagem( path + this.imagemC  );
		File imagem_d = download_imagem( path + this.imagemD  );

		lista_imagens.put("imagemPrincipal", imagem_principal);
        lista_imagens.put("imagemA", imagem_a);
        lista_imagens.put("imagemB", imagem_b);
        lista_imagens.put("imagemC", imagem_c);
        lista_imagens.put("imagemD", imagem_d);

		return lista_imagens;
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
	public Integer getQuilometragemAtual() {
		return quilometragemAtual;
	}
	public void setQuilometragemAtual(int quilometragemAtual) {
		this.quilometragemAtual = quilometragemAtual;
	}
	public Integer getLimiteQuilometragem() {
		return limiteQuilometragem;
	}
	public void setLimiteQuilometragem(int limiteQuilometragem) {
		this.limiteQuilometragem = limiteQuilometragem;
	}
	public Integer getIdStatusPublicacao() {
		return idStatusPublicacao;
	}
	public void setIdStatusPublicacao(Integer idStatusPublicacao) {
		this.idStatusPublicacao = idStatusPublicacao;
	}
	public Integer getIdAgencia() {
		return idAgencia;
	}
	public void setIdAgencia(Integer idAgencia) {
		this.idAgencia = idAgencia;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Integer getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Integer idFuncionario) {
		this.idFuncionario = idFuncionario;
	}
	public Integer getIdVeiculo() {
		return idVeiculo;
	}
	public void setIdVeiculo(Integer idVeiculo) {
		this.idVeiculo = idVeiculo;
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
	public BigDecimal getPrecoMedio() {
		return valorVeiculo;
	}
	public void setPrecoMedio(BigDecimal precoMedio) {
		this.valorVeiculo = precoMedio;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getDisponivelOnline() {
		return disponivelOnline;
	}

	public void setDisponivelOnline(Boolean disponivelOnline) {
		this.disponivelOnline = disponivelOnline;
	}

	public Timestamp getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Timestamp dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getImagemPrincipal() {
		return imagemPrincipal;
	}

	public void setImagemPrincipal(String imagemPrincipal) {
		this.imagemPrincipal = imagemPrincipal;
	}

	public String getImagemA() {
		return imagemA;
	}

	public void setImagemA(String imagemA) {
		this.imagemA = imagemA;
	}

	public String getImagemB() {
		return imagemB;
	}

	public void setImagemB(String imagemB) {
		this.imagemB = imagemB;
	}

	public String getImagemC() {
		return imagemC;
	}

	public void setImagemC(String imagemC) {
		this.imagemC = imagemC;
	}

	public String getImagemD() {
		return imagemD;
	}

	public void setImagemD(String imagemD) {
		this.imagemD = imagemD;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("%d - %s", this.id, this.titulo);
	}
}
