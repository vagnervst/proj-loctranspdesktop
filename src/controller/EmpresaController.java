package controller;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Empresa;
import dao.Funcionario;
import dao.Publicacao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import view.WindowManager;
import javafx.scene.control.Label;
import model.Arquivo;
import model.CustomCallable;
import model.Login;
import model.Server;
import model.ThreadTask;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

public class EmpresaController implements Initializable {

	@FXML Label lblNomeEmpresa;
	@FXML Label lblCnpjEmpresa;
	@FXML Label lblPublicacoes;
	@FXML Label lblFuncionarios;
	@FXML Label lblFuncionariosOnline;
	@FXML AnchorPane apConteudo;
	@FXML ImageView ivLogoEmpresa;
	@FXML Button btnFuncionarios;
	@FXML Button btnAgencias;
	@FXML MenuItem btnNiveisAcesso;
	@FXML MenuItem btnEstatisticaPublicacoes;
	@FXML MenuItem btnEstatisticaLocacoes;
	@FXML Button btnAlterarInformacoes;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		Login.get_id_empresa( new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				Integer id_empresa = (Integer) getParametro();
				
				if( id_empresa != null ) {
					
					new ThreadTask<List<Empresa>>( new Callable<List<Empresa>>() {

						@Override
						public List<Empresa> call() throws Exception {
							// TODO Auto-generated method stub
							return new Empresa().buscar("id = ?", Arrays.asList( id_empresa ), Empresa.class);
						}
						
					}, new CustomCallable<List<Empresa>>() {
						
						@Override
						public List<Empresa> call() throws Exception {
							// TODO Auto-generated method stub
							List<Empresa> lista_empresa = (List<Empresa>) getParametro();
							
							if( lista_empresa != null && lista_empresa.size() == 1 ) {
								Empresa empresa = lista_empresa.get(0);
								
								lblNomeEmpresa.setText( empresa.getRazaoSocial() );
								lblCnpjEmpresa.setText( empresa.getCnpj() );
								
								new ThreadTask<Image>( new Callable<Image>() {
									
									@Override
									public Image call() throws Exception {
										// TODO Auto-generated method stub
										String path = Server.address + "img/uploads/empresas/" + empresa.getLogomarca();
										File arquivo_logo_empresa = new File( path );
																				
										return new Image( Arquivo.httpUrlFromFile(arquivo_logo_empresa), true );
									}
									
								}, new CustomCallable<Image>() {
									
									@Override
									public Image call() throws Exception {
										// TODO Auto-generated method stub
										Image logo_empresa = (Image) getParametro();
										
										if( logo_empresa.isError() ) {
											logo_empresa = new Image("/images/company.png");
										}
										
										ivLogoEmpresa.setImage( logo_empresa );
										
										return super.call();
									}
									
								}).run();
								
							}
							
							return super.call();
						}
						
					}).runWithProgress(apConteudo);		
					
					ThreadTask<List<Funcionario>> task_quantidade_funcionarios = new ThreadTask<List<Funcionario>>( new Callable<List<Funcionario>>() {

						@Override
						public List<Funcionario> call() throws Exception {
							// TODO Auto-generated method stub							
							return new Funcionario().buscar("idEmpresa = ?", Arrays.asList( id_empresa ), Funcionario.class);
						}
						
					}, new CustomCallable<List<Funcionario>>() {
						
						@Override
						public List<Funcionario> call() throws Exception {
							// TODO Auto-generated method stub
							List<Funcionario> funcionarios = (List<Funcionario>) getParametro();
							
							if( funcionarios != null ) {
																							
								int funcionarios_online = 0;
								for( int i = 0; i < funcionarios.size(); ++i ) {
									if( funcionarios.get(i).getStatusOnline() == 1 ) ++funcionarios_online;
								}
								
								lblFuncionarios.setText( String.format("%d", funcionarios.size()) );
								lblFuncionariosOnline.setText( String.format("%d", funcionarios_online) );								
							}
							
							return super.call();
						}
						
					});
					
					task_quantidade_funcionarios.run();					
				}
				
				return super.call();
			}
			
		});
		
		Login.get_id_juridico( new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				Integer id_juridico = (Integer) getParametro();
				
				if( id_juridico != null ) {
					
					ThreadTask<Integer> task_quantidade_anuncios = new ThreadTask<Integer>( new Callable<Integer>() {

						@Override
						public Integer call() throws Exception {
							// TODO Auto-generated method stub
							List<Publicacao> lista_publicacoes_empresa = new Publicacao().buscar("idUsuario = ?", Arrays.asList( id_juridico ), Publicacao.class);							
							return lista_publicacoes_empresa.size();
						}											
						
					}, new CustomCallable<Integer>() {
						
						@Override
						public Integer call() throws Exception {
							// TODO Auto-generated method stub
							Integer qtd_publicacoes = (Integer) getParametro();
							
							if( qtd_publicacoes != null ) {								
								lblPublicacoes.setText( String.format("%d", qtd_publicacoes) );								
							}
							
							return super.call();
						}
						
					});
					
					task_quantidade_anuncios.run();													
				}
				
				return super.call();
			}
			
		});
		
		controle_permissoes();
	}

	public void controle_permissoes() {
		if( Login.get_tipo_conta() == Login.FUNCIONARIO ) {
			btnAlterarInformacoes.setVisible(false);
			
			Funcionario funcionario = new Funcionario();
			funcionario.setId( Login.get_id_usuario() );

			funcionario.getPermissoes( new CustomCallable<List<Map>>() {

				@Override
				public List<Map> call() throws Exception {
					// TODO Auto-generated method stub
					List<Map> telas_permitidas = (List<Map>) getParametro();
					
					final int cod_tela_funcionarios = 1;
					final int cod_tela_agencias = 3;
					final int cod_tela_nivel_acesso = 4;
					final int cod_tela_estatistica_publicacao = 9;
					final int cod_tela_estatistica_locacao = 8;
                    
                    boolean tela_funcionarios = false;
                    boolean tela_agencias = false;
                    boolean tela_nivel_acesso = false;
                    boolean tela_estatistica_publicacao = false;
                    boolean tela_estatistica_locacao = false;
                    
					for( int i = 0; i < telas_permitidas.size(); ++i ) {

						Map<String, Object> tela = telas_permitidas.get(i);
						
						int codigo_tela = ((int) tela.get("cod"));
						
						if( codigo_tela == cod_tela_funcionarios ) {
							tela_funcionarios = true;
							
							if( tela.get("leitura") == null ) {
								btnFuncionarios.setDisable(true);
							}

						} else if( codigo_tela == cod_tela_agencias ) {
							tela_agencias = true;
							
							if( tela.get("leitura") == null ) {
								btnAgencias.setDisable(true);
							}
							
						} else if( codigo_tela == cod_tela_nivel_acesso ) {
                            tela_nivel_acesso = true;
                            
                            if( tela.get("leitura") == null ) {
                            	btnNiveisAcesso.setDisable(true);
                            }
                            
						} else if( codigo_tela == cod_tela_estatistica_publicacao ) {
                            tela_estatistica_publicacao = true;
                            
                            if( tela.get("leitura") == null ) {
                                btnEstatisticaPublicacoes.setDisable(true);
                            }
                            
                        } else if( codigo_tela == cod_tela_estatistica_locacao ) {
                            tela_estatistica_locacao = true;
                            
                            if( tela.get("leitura") == null ) {
                                btnEstatisticaLocacoes.setDisable(true);
                            }
                            
                        }
					}

                    if( tela_funcionarios == false ) {
                        btnFuncionarios.setDisable(true);
                    }
                    
                    if( tela_agencias == false ) {
                        btnAgencias.setDisable(true);
                    }
                    
                    if( tela_nivel_acesso == false ) {
                        btnNiveisAcesso.setDisable(true);
                    }
                    
                    if( tela_estatistica_publicacao == false ) {
                        btnEstatisticaPublicacoes.setDisable(true);
                    }
                    
                    if( tela_estatistica_locacao == false ) {
                        btnEstatisticaLocacoes.setDisable(true);
                    }
                    
                    
					return super.call();
				}

			});
		} else {
			btnAlterarInformacoes.setVisible(true);
		}
	}
	
	@FXML public void abrirAgencias(ActionEvent event) {
		WindowManager.abrirModal("/view/Agencias.fxml", getClass());
	}

	@FXML public void abrirFuncionarios(ActionEvent event) {
		WindowManager.abrirModal("/view/Funcionarios.fxml", getClass());
	}

	@FXML public void abrirPerfisNivelAcesso(ActionEvent event) {
		WindowManager.abrirModal("/view/NiveisAcesso.fxml", getClass());
	}
	
	@FXML public void abrirEstatisticasPublicacao(ActionEvent event) {
		WindowManager.abrirModal("/view/RelatorioPublicacao.fxml", getClass());
	}

	@FXML public void abrirEstatisticasLocacao(ActionEvent event) {
		WindowManager.abrirModal("/view/RelatorioLocacoes.fxml", getClass());
	}

	@FXML public void abrirInformacoesEmpresa(ActionEvent event) {
		WindowManager.abrirModal("/view/FormularioInformacoesEmpresa.fxml", getClass());
	}

}