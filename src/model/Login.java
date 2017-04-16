package model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Login {
	public static final int JURIDICO = 1, FUNCIONARIO = 2;

	private static int tipo_conta;
	private static Integer id_usuario;

	public static void set_id_usuario(Integer id, int tipoConta) {
		id_usuario = id;
		tipo_conta = tipoConta;
	}

	public static Integer get_id_usuario() {
		return id_usuario;
	}

	public static int get_tipo_conta() {
		return tipo_conta;
	}

	public static void get_id_juridico(CustomCallable callable) {

        ThreadTask task = null;
        if( tipo_conta == JURIDICO ) {

        	task = new ThreadTask<Integer>(new Callable<Integer>(){

				@Override
				public Integer call() throws Exception {
					// TODO Auto-generated method stub
					return id_usuario;
				}
			}, callable);

		} else {

            task = new ThreadTask<List<Map>>(new Callable<List<Map>>(){

				@Override
				public List<Map> call() throws Exception {
					// TODO Auto-generated method stub
					String query = "SELECT e.idUsuarioJuridico ";
		            query += "FROM tbl_funcionario AS f ";
		            query += "INNER JOIN tbl_agencia AS a ";
		            query += "ON a.id = f.idAgencia ";
		            query += "INNER JOIN tbl_empresa AS e ";
		            query += "ON e.id = a.idEmpresa ";
		            query += "WHERE f.id = " + id_usuario;

		            DatabaseUtils db = new DatabaseUtils();

					return db.get_list_from_result_set( db.executarQuery(query), Arrays.asList("idUsuarioJuridico"));
				}
			}, callable);

		}
        
        task.run();
	}

	public static void get_id_empresa(CustomCallable callable) {
		ThreadTask<Long> task = null;
		if( tipo_conta == JURIDICO ) {
			
            task = new ThreadTask<Long>(new Callable<Long>() {

				@Override
				public Long call() throws Exception {
					// TODO Auto-generated method stub
					String query = "SELECT e.id AS idEmpresa ";
		            query += "FROM tbl_empresa AS e ";
		            query += "INNER JOIN tbl_usuario AS u ";
		            query += "ON u.id = e.idUsuarioJuridico ";
		            query += "WHERE u.id = " + id_usuario;		            
		            
					DatabaseUtils db = new DatabaseUtils();
					
					return Long.valueOf( String.valueOf( db.get_list_from_result_set(db.executarQuery(query), Arrays.asList("idEmpresa")).get(0).get("idEmpresa") ) );
				}

            }, callable);

		} else {

			task = new ThreadTask<Long>(new Callable<Long>() {

				@Override
				public Long call() throws Exception {
					// TODO Auto-generated method stub
                    String query = "SELECT f.idEmpresa ";
                    query += "FROM tbl_funcionario AS f ";                    
                    query += "WHERE f.id = " + id_usuario;

					DatabaseUtils db = new DatabaseUtils();
					return Long.valueOf( String.valueOf( db.get_list_from_result_set(db.executarQuery(query), Arrays.asList("idEmpresa")).get(0).get("idEmpresa") ) );
				}

            }, callable);					
		}
		
		task.run();
	}
}
