package model;

public class Login {
	public static final int JURIDICO = 1, FUNCIONARIO = 2;

	private static int tipo_conta;
	private static int id_usuario;

	public static void set_id_usuario(int id, int tipoConta) {
		id_usuario = id;
		tipo_conta = tipoConta;
	}

	public static int get_id_usuario() {
		return id_usuario;
	}

	public static int get_tipo_conta() {
		return tipo_conta;
	}
}
