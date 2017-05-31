package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Funcionario;
import dao.NivelAcessoJuridico;
import dao.TelaSoftware;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.AlertDialog;
import model.ComboBoxUtils;
import model.Context;
import model.CustomCallable;
import model.Login;
import model.TableViewUtils;
import model.ThreadTask;
import view.WindowManager;
import javafx.scene.layout.AnchorPane;

public class FormularioNivelAcessoController implements Initializable {

	@FXML AnchorPane apConteudo;
	@FXML Button btnRemoverTela;
	@FXML Button btnCancelar;
	@FXML Button btnConcluido;
	@FXML ComboBox<TelaSoftware> cbTelas;
	@FXML CheckBox chkLeitura;
	@FXML CheckBox chkEscrita;
	@FXML CheckBox chkModificacao;
	@FXML CheckBox chkRemocao;
	@FXML Button btnAdicionarTela;
	@FXML TableView tblTelasPermitidas;
	@FXML TextField txtTituloNivelAcesso;
	@FXML Button btnRemoverNivel;

	List<TelaSoftware> lista_telas_combobox;
	List<TelaSoftware> telas_permitidas;
	List<Map> valores_coluna;
	List<Map> permissoes;

	private static Integer LEITURA = 1, ESCRITA = 2, EDICAO = 3, REMOCAO = 4;

	NivelAcessoJuridico nivel_acesso;
	Integer id_nivel_acesso = -1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		nivel_acesso = new NivelAcessoJuridico();
		permissoes = new ArrayList<Map>();
		telas_permitidas = new ArrayList<TelaSoftware>();
		valores_coluna = new ArrayList<Map>();

		lista_telas_combobox = new TelaSoftware().buscar(TelaSoftware.class);
		ComboBoxUtils.popular_combobox(cbTelas, lista_telas_combobox);

		cbTelas.setOnAction(new ChangeTelas());

		tblTelasPermitidas.setOnMouseClicked(new TabelaClick());

		id_nivel_acesso = Context.getIntData("idNivelAcesso");

		if( id_nivel_acesso != -1 ) {

			ThreadTask<List<Map>> task_get_telas_permitidas = new ThreadTask<List<Map>>( new Callable<List<Map>>() {

				@Override
				public List<Map> call() throws Exception {
					// TODO Auto-generated method stub
					return nivel_acesso.getTelasPermitidas();
				}

			}, new CustomCallable<List<Map>>() {

				@Override
				public List<Map> call() throws Exception {
					// TODO Auto-generated method stub
					List<Map> telas = (List<Map>) getParametro();
					preparar_lista_telas(telas);
					return super.call();
				}

			});

			ThreadTask<List<Map>> task_get_quantidade_funcionarios_atribuidos = new ThreadTask<List<Map>>( new Callable<List<Map>>() {

				@Override
				public List<Map> call() throws Exception {
					// TODO Auto-generated method stub
					return nivel_acesso.getNiveisAcesso("id = ?", Arrays.asList( id_nivel_acesso ));
				}

			}, new CustomCallable<List<Map>>() {

				@Override
				public List<Map> call() throws Exception {
					// TODO Auto-generated method stub
					List<Map> nivel_acesso_info = (List<Map>) getParametro();

					if( nivel_acesso_info.get(0) != null && nivel_acesso_info.get(0).containsKey("qtdFuncionarios") ) {
						Long qtd_funcionarios_atribuidos = (Long) nivel_acesso_info.get(0).get("qtdFuncionarios");

						if( qtd_funcionarios_atribuidos > 0 ) {
							btnRemoverNivel.setVisible(false);
							btnRemoverNivel.setManaged(false);
						}
					}

					return super.call();
				}

			});

			ThreadTask<NivelAcessoJuridico> task_info_nivel_acesso = new ThreadTask<NivelAcessoJuridico>( new Callable<NivelAcessoJuridico>() {

				@Override
				public NivelAcessoJuridico call() throws Exception {
					// TODO Auto-generated method stub
					return nivel_acesso.buscar("id = ?", Arrays.asList( id_nivel_acesso ), NivelAcessoJuridico.class).get(0);
				}

			}, new CustomCallable<NivelAcessoJuridico>() {

				@Override
				public NivelAcessoJuridico call() throws Exception {
					// TODO Auto-generated method stub
					nivel_acesso = (NivelAcessoJuridico) getParametro();

					txtTituloNivelAcesso.setText( nivel_acesso.getTitulo() );
					task_get_telas_permitidas.runWithProgress(apConteudo);
					task_get_quantidade_funcionarios_atribuidos.runWithProgress(apConteudo);
					return super.call();
				}

			});

			task_info_nivel_acesso.runWithProgress(apConteudo);
		}
		
		controle_permissoes();
	}

	public void controle_permissoes() {
		if( Login.get_tipo_conta() == Login.FUNCIONARIO ) {
			Funcionario funcionario = new Funcionario();
			funcionario.setId( Login.get_id_usuario() );

			funcionario.getPermissoes( new CustomCallable<List<Map>>() {

				@Override
				public List<Map> call() throws Exception {
					// TODO Auto-generated method stub
					List<Map> telas_permitidas = (List<Map>) getParametro();
					
					final int cod_tela_niveis_acesso = 4;
                    
                    boolean tela_niveis_acesso = false;
                    
					for( int i = 0; i < telas_permitidas.size(); ++i ) {

						Map<String, Object> tela = telas_permitidas.get(i);
						
						int codigo_tela = ((int) tela.get("cod"));
						
						if( codigo_tela == cod_tela_niveis_acesso ) {
							tela_niveis_acesso = true;
		
							if( tela.get("remocao") == null ) {
								btnRemoverNivel.setDisable(true);
							}
							
							if( tela.get("edicao") == null && id_nivel_acesso != -1 ) {
								btnConcluido.setDisable(true);
							}
							
							if( tela.get("escrita") == null && id_nivel_acesso == -1 ) {
								btnConcluido.setDisable(true);
							}
							
						}
					}

                    if( tela_niveis_acesso == false ) {
                    	btnRemoverNivel.setDisable(true);
                    	btnConcluido.setDisable(true);
                    }                    
                    
					return super.call();
				}

			});
		}
	}
	
	private void preparar_lista_telas(List<Map> telas) {
		for( int i = 0; i < telas.size(); ++i ) {
            Map<String, Object> permissoes_tela = new HashMap<String, Object>();
            permissoes_tela.put("idTelaAlvo", telas.get(i).get("id"));
            permissoes_tela.put("leitura", ( telas.get(i).get("leitura") != null )? true : false );
            permissoes_tela.put("escrita", ( telas.get(i).get("escrita") != null )? true : false );
            permissoes_tela.put("modificacao", ( telas.get(i).get("edicao") != null )? true : false );
            permissoes_tela.put("remocao", ( telas.get(i).get("remocao") != null )? true : false );

            permissoes.add( permissoes_tela );
            TelaSoftware tela = encontrar_tela( (int) telas.get(i).get("id") );
			telas_permitidas.add( tela );

			Map<String, Object> map_tela_para_tabela = new HashMap<String, Object>();

			System.out.println("Adicionando: " + tela);
			map_tela_para_tabela.put("objTela", tela);
			valores_coluna.add( map_tela_para_tabela );

			preencher_tabela_telas_permitidas();
		}
	}

	public void preencher_tabela_telas_permitidas() {
		Map<String, String> colunas = new LinkedHashMap<String, String>();
		colunas.put("Titulo", "objTela");

		TableViewUtils.preparar_tabela(tblTelasPermitidas, colunas, new Callable<List<Map>>() {

			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return valores_coluna;
			}
		});
	}

	private boolean is_tela_adicionada(TelaSoftware tela) {
		if( telas_permitidas.contains(tela) ) return true;

		return false;
	}

	private TelaSoftware encontrar_tela(int id_tela) {

		for(int i = 0; i < lista_telas_combobox.size(); ++i) {
			System.out.println( "ID: " +  lista_telas_combobox.get(i).getId() );
			if( lista_telas_combobox.get(i).getId() == id_tela ) return lista_telas_combobox.get(i);
		}

		return null;
	}

	private Map<String, Object> get_permissoes_da_tela(TelaSoftware tela) {
		return permissoes.get( telas_permitidas.indexOf(tela) );
	}

	public void adicionar_tela(TelaSoftware tela) {
		Map<String, Object> permissoes_selecionadas = new HashMap<String, Object>();
		permissoes_selecionadas.put("idTelaAlvo", tela.getId());
		permissoes_selecionadas.put("leitura", chkLeitura.selectedProperty().get());
		permissoes_selecionadas.put("escrita", chkEscrita.selectedProperty().get());
		permissoes_selecionadas.put("modificacao", chkModificacao.selectedProperty().get());
		permissoes_selecionadas.put("remocao", chkRemocao.selectedProperty().get());

		if( is_tela_adicionada(tela) ) {
			Map<String, Object> permissoes_a_substituir = get_permissoes_da_tela(tela);

			permissoes_a_substituir.replace("leitura", permissoes_selecionadas.get("leitura"));
			permissoes_a_substituir.replace("escrita", permissoes_selecionadas.get("escrita"));
			permissoes_a_substituir.replace("modificacao", permissoes_selecionadas.get("modificacao"));
			permissoes_a_substituir.replace("remocao", permissoes_selecionadas.get("remocao"));
		} else {

			permissoes.add( permissoes_selecionadas );

			telas_permitidas.add( tela );
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("objTela", tela);
			valores_coluna.add( map );
		}
	}

	private List<Map> getPermissoes(TelaSoftware tela) {

		List<Map> lista_permissoes = new ArrayList<>();
		for( int i = 0; i < permissoes.size(); ++i ) {
			if( (int) permissoes.get(i).get("idTelaAlvo") == tela.getId() ) {
				lista_permissoes.add( permissoes.get(i) );
			}
		}

		return lista_permissoes;
	}

	public void remover_tela(TelaSoftware tela) {
		telas_permitidas.remove(tela);

		for( int i = 0; i < permissoes.size(); ++i ) {
			if( (int) permissoes.get(i).get("idTelaAlvo") == tela.getId() ) {
				permissoes.remove(i);
			}
		}

		for( int i = 0; i < valores_coluna.size(); ++i ) {
			if( valores_coluna.get(i).get("objTela") == tela ) {
				valores_coluna.remove(i);
			}
		}
	}

	private void resetFormulario() {
		telas_permitidas.clear();
		permissoes.clear();
		setSelecaoCheckBoxes(false, false, false, false);
		preencher_tabela_telas_permitidas();
	}

	private void setStatusCheckBoxes(boolean leitura, boolean escrita, boolean modificacao, boolean remocao) {
		if( leitura ) {
			chkLeitura.setDisable(false);
		} else {
			chkLeitura.setDisable(true);
		}

		if( escrita ) {
			chkEscrita.setDisable(false);
		} else {
			chkEscrita.setDisable(true);
		}

		if( modificacao ) {
			chkModificacao.setDisable(false);
		} else {
			chkModificacao.setDisable(true);
		}

		if( remocao ) {
			chkRemocao.setDisable(false);
		} else {
			chkRemocao.setDisable(true);
		}
	}

	private void setSelecaoCheckBoxes(boolean leitura, boolean escrita, boolean modificacao, boolean remocao) {
		if( leitura ) {
			chkLeitura.setSelected(true);
		} else {
			chkLeitura.setSelected(false);
		}

		if( escrita ) {
			chkEscrita.setSelected(true);
		} else {
			chkEscrita.setSelected(false);
		}

		if( modificacao ) {
			chkModificacao.setSelected(true);
		} else {
			chkModificacao.setSelected(false);
		}

		if( remocao ) {
			chkRemocao.setSelected(true);
		} else {
			chkRemocao.setSelected(false);
		}
	}

	@FXML public void adicionarTela(ActionEvent event) {
		TelaSoftware tela = cbTelas.getSelectionModel().getSelectedItem();
		if( tela == null ) return;

		adicionar_tela(tela);

		preencher_tabela_telas_permitidas();
		setSelecaoCheckBoxes(false, false, false, false);
		btnAdicionarTela.setText("Adicionar");
	}

	@FXML public void removerTela(ActionEvent event) {
		Map<String, Object> map = (Map<String, Object>) tblTelasPermitidas.getSelectionModel().getSelectedItem();
		TelaSoftware tela = (TelaSoftware) map.get("objTela");

		remover_tela(tela);
		preencher_tabela_telas_permitidas();
		setSelecaoCheckBoxes(false, false, false, false);
	}

	@FXML public void concluir(ActionEvent event) {
		if(  txtTituloNivelAcesso.getText() == null ) return;

		Login.get_id_juridico( new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				Integer id_juridico = (Integer) getParametro();
				
				String titulo_nivel_acesso = txtTituloNivelAcesso.getText().trim();

				nivel_acesso = new NivelAcessoJuridico();
				nivel_acesso.setTitulo( titulo_nivel_acesso );
				nivel_acesso.setIdUsuario( id_juridico );

				ThreadTask<Void> task_relacionar_telas = new ThreadTask<Void>( new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						// TODO Auto-generated method stub

						for( int i = 0; i < telas_permitidas.size(); ++i ) {
							TelaSoftware tela = telas_permitidas.get(i);

							List<Map> lista_permissoes_tela = getPermissoes(tela);
							for( int x = 0; x < lista_permissoes_tela.size(); ++x ) {
								List<Integer> permissoes_tela = new ArrayList<Integer>();

								if( (Boolean) lista_permissoes_tela.get(x).get("modificacao") ) {
									permissoes_tela.add( EDICAO );
								}

								if( (Boolean) lista_permissoes_tela.get(x).get("escrita") ) {
									permissoes_tela.add( ESCRITA );
								}

								if( (Boolean) lista_permissoes_tela.get(x).get("leitura") ) {
									permissoes_tela.add( LEITURA );
								}

								if( (Boolean) lista_permissoes_tela.get(x).get("remocao") ) {
									permissoes_tela.add( REMOCAO );
								}

								for( int z = 0; z < permissoes_tela.size(); ++z ) {
									nivel_acesso.relacionar_a_tela( tela.getId(), permissoes_tela.get(z), apConteudo, new CustomCallable<Integer>() {
										@Override
										public Integer call() throws Exception {
											// TODO Auto-generated method stub
											return super.call();
										}
									});							
								}
							}
						}

						return null;
					}

				}, new CustomCallable<Void>() {

					@Override
					public Void call() throws Exception {
						// TODO Auto-generated method stub
						WindowManager.fecharJanela( WindowManager.get_stage_de_componente(btnAdicionarTela) );
						return super.call();
					}

				});

				if( id_nivel_acesso != -1 ) {
					nivel_acesso.setId(id_nivel_acesso);
					nivel_acesso.atualizar();

					ThreadTask<Boolean> task_eliminar_relacionamentos = new ThreadTask<Boolean>( new Callable<Boolean>() {

						@Override
						public Boolean call() throws Exception {
							// TODO Auto-generated method stub
							return nivel_acesso.limpar_relacionamentos_a_telas();
						}

					}, new CustomCallable<Boolean>() {

						@Override
						public Boolean call() throws Exception {
							// TODO Auto-generated method stub
							System.out.println("RELACINANDO TELAS");
							task_relacionar_telas.runWithProgress(apConteudo);
							return super.call();
						}

					});

					ThreadTask<Boolean> task_atualizar = new ThreadTask<Boolean>( new Callable<Boolean>() {

						@Override
						public Boolean call() throws Exception {
							// TODO Auto-generated method stub
							return nivel_acesso.atualizar();
						}

					}, new CustomCallable<Boolean>() {

						@Override
						public Boolean call() throws Exception {
							// TODO Auto-generated method stub
							System.out.println("ELIMINANDO RELACIONAMENTOS");
							task_eliminar_relacionamentos.run();
							return super.call();
						}

					});

					task_atualizar.runWithProgress(apConteudo);

				} else {

					ThreadTask<Integer> task_inserir = new ThreadTask<Integer>( new Callable<Integer>() {

						@Override
						public Integer call() throws Exception {
							// TODO Auto-generated method stub
							return nivel_acesso.inserirGetKey();
						}

					}, new CustomCallable<Integer>() {

						@Override
						public Integer call() throws Exception {
							// TODO Auto-generated method stub
							Integer id_nivel_acesso_inserido = (Integer) getParametro();
							nivel_acesso.setId(id_nivel_acesso_inserido);
							task_relacionar_telas.runWithProgress(apConteudo);

							return super.call();
						}

					});

					task_inserir.runWithProgress(apConteudo);
				}
				
				return super.call();
			}
			
		});			
	}

	@FXML public void removerNivel(ActionEvent event) {
		if( AlertDialog.show("Confirmar", "Remover Nível de Acesso", "Você tem certeza?", AlertType.CONFIRMATION) ) {
			nivel_acesso = new NivelAcessoJuridico();
			nivel_acesso.setId( id_nivel_acesso );

			ThreadTask<Boolean> task_remover = new ThreadTask<Boolean>( new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					return nivel_acesso.remover();
				}

			}, new CustomCallable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					WindowManager.fecharJanela( WindowManager.get_stage_de_componente(btnAdicionarTela) );
					return super.call();
				}

			});

			ThreadTask<Boolean> task_limpar_relacionamento_a_telas = new ThreadTask<Boolean>( new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					return nivel_acesso.limpar_relacionamentos_a_telas();
				}

			}, new CustomCallable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					task_remover.runWithProgress(apConteudo);
					return super.call();
				}

			});

			task_limpar_relacionamento_a_telas.runWithProgress(apConteudo);
		}
	}

	private class ChangeTelas implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			TelaSoftware tela = cbTelas.getSelectionModel().getSelectedItem();

			setStatusCheckBoxes(tela.isLeitura(), tela.isEscrita(), tela.isEdicao(), tela.isRemocao());
			setSelecaoCheckBoxes(false, false, false, false);
		}

	}

	private class TabelaClick implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			// TODO Auto-generated method stub
			Map<String, Object> map = (Map<String, Object>) tblTelasPermitidas.getSelectionModel().getSelectedItem();
			
			if( map == null ) return;
			
			TelaSoftware tela = (TelaSoftware) map.get("objTela");

			cbTelas.getSelectionModel().select( tela );
			setStatusCheckBoxes(tela.isLeitura(), tela.isEscrita(), tela.isEdicao(), tela.isRemocao());

			Map<String, Object> permissoes_selecionadas = get_permissoes_da_tela(tela);
			System.out.println( permissoes_selecionadas );
			setSelecaoCheckBoxes((Boolean) permissoes_selecionadas.get("leitura"),
					(Boolean) permissoes_selecionadas.get("escrita"),
					(Boolean) permissoes_selecionadas.get("modificacao"),
					(Boolean) permissoes_selecionadas.get("remocao"));

			btnAdicionarTela.setText("Salvar");
		}

	}
}