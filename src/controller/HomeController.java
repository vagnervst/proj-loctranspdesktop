package controller;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Agencia;
import dao.Empresa;
import dao.Funcionario;
import dao.NivelAcessoJuridico;
import dao.NivelAcessoJuridicoTelaSoftare;
import dao.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Arquivo;
import model.CustomCallable;
import model.Login;
import model.Server;
import model.ThreadTask;
import view.WindowManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.MenuItem;

public class HomeController implements Initializable {

	@FXML Button btnPublicacoes;	
	@FXML Label lblNomeEmpresa;
	@FXML Label lblNomeFuncionario;
	@FXML Label lblHolderNivelAcesso;
	@FXML Label lblNivelAcesso;
	@FXML Label lblHolderAgencia;
	@FXML Label lblAgencia;
	@FXML Label lblUsuario;
	@FXML ImageView ivImagemEmpresa;
	
	Empresa empresa_alvo;
	int id_empresa;
	@FXML MenuItem btnBuscarClientes;
	@FXML MenuItem btnCadastrarCliente;
	@FXML MenuItem btnEmpresa;
	@FXML MenuItem btnPlano;	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		if( Login.get_tipo_conta() == Login.JURIDICO ) {				
		
			ThreadTask<List<Usuario>> task_get_info_juridico = new ThreadTask<List<Usuario>>(new Callable<List<Usuario>>() {
	
				@Override
				public List<Usuario> call() throws Exception {
					// TODO Auto-generated method stub
					return new Usuario().buscar("id = ?", Arrays.asList( Login.get_id_usuario() ), Usuario.class);
				}
				
			}, new CustomCallable<List<Usuario>>() {
				
				@Override
				public List<Usuario> call() throws Exception {
					// TODO Auto-generated method stub
					List<Usuario> usuarios_encontrados = (List<Usuario>) getParametro();
					
					if( usuarios_encontrados.size() >= 1 ) {					
						lblNomeFuncionario.setText( String.format("%s %s", usuarios_encontrados.get(0).getNome(), usuarios_encontrados.get(0).getSobrenome()) );
						lblUsuario.setText( String.format("%s", usuarios_encontrados.get(0).getEmail()) );
					}
					
					return super.call();
				}
				
			});
			
			task_get_info_juridico.run();
		} else {
			
			ThreadTask<List<Funcionario>> task_get_info_funcionario = new ThreadTask<List<Funcionario>>(new Callable<List<Funcionario>>() {
				
				@Override
				public List<Funcionario> call() throws Exception {
					// TODO Auto-generated method stub
					return new Funcionario().buscar("id = ?", Arrays.asList( Login.get_id_usuario() ), Funcionario.class);
				}
				
			}, new CustomCallable<List<Funcionario>>() {
				
				@Override
				public List<Funcionario> call() throws Exception {
					// TODO Auto-generated method stub
					List<Funcionario> funcionarios_encontrados = (List<Funcionario>) getParametro();
					
					if( funcionarios_encontrados != null && funcionarios_encontrados.size() == 1 ) {					
						Funcionario funcionario_logado = funcionarios_encontrados.get(0);
												
						lblNomeFuncionario.setText( funcionario_logado.getNome() );
						lblUsuario.setText( funcionario_logado.getCredencial() );
						
						new ThreadTask<List<NivelAcessoJuridico>>( new Callable<List<NivelAcessoJuridico>>() {

							@Override
							public List<NivelAcessoJuridico> call() throws Exception {
								// TODO Auto-generated method stub
								return new NivelAcessoJuridico().buscar("id = ?", Arrays.asList( funcionario_logado.getIdNivelAcesso() ), NivelAcessoJuridico.class);
							}
							
						}, new CustomCallable<List<NivelAcessoJuridico>>() {
							
							@Override
							public List<NivelAcessoJuridico> call() throws Exception {
								// TODO Auto-generated method stub
								List<NivelAcessoJuridico> lista_niveis_acesso = (List<NivelAcessoJuridico>) getParametro();
								
								if( lista_niveis_acesso != null && lista_niveis_acesso.size() == 1 ) {
									NivelAcessoJuridico nivel_do_funcionario = lista_niveis_acesso.get(0);
									
									lblNivelAcesso.setText( nivel_do_funcionario.getTitulo() );
								}
								
								return super.call();
							}
							
						}).run();
						
						new ThreadTask<List<Agencia>>( new Callable<List<Agencia>>() {

							@Override
							public List<Agencia> call() throws Exception {
								// TODO Auto-generated method stub
								return new Agencia().buscar("id = ?", Arrays.asList( funcionario_logado.getIdAgencia() ), Agencia.class);
							}
							
						}, new CustomCallable<List<Agencia>>() {
							
							@Override
							public List<Agencia> call() throws Exception {
								// TODO Auto-generated method stub
								List<Agencia> lista_agencias_encontradas = (List<Agencia>) getParametro();
								
								if( lista_agencias_encontradas != null && lista_agencias_encontradas.size() == 1 ) {
									Agencia agencia_funcionario = lista_agencias_encontradas.get(0);
									
									lblAgencia.setText( agencia_funcionario.getTitulo() );
								}
								
								return super.call();
							}
							
						}).run();
						
					}
					
					return super.call();
				}
				
			});
			
			task_get_info_funcionario.run();
		}
		
		ThreadTask<List<Empresa>> task_empresa = new ThreadTask<List<Empresa>>(new Callable<List<Empresa>>() {

			@Override
			public List<Empresa> call() throws Exception {
				// TODO Auto-generated method stub
				return new Empresa().buscar("id = ?", Arrays.asList( id_empresa ), Empresa.class);
			}

		}, new CustomCallable<List<Empresa>>() {

			@Override
			public List<Empresa> call() throws Exception {
				// TODO Auto-generated method stub
				if( ((List<Empresa>) this.getParametro()).get(0) != null ) {
					
					empresa_alvo = (Empresa) ((List<Empresa>) this.getParametro()).get(0);
					
					lblNomeEmpresa.setText( empresa_alvo.getNomeFantasia() );
					
					String path = Server.address + "img/uploads/empresas/" + empresa_alvo.getLogomarca();
					File arquivo_logo_empresa = new File( path );					
					
					new ThreadTask<Image>( new Callable<Image>() {

						@Override
						public Image call() throws Exception {
							// TODO Auto-generated method stub
							return new Image( Arquivo.httpUrlFromFile(arquivo_logo_empresa), true );
						}											
						
					}, new CustomCallable<Image>() {
						
						@Override
						public Image call() throws Exception {
							// TODO Auto-generated method stub
							Image imagem_empresa = (Image) getParametro();
							
							if( imagem_empresa.isError() ) {								
								imagem_empresa = new Image( "/images/company.png" );				
							}
												
							ivImagemEmpresa.setImage( imagem_empresa );
							return super.call();
						}
						
					}).run();									
					
					if( Login.get_tipo_conta() == Login.JURIDICO ) {
						lblHolderAgencia.setVisible(false);
						lblAgencia.setVisible(false);
						lblHolderNivelAcesso.setVisible(false);
						lblNivelAcesso.setVisible(false);
					}
					
				}

				return super.call();
			}

		});
		
		Login.get_id_empresa(new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				id_empresa = (int) getParametro();
				task_empresa.run();
				return super.call();
			}
			
		});
			
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

					final int cod_tela_clientes = 5;
					final int cod_tela_plano = 7;
					final int cod_tela_funcionarios = 1;
					final int cod_tela_agencias = 3;
					final int cod_tela_nivel_acesso = 4;
					final int cod_tela_estatistica_publicacao = 9;
					final int cod_tela_estatistica_locacao = 8;
					
					boolean tela_clientes_encontrada = false;
					boolean tela_plano_encontrada = false;
					boolean tela_gerencia = false;
					for( int i = 0; i < telas_permitidas.size(); ++i ) {

						Map<String, Object> tela = telas_permitidas.get(i);
						
						int codigo_tela = ((int) tela.get("cod"));
						if( codigo_tela == cod_tela_clientes ) {
							tela_clientes_encontrada = true;
							
							if( tela.get("leitura") == null ) {
								btnBuscarClientes.setDisable(true);
							}
							
							if( tela.get("escrita") == null ) {
								btnCadastrarCliente.setDisable(true);
							}

						} else if( codigo_tela == cod_tela_plano ) {
							tela_plano_encontrada = true;
							
							if( tela.get("leitura") == null ) {
								btnPlano.setDisable(true);
							}
						}
						
						if( codigo_tela == cod_tela_funcionarios || 
								codigo_tela == cod_tela_agencias || 
								codigo_tela == cod_tela_nivel_acesso || 
								codigo_tela == cod_tela_estatistica_publicacao || 
								codigo_tela == cod_tela_estatistica_locacao ) 
						{
							tela_gerencia = true;
							btnEmpresa.setDisable(false);
						}
						
					}

					if( tela_clientes_encontrada == false ) {
						btnBuscarClientes.setDisable(true);											
						btnCadastrarCliente.setDisable(true);
					}
					
					if( tela_plano_encontrada == false ) {
						btnPlano.setDisable(true);
					}
					
					if( tela_gerencia == false ) {
						btnEmpresa.setDisable(true);						
					}
					
					return super.call();
				}

			});
		}
	}
	
	@FXML
	public void abrirPublicacoes(ActionEvent event) {
		WindowManager.abrirModal("/view/Publicacoes.fxml", getClass());
	}

	@FXML public void abrirSolicitacoes(ActionEvent event) {
		WindowManager.abrirModal("/view/Solicitacoes.fxml", getClass());
	}

	@FXML public void abrirFormularioUsuario(ActionEvent event) {
		WindowManager.abrirModal("/view/FormularioUsuario.fxml", getClass());
	}

	@FXML public void abrirGerenciaEmpresa(ActionEvent event) {
		WindowManager.abrirModal("/view/Empresa.fxml", getClass());
	}

	@FXML public void abrirGerenciaPlano(ActionEvent event) {
		WindowManager.abrirModal("/view/Plano.fxml", getClass());
	}

	@FXML public void abrirTabelaClientes(ActionEvent event) {
		WindowManager.abrirModal("/view/Usuarios.fxml", getClass());
	}

	@FXML public void logoff(ActionEvent event) {
		Login.set_id_usuario(null, Login.get_tipo_conta());
		WindowManager.abrirJanela(WindowManager.get_stage_de_componente(lblAgencia), "/view/Login.fxml", getClass());
	}
}
