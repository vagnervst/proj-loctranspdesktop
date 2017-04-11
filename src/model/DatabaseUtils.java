package model;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseUtils {
	String nome_tabela;

	Map<Class<?>, Integer> relacao_tipos_sql_java;

	public DatabaseUtils() {
		relacao_tipos_sql_java = new HashMap<>();

		relacao_tipos_sql_java.put(String.class, Types.VARCHAR);
		relacao_tipos_sql_java.put(Integer.class, Types.INTEGER);
		relacao_tipos_sql_java.put(BigDecimal.class, Types.DECIMAL);
		relacao_tipos_sql_java.put(Double.class, Types.DECIMAL);
		relacao_tipos_sql_java.put(java.sql.Date.class, Types.DATE);
		relacao_tipos_sql_java.put(java.util.Date.class, Types.DATE);
	}

	public <T extends DatabaseUtils> DatabaseUtils getInstanceOfT(Class<? extends DatabaseUtils> class1) {
		try {
			return class1.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	protected Map<String, Object> get_campos_valor( boolean incluir_id ) {
		Map<String, Object> array_keys = new HashMap<String, Object>();

		for (Field campo : this.getClass().getDeclaredFields()) {
			campo.setAccessible(true);

			try {
				Object valor = campo.get(this);

				if (campo.getType().getClass().isInstance(valor)) {
					valor = campo.getType().getClass();
				}

				if (!campo.getName().equals("nome_tabela")) {										
					
					if( campo.getName().equals("id") ) {
						
						if( incluir_id )
							array_keys.put(campo.getName(), valor);
					} else {
						array_keys.put(campo.getName(), valor);
					}
				}

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return array_keys;
	}

	protected void set_campo_valor(DatabaseUtils objeto_alvo, String nome_campo, Object valor) {
		for (Field campo : this.getClass().getDeclaredFields()) {
			campo.setAccessible(true);

			if (campo.getName().equals(nome_campo)) {
				try {
					campo.set(objeto_alvo, valor);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	protected Set<String> get_campos(boolean incluir_id) {
		return this.get_campos_valor(incluir_id).keySet();
	}

	protected List<Object> get_valores(Map<String, Object> lista_campo_valor) {
		List<Object> valores = new ArrayList<>();

		for (Map.Entry<String, Object> entry : lista_campo_valor.entrySet()) {
			valores.add(entry.getValue());
		}

		return valores;
	}

	protected Class<? extends Object> get_tipo_campo(Object campo) {
		return campo.getClass();
	}

	protected String get_nome_tabela() {

		String nome_tabela = null;
		for (Field campo : this.getClass().getDeclaredFields()) {
			campo.setAccessible(true);

			try {
				Object valor = campo.get(this);

				if (campo.getName().equals("nome_tabela"))
					nome_tabela = valor.toString();

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return nome_tabela;
	}

	protected boolean is_decimal(Object t) {
		if (get_tipo_sql_correspondente(t.getClass()) == Types.DECIMAL)
			return true;

		return false;
	}

	protected boolean is_text(Object t) {

		if (get_tipo_sql_correspondente(t.getClass()) == Types.VARCHAR) {
			return true;
		}

		return false;
	}

	protected boolean is_integer(Object t) {
		if (get_tipo_sql_correspondente(t.getClass()) == Types.INTEGER)
			return true;

		return false;
	}

	protected String preparar_valor(Object t) {
		String resultado = "";
		if (t == null)
			return resultado;

		if (is_decimal(t) || is_text(t))
			resultado = "'" + t.toString() + "'";
		else
			resultado = t.toString();

		return resultado;
	}
	
	protected ResultSet resultado_from_statement(PreparedStatement statement) {		
		
		ResultSet resultado = null;
		try {
			resultado = statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	protected boolean boolean_from_statement(PreparedStatement statement) {				
		boolean resultado = false;
		
		try {
			resultado = statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultado;		
	}
	
	protected ResultSet executarQuery(String query) {
		PreparedStatement statement = this.preparar_statement(query);		
				
		ResultSet resultado = resultado_from_statement(statement);
		return resultado;
	}
	
	protected ResultSet executarQuery(String query, List<Object> parametros) {
		PreparedStatement statement = this.preparar_statement(query, parametros);		
		
		ResultSet resultado = resultado_from_statement(statement);
		return resultado;
	}
	
	protected boolean executarQueryAlteracao(String query) {
		PreparedStatement statement = this.preparar_statement(query);

		boolean resultado = boolean_from_statement(statement);

		return resultado;
	}
	
	protected boolean executarQueryAlteracao(String query, List<Object> parametros) {
		PreparedStatement statement = this.preparar_statement(query, parametros);
		
		System.out.println( statement );
		boolean resultado = boolean_from_statement(statement);
		return resultado;
	}
	
	protected int get_tipo_sql_correspondente(Class<? extends Object> classe) {
		return relacao_tipos_sql_java.get(classe);
	}

	protected PreparedStatement preparar_statement(String query) {
		Database db = new Database();

		PreparedStatement statement = null;
				
		try {
			statement = db.getConexao().prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return statement;
	}

	protected PreparedStatement preparar_statement(String query, List<Object> parametros) {
		Database db = new Database();
				
		PreparedStatement statement = null;

		try {
			statement = db.getConexao().prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (statement != null) {
			
			int indice_parametro = 1;
			for( Object parametro : parametros ) {
				
				int tipo_sql_valor = this.get_tipo_sql_correspondente( parametro.getClass() );
				try {
					statement.setObject(indice_parametro, parametro, tipo_sql_valor);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				++indice_parametro;
			}

		}

		return statement;
	}
	
	protected List<Map> get_list_from_result_set(ResultSet resultados, List<String> nome_colunas) {
		
		List<Map> registros = new ArrayList<>();		
		try {
			while( resultados.next() ) {
				Map<String, Object> registro = new HashMap<String, Object>();
				
				for( String nome_coluna : nome_colunas ) {
					Class tipo_valor = resultados.getObject( nome_coluna ).getClass();
					
					if( tipo_valor.isInstance( resultados.getObject( nome_coluna ) ) ) {
						Object valor = resultados.getObject( nome_coluna );
						valor = tipo_valor.cast(valor);
						registro.put(nome_coluna, valor);
					}
				}
								
				registros.add( registro );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return registros;
	}
	
	protected <T extends DatabaseUtils> List<T> get_list_from_result_set(ResultSet resultados, Class<T> tipo) {

		List<T> lista = new ArrayList<>();
		try {
			while (resultados.next()) {
				DatabaseUtils objeto = this.getInstanceOfT(this.getClass());

				Object[] campos = this.get_campos(true).toArray();				
				
				for (int i = 0; i < campos.length; ++i) {
					String nome_campo = String.valueOf( campos[i] );
					Object valor_campo = resultados.getObject( nome_campo );
					
					objeto.set_campo_valor(objeto, nome_campo, valor_campo);
				}

				if (tipo.isInstance(objeto)) {
					lista.add(tipo.cast(objeto));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lista;
	}

	public <T extends DatabaseUtils> List<T> buscar(Class<T> tipo) {
		String query = "SELECT * FROM " + this.get_nome_tabela();

		ResultSet resultados = this.executarQuery( query );

		List<T> lista_resultados = this.get_list_from_result_set(resultados, tipo);
		return lista_resultados;
	}

	public <T extends DatabaseUtils> List<T> buscar(String where, List<Object> parametros, Class<T> tipo) {
		String query = "SELECT * FROM " + this.get_nome_tabela() + " ";
		query += "WHERE " + where;

		ResultSet resultados = this.executarQuery( query, parametros );

		List<T> lista_resultados = this.get_list_from_result_set(resultados, tipo);
		return lista_resultados;
	}	
	
	public Map<String, Object> get_campos_preenchidos() {
		Map<String, Object> campos = this.get_campos_valor(false);
		
		Map<String, Object> campos_preenchidos = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : campos.entrySet()){
			
			if( entry.getValue() != null ) {
				campos_preenchidos.put(entry.getKey(), entry.getValue());
			}
		}
		
		return campos_preenchidos;
	}
	
	public boolean inserir() {
		Map<String, Object> campos_chave_valor = this.get_campos_preenchidos();

		String query = "INSERT INTO " + this.get_nome_tabela() + " ";

		query += "(";
		int contador = 0;
		for (Map.Entry<String, Object> entry : campos_chave_valor.entrySet()) {
						
			if( entry.getValue() != null ) {
				++contador;
				query += entry.getKey();

				if (contador < campos_chave_valor.size()) {
					query += ", ";
				}
			}
		}
		query += ") ";
		
		query += "VALUES(";
				
		List<Object> valores = new ArrayList<>();		
		int contador_valores = 0;
		for (Map.Entry<String, Object> entry : campos_chave_valor.entrySet()) {			
			query += "?";
			valores.add( entry.getValue() );			
									
			if (contador_valores < campos_chave_valor.size() - 1) {
				query += ", ";
			}
            
            ++contador_valores;
		}
				
		query += ")";
				
		return this.executarQueryAlteracao(query, valores);
	}
	
	public boolean atualizar() {
		String query = "UPDATE " + this.get_nome_tabela() + " SET ";

		Map<String, Object> campo_valor = this.get_campos_valor(true);

		int contador = 0;

		List<Object> valores = new ArrayList<>();
		for (Map.Entry<String, Object> entry : campo_valor.entrySet()) {

			if (!entry.getKey().equals("id")) {
				query += entry.getKey() + " = ?";
				valores.add(entry.getValue());

				if (contador < campo_valor.size() - 1) {
					query += ", ";
				}
			}

			++contador;
		}

		query += " WHERE id = " + campo_valor.get("id");
		
		return this.executarQueryAlteracao(query, valores);
	}

	public boolean remover() {
		String query = "DELETE FROM " + this.get_nome_tabela() + " ";
		query += "WHERE id = " + this.get_campos_valor(true).get("id") + " ";
		query += "LIMIT 1";

		return this.executarQueryAlteracao(query);
	}
}
