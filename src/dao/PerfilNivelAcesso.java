package dao;

import java.util.Arrays;
import java.util.List;

import model.DatabaseUtils;

public class PerfilNivelAcesso extends DatabaseUtils {
	private String nome_tabela = "tbl_perfil_nivel_acesso_juridico";	
	private int id;
	private String nome;
	private int idUsuario;
	
	public List<NivelAcessoJuridico> getNiveis() {
		return new NivelAcessoJuridico().buscar("idPerfilNivelAcesso = ?", Arrays.asList( this.id ), NivelAcessoJuridico.class);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	@Override
	public String toString() {
		return this.nome;
	}
}
