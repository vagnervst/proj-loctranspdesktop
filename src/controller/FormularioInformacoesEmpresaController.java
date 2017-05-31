package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import dao.Empresa;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Arquivo;
import model.CustomCallable;
import model.Login;
import model.Server;
import model.ThreadTask;
import view.WindowManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class FormularioInformacoesEmpresaController implements Initializable {

	@FXML ImageView ivLogoEmpresa;
	@FXML TextField txtRazaoSocial;
	@FXML TextField txtNomeFantasia;
	@FXML TextField txtHost;
	@FXML AnchorPane apConteudo;
		
	File nova_logo;
	Empresa empresa;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		Login.get_id_empresa( new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				Integer id_empresa = (Integer) getParametro();
				
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
						List<Empresa> empresas_encontradas = (List<Empresa>) getParametro();
						
						if( empresas_encontradas != null && empresas_encontradas.size() == 1 ) {
							empresa = empresas_encontradas.get(0);
							
							txtRazaoSocial.setText( empresa.getRazaoSocial() );
							txtNomeFantasia.setText( empresa.getNomeFantasia() );
							txtHost.setText( empresa.getNomeHost() );
							
							new ThreadTask<Image>( new Callable<Image>() {
								
								@Override
								public Image call() throws Exception {
									// TODO Auto-generated method stub
									String caminho_imagem = Server.address + "img/uploads/empresas/" + empresa.getLogomarca();
									File arquivo_imagem = new File( caminho_imagem );
									return new Image( Arquivo.httpUrlFromFile(arquivo_imagem), true );
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
				
				return super.call();
			}
			
		});
		
	}

	@FXML public void selecionarFoto(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filtroJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
		FileChooser.ExtensionFilter filtroJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG");

		fileChooser.getExtensionFilters().addAll( filtroJPG, filtroJPEG );
		File arquivoImagem = fileChooser.showOpenDialog(null);
		
		if( arquivoImagem == null ) return;
		
		try {
			BufferedImage buffer = ImageIO.read( arquivoImagem );
			
			Image imagem_selecionada = SwingFXUtils.toFXImage(buffer, null);
			ivLogoEmpresa.setImage( imagem_selecionada );

			nova_logo = arquivoImagem;			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML public void cancelar(ActionEvent event) {
		WindowManager.fecharJanela( WindowManager.get_stage_de_componente(apConteudo) );
	}

	@FXML public void salvar(ActionEvent event) {
		
		Login.get_id_juridico( new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				Integer id_juridico = (Integer) getParametro();
				
				String razaoSocial = txtRazaoSocial.getText().toString().trim();
				String nomeFantasia = txtNomeFantasia.getText().toString().trim();
				String host = txtHost.getText().toString().trim();
				
				empresa.setRazaoSocial( razaoSocial );
				empresa.setNomeFantasia( nomeFantasia );
				empresa.setNomeHost( host );
										
				ThreadTask<Void> upload_imagem = new ThreadTask<Void>( new Callable<Void>() {
					
					@Override
					public Void call() throws Exception {
						// TODO Auto-generated method stub
						String pasta_imagens_empresa = "../img/uploads/empresas";
						String nome_arquivo = "empresa_usr_" + id_juridico + "." + FilenameUtils.getExtension( nova_logo.getName() );
						Arquivo.replace(pasta_imagens_empresa, empresa.getLogomarca(), nome_arquivo, nova_logo);
												
						return null;
					}
					
				}, new CustomCallable<Void>() {
					
					@Override
					public Void call() throws Exception {
						
						String pasta_imagens_empresa = "../img/uploads/empresas";
						String nome_arquivo = "empresa_usr_" + id_juridico + "." + FilenameUtils.getExtension( nova_logo.getName() );
						// TODO Auto-generated method stub
						new ThreadTask<Boolean>( new Callable<Boolean>() {
							
							@Override
							public Boolean call() throws Exception {
								// TODO Auto-generated method stub							
								File arquivo_imagem = new File( Server.address + "img/uploads/empresas/" + nome_arquivo );
								System.out.println("NOVA LOGO: " + arquivo_imagem );
								
								Image imagem = new Image( Arquivo.httpUrlFromFile( arquivo_imagem ), false );
								
								System.out.println( imagem );
								if( imagem.isError() ) {
									empresa.setLogomarca("company.png");
								} else {
									empresa.setLogomarca( nome_arquivo );
								}
																
								return empresa.atualizar();															
							}
							
						}, new CustomCallable<Boolean>() {
							
							@Override
							public Boolean call() throws Exception {
								// TODO Auto-generated method stub
								WindowManager.fecharJanela( WindowManager.get_stage_de_componente(apConteudo) );
								return super.call();
							}
							
						}).run();
						
						return super.call();
					}
					
				});
				
				new ThreadTask<Boolean>( new Callable<Boolean>() {
					
					@Override
					public Boolean call() throws Exception {
						// TODO Auto-generated method stub
						return empresa.atualizar();
					}
					
				}, new CustomCallable<Boolean>() {
					
					@Override
					public Boolean call() throws Exception {
						// TODO Auto-generated method stub
						
						if( nova_logo != null ) {
							upload_imagem.runWithProgress(apConteudo);
						} else {
							WindowManager.fecharJanela( WindowManager.get_stage_de_componente(apConteudo) );
						}
						
						return super.call();
					}
					
				}).runWithProgress(apConteudo);
				
				return super.call();
			}
			
		});			
		
	}

}
