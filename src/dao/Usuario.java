package dao;

import java.math.BigDecimal;

import model.DatabaseUtils;

public class Usuario extends DatabaseUtils {
	String nome_tabela = "tbl_usuario";
	
	public int id;
	public String nome, sobrenome, cnpj, telefone, celular, email, senha, fotoPerfil;
	public BigDecimal saldo;
}
