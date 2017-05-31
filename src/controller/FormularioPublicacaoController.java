package controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import dao.Agencia;
import dao.CombustivelVeiculo;
import dao.FabricanteVeiculo;
import dao.Funcionario;
import dao.Publicacao;
import dao.TipoVeiculo;
import dao.Veiculo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.AlertDialog;
import model.Arquivo;
import model.ComboBoxUtils;
import model.Context;
import model.CustomCallable;
import model.FormValidator;
import model.Login;
import model.ThreadTask;
import view.WindowManager;
import javafx.scene.layout.AnchorPane;

public class FormularioPublicacaoController implements Initializable {

	@FXML ImageView ivImagemPrincipal;
	@FXML Button btnImagemPrincipal;
	@FXML ImageView ivImagemA;
	@FXML Button btnImagemA;
	@FXML ImageView ivImagemB;
	@FXML Button btnImagemB;
	@FXML ImageView ivImagemC;
	@FXML Button btnImagemC;
	@FXML ImageView ivImagemD;
	@FXML Button btnImagemD;
	@FXML TextField txtTitulo;
	@FXML Slider sliderPortas;
	@FXML RadioButton rdAutomatico;
	@FXML RadioButton rdManual;
	@FXML TextField txtValorDiaria;
	@FXML Button btnAcessorios;
	@FXML TextField txtValorCombustivel;
	@FXML TextField txtQuilometragem;
	@FXML TextField txtLimiteQuilometragem;
	@FXML TextField txtQuilometragemExcedida;
	@FXML TextArea txtDescricao;
	@FXML RadioButton rdOnline;
	@FXML RadioButton rdLocal;
	@FXML Button btnRemover;
	@FXML Button btnCancelar;
	@FXML Button btnSalvar;
	@FXML ToggleGroup groupTransmissao;
	@FXML ComboBox cbVeiculo;
	@FXML ComboBox cbAgencia;
	@FXML ComboBox cbCombustivel;
	@FXML ComboBox cbTipoVeiculo;
	@FXML ToggleGroup groupDisponibilizacao;
	@FXML ComboBox cbFabricante;
	@FXML TextField txtPrecoMedio;

	private final Integer NAO_DEFINIDO = -1, AUTOMATICO = 1, MANUAL = 2, STATUS_PUBLICACAO_DISPONIVEL = 1;
	private final Boolean ONLINE = true, LOCAL = false;

	List<Integer> lista_acessorios = null;

	Integer id_tipo_selecionado = NAO_DEFINIDO;
	Integer id_fabricante = NAO_DEFINIDO;
	Integer id_tipo_combustivel = NAO_DEFINIDO;
	Integer id_transmissao = NAO_DEFINIDO;
	Integer numero_portas = NAO_DEFINIDO;

	Map<String, File> imagens_publicacao;
	Publicacao publicacao_alvo = null;
	@FXML AnchorPane apConteudo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		lista_acessorios = new ArrayList<>();
		imagens_publicacao = new HashMap<>();
		btnRemover.setVisible(false);

		ComboBoxUtils.popular_combobox( cbTipoVeiculo, "visivel = ?", Arrays.asList(1), new TipoVeiculo() );
		ComboBoxUtils.popular_combobox( cbFabricante, new FabricanteVeiculo() );

		if( Login.get_tipo_conta() == Login.FUNCIONARIO ) {
			cbAgencia.setDisable(true);
		} else {
			Login.get_id_empresa( new CustomCallable() {

				@Override
				public Object call() throws Exception {
					// TODO Auto-generated method stub
					Integer id_empresa = (Integer) this.getParametro();

					ComboBoxUtils.popular_combobox(cbAgencia, "idEmpresa = ?", Arrays.asList( id_empresa ), new Agencia());
					return super.call();
				}

			});
		}

		mudar_status_combobox(cbFabricante);
		mudar_status_combobox(cbCombustivel);
		mudar_status_combobox(cbVeiculo);
		mudar_status_node(rdManual, false);
		mudar_status_node(rdAutomatico, false);
		mudar_status_node(sliderPortas, false);
		rdOnline.setSelected(true);

		FormValidator.limitar_caracteres(txtTitulo, 100);
		FormValidator.limitar_caracteres(txtDescricao, 400);
		FormValidator.limitar_para_decimal(txtValorDiaria, 3, 2);
		FormValidator.limitar_para_decimal(txtValorCombustivel, 2, 2);
		FormValidator.limitar_para_decimal(txtQuilometragemExcedida, 2, 2);
		FormValidator.limitar_para_inteiro(txtLimiteQuilometragem, 5);
		FormValidator.limitar_para_inteiro(txtQuilometragem, 5);
		FormValidator.limitar_para_decimal(txtPrecoMedio, 7, 2);

		cbTipoVeiculo.setOnAction( new AcaoTipoVeiculo() );
		cbFabricante.setOnAction( new AcaoFabricanteVeiculo() );
		cbCombustivel.setOnAction( new AcaoCombustivelVeiculo() );
		rdAutomatico.setOnMouseClicked( new AcaoTransmissaoVeiculo() );
		rdManual.setOnMouseClicked( new AcaoTransmissaoVeiculo() );
		sliderPortas.valueProperty().addListener( new AcaoQtdPortas() );
		btnImagemPrincipal.setOnAction( new AcaoBotaoSelecaoImagem() );
		btnImagemA.setOnAction( new AcaoBotaoSelecaoImagem() );
		btnImagemB.setOnAction( new AcaoBotaoSelecaoImagem() );
		btnImagemC.setOnAction( new AcaoBotaoSelecaoImagem() );
		btnImagemD.setOnAction( new AcaoBotaoSelecaoImagem() );

		sliderPortas.setMin(0);
		sliderPortas.setValue(0);
		sliderPortas.setShowTickLabels(true);
		sliderPortas.setShowTickMarks(true);
		sliderPortas.setMinorTickCount(0);
		sliderPortas.setMajorTickUnit(2);
		sliderPortas.setSnapToTicks(true);
		sliderPortas.setMax(8);

		Long id_publicacao_selecionada = Context.getLongData("idPublicacao");
		if( id_publicacao_selecionada != null ) {

			ThreadTask<Publicacao> task_busca_dados_publicacao = new ThreadTask<Publicacao>(new Callable<Publicacao>() {

				@Override
				public Publicacao call() throws Exception {
					// TODO Auto-generated method stub
					return new Publicacao().buscar("id = ?", Arrays.asList( id_publicacao_selecionada ), Publicacao.class).get(0);
				}

			}, new CustomCallable<Publicacao>(){

				@Override
				public Publicacao call() throws Exception {
					// TODO Auto-generated method stub
					publicacao_alvo = (Publicacao) this.getParametro();
					preencher_campos(publicacao_alvo);
					btnRemover.setVisible(true);
					return super.call();
				}

			});

			task_busca_dados_publicacao.runWithProgress(apConteudo);
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

					final int cod_tela_publicacoes = 2;
					
					boolean tela_encontrada = false;
					for( int i = 0; i < telas_permitidas.size(); ++i ) {

						Map<String, Object> tela = telas_permitidas.get(i);

						if( ((int) tela.get("cod")) == cod_tela_publicacoes ) {
							tela_encontrada = true;
							
							if( tela.get("edicao") == null ) {
								btnSalvar.setDisable(true);
							}
							
							if( tela.get("remocao") == null ) {
								btnRemover.setDisable(false);
							}

						}

					}

					if( tela_encontrada == false ) {
						btnSalvar.setDisable(true);											
						btnRemover.setDisable(false);
					}
					
					return super.call();
				}

			});
		}
	}

	private void preencher_acessorios(Publicacao publicacao_referencia) {
		ThreadTask<List<Integer>> task_acessorios = new ThreadTask<List<Integer>>( new Callable<List<Integer>>() {

			@Override
			public List<Integer> call() throws Exception {
				// TODO Auto-generated method stub
				return publicacao_referencia.get_id_acessorios_relacionados();
			}

		}, new CustomCallable<List<Integer>>() {

			@Override
			public List<Integer> call() throws Exception {
				// TODO Auto-generated method stub
				List<Integer> acessorios = (List<Integer>) this.getParametro();

				lista_acessorios = acessorios;

				return super.call();
			}

		});

		task_acessorios.run();
	}

	private void preencher_imagens() {

		ThreadTask<Map<String, File>> task_imagens = new ThreadTask<Map<String, File>>(new Callable<Map<String, File>>() {

			@Override
			public Map<String, File> call() throws Exception {
				// TODO Auto-generated method stub
				return publicacao_alvo.get_imagens();
			}

		}, new CustomCallable<Map<String, File>>() {

			@Override
			public Map<String, File> call() throws Exception {
				// TODO Auto-generated method stub
				Map<String, File> imagens_publicacao_selecionada = (Map<String, File>) this.getParametro();
				
				Image imagem_principal = new Image( Arquivo.httpUrlFromFile( imagens_publicacao_selecionada.get("imagemPrincipal") ) );
				Image imagem_a = new Image( Arquivo.httpUrlFromFile( imagens_publicacao_selecionada.get("imagemA") ) );
				Image imagem_b = new Image( Arquivo.httpUrlFromFile( imagens_publicacao_selecionada.get("imagemB") ) );
				Image imagem_c = new Image( Arquivo.httpUrlFromFile( imagens_publicacao_selecionada.get("imagemC") ) );
				Image imagem_d = new Image( Arquivo.httpUrlFromFile( imagens_publicacao_selecionada.get("imagemD") ) );
				
				if( imagem_principal.isError() ) {
					imagem_principal = new Image("/images/car_cityshare.png");
				}
				
				if( imagem_a.isError() ) {
					imagem_a = new Image("/images/car_cityshare.png");
				}
				
				if( imagem_b.isError() ) {
					imagem_b = new Image("/images/car_cityshare.png");
				}
				
				if( imagem_c.isError() ) {
					imagem_c = new Image("/images/car_cityshare.png");
				}
				
				if( imagem_d.isError() ) {
					imagem_d = new Image("/images/car_cityshare.png");
				}
				
				ivImagemPrincipal.setImage( imagem_principal );
				ivImagemA.setImage( imagem_a );
				ivImagemB.setImage( imagem_b );
				ivImagemC.setImage( imagem_c );
				ivImagemD.setImage( imagem_d );

				return super.call();
			}

		});

		task_imagens.run();
	}

	private void preencher_campos(Publicacao publicacao_referencia) {

		preencher_acessorios(publicacao_referencia);
		preencher_imagens();
		txtTitulo.setText( publicacao_alvo.getTitulo() );
		txtDescricao.setText( publicacao_alvo.getDescricao() );
		txtPrecoMedio.setText( FormValidator.double_to_string( publicacao_alvo.getPrecoMedio() ) );
		txtQuilometragem.setText( publicacao_alvo.getQuilometragemAtual().toString() );
		txtQuilometragemExcedida.setText( FormValidator.double_to_string( publicacao_alvo.getValorQuilometragem() ) );
		txtLimiteQuilometragem.setText( publicacao_alvo.getLimiteQuilometragem().toString() );
		txtValorDiaria.setText( FormValidator.double_to_string( publicacao_alvo.getValorDiaria() ) );
		txtValorCombustivel.setText( FormValidator.double_to_string( publicacao_alvo.getValorCombustivel() ) );

		ThreadTask<Veiculo> taskVeiculo = new ThreadTask<Veiculo>( new Callable<Veiculo>() {

			@Override
			public Veiculo call() throws Exception {
				// TODO Auto-generated method stub

				Veiculo veiculo_da_publicacao = new Veiculo().buscar("id = ?", Arrays.asList( publicacao_alvo.getIdVeiculo() ), Veiculo.class).get(0);

				id_tipo_selecionado = veiculo_da_publicacao.getIdTipoVeiculo();
				id_fabricante = veiculo_da_publicacao.getIdFabricante();
				id_tipo_combustivel = veiculo_da_publicacao.getIdTipoCombustivel();
				id_transmissao = veiculo_da_publicacao.getIdTransmissao();
				numero_portas = veiculo_da_publicacao.getQtdPortas();

				return veiculo_da_publicacao;
			}

		}, new CustomCallable<Veiculo>() {

			@Override
			public Veiculo call() throws Exception {
				// TODO Auto-generated method stub
				Veiculo veiculo_da_publicacao = (Veiculo) this.getParametro();

				cbTipoVeiculo.getSelectionModel().select( new TipoVeiculo().buscar("id = ?", Arrays.asList( id_tipo_selecionado ), TipoVeiculo.class).get(0) );
				cbCombustivel.getSelectionModel().select( new CombustivelVeiculo().buscar("id = ?", Arrays.asList( id_tipo_combustivel ), CombustivelVeiculo.class).get(0) );
				cbFabricante.getSelectionModel().select( new FabricanteVeiculo().buscar("id = ?", Arrays.asList( id_fabricante ), FabricanteVeiculo.class).get(0) );
				sliderPortas.setValue( numero_portas );
				cbVeiculo.getSelectionModel().select( veiculo_da_publicacao );

				if( veiculo_da_publicacao.getIdTransmissao() == AUTOMATICO )
				{
					rdAutomatico.setSelected( true );
				}
				else if( veiculo_da_publicacao.getIdTransmissao() == MANUAL )
				{
					rdManual.setSelected( true );
				}

				if( publicacao_referencia.getDisponivelOnline() == ONLINE )
				{
					rdOnline.setSelected( true );
				}
				else if( publicacao_referencia.getDisponivelOnline() == LOCAL )
				{
					rdLocal.setSelected( true );
				}

				return super.call();
			}

		});

		ThreadTask<Agencia> taskAgencia = new ThreadTask<Agencia>( new Callable<Agencia>() {

			@Override
			public Agencia call() throws Exception {
				// TODO Auto-generated method stub
				return new Agencia().buscar("id = ?", Arrays.asList( publicacao_alvo.getIdAgencia() ), Agencia.class).get(0);
			}

		}, new CustomCallable<Agencia>(){

			@Override
			public Agencia call() throws Exception {
				// TODO Auto-generated method stub
				cbAgencia.getSelectionModel().select( this.getParametro() );
				return super.call();
			}

		});

		taskVeiculo.run();
		taskAgencia.run();
	}

	private void mudar_status_combobox(ComboBox cb) {
		if( cb.getItems().size() > 0 ) {
			cb.setDisable(false);
		} else {
			cb.setDisable(true);
		}
	}

	private void mudar_status_node(Node node, boolean status) {
		node.setDisable( !status );
	}

	private String preparar_query_filtragem_veiculos() {

		Map info_imagem = new HashMap<>();
        String query_filtragem = "";
        if( id_tipo_selecionado != NAO_DEFINIDO ) {
            query_filtragem += "tv.id = " + id_tipo_selecionado;
        }

        if( id_fabricante != NAO_DEFINIDO ) {
            query_filtragem += " AND fv.id = " + id_fabricante;
        }

        if( id_tipo_combustivel != NAO_DEFINIDO ) {
            query_filtragem += " AND tc.id = " + id_tipo_combustivel;
        }

        if( id_transmissao != NAO_DEFINIDO ) {
            query_filtragem += " AND t.id = " + id_transmissao;
        }

        if( numero_portas != NAO_DEFINIDO ) {
            query_filtragem += " AND v.qtdPortas = " + numero_portas;
        }
        
        query_filtragem += " AND v.visivel = 1";
        
        return query_filtragem;
	}

	public void filtrar_veiculos() {
		ComboBoxUtils.popular_combobox(cbVeiculo, new Veiculo().getVeiculos( preparar_query_filtragem_veiculos() ));

		mudar_status_combobox(cbVeiculo);
	}

	public Boolean get_disponibilizacao_online() {
		if( rdOnline.isSelected() ) {
			return true;
		} else {
			return false;
		}
	}

	public Void realizar_relacionamentos(Publicacao publicacao_alvo, List<Integer> acessorios) {
		if( acessorios == null ) return null;

		for( int i = 0; i < acessorios.size(); ++i ) {
			publicacao_alvo.relacionar_a_acessorio( acessorios.get(i) );
		}

		return null;
	}

	private void upload_imagem(String arquivo_antigo, String nome_arquivo, File imagem) {
		if( nome_arquivo == null || imagem == null ) return;

		System.out.println( "Iniciando upload!" );
		Arquivo.replace("../img/uploads/publicacoes/", arquivo_antigo, nome_arquivo, imagem);
	}

	private void upload_imagens_publicacao() {
		System.out.println("Preparando Upload!");

		File imagemPrincipal = imagens_publicacao.get("imagemPrincipal");
        File imagemA = imagens_publicacao.get("imagemA");
        File imagemB = imagens_publicacao.get("imagemB");
        File imagemC = imagens_publicacao.get("imagemC");
        File imagemD = imagens_publicacao.get("imagemD");

        if( imagemPrincipal != null ) {
            String nome_arquivo_imagem_principal = "post_" + publicacao_alvo.getId() + "_imagem_principal." + FilenameUtils.getExtension( imagemPrincipal.getName() );
            upload_imagem(publicacao_alvo.getImagemPrincipal(), nome_arquivo_imagem_principal, imagemPrincipal);
            publicacao_alvo.setImagemPrincipal( nome_arquivo_imagem_principal );
        }

        if( imagemA != null ) {
            String nome_arquivo_imagem_a = "post_" + publicacao_alvo.getId() + "_imagem_a." + FilenameUtils.getExtension( imagemA.getName() );
            upload_imagem(publicacao_alvo.getImagemA(), nome_arquivo_imagem_a, imagemA);
            publicacao_alvo.setImagemA( nome_arquivo_imagem_a );
        }

        if( imagemB != null ) {
            String nome_arquivo_imagem_b = "post_" + publicacao_alvo.getId() + "_imagem_b." + FilenameUtils.getExtension( imagemB.getName() );
            upload_imagem(publicacao_alvo.getImagemB(), nome_arquivo_imagem_b, imagemB);
            publicacao_alvo.setImagemB( nome_arquivo_imagem_b );
        }

        if( imagemC != null ) {
            String nome_arquivo_imagem_c = "post_" + publicacao_alvo.getId() + "_imagem_c." + FilenameUtils.getExtension( imagemC.getName() );
            upload_imagem(publicacao_alvo.getImagemC(), nome_arquivo_imagem_c, imagemC);
            publicacao_alvo.setImagemC( nome_arquivo_imagem_c );
        }

        if( imagemD != null ) {
            String nome_arquivo_imagem_d = "post_" + publicacao_alvo.getId() + "_imagem_d." + FilenameUtils.getExtension( imagemD.getName() );
            upload_imagem(publicacao_alvo.getImagemD(), nome_arquivo_imagem_d, imagemD);
            publicacao_alvo.setImagemD( nome_arquivo_imagem_d );
        }
	}

	private class AcaoBotaoSelecaoImagem implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub

			String chave_identificadora_imagem = "";
			ImageView imagem_alvo = null;
			if( event.getSource() == btnImagemPrincipal ) {
				imagem_alvo = ivImagemPrincipal;
				chave_identificadora_imagem = "imagemPrincipal";
			} else if( event.getSource() == btnImagemA ) {
				imagem_alvo = ivImagemA;
				chave_identificadora_imagem = "imagemA";
			} else if( event.getSource() == btnImagemB ) {
				imagem_alvo = ivImagemB;
				chave_identificadora_imagem = "imagemB";
			} else if( event.getSource() == btnImagemC ) {
				imagem_alvo = ivImagemC;
				chave_identificadora_imagem = "imagemC";
			} else if( event.getSource() == btnImagemD ) {
				imagem_alvo = ivImagemD;
				chave_identificadora_imagem = "imagemD";
			}

			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter filtroJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
			FileChooser.ExtensionFilter filtroJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG");

			fileChooser.getExtensionFilters().addAll( filtroJPG, filtroJPEG );
			File arquivoImagem = fileChooser.showOpenDialog(null);
			
			if( arquivoImagem == null ) return;
			
			try {								
				BufferedImage buffer_imagem = ImageIO.read(arquivoImagem);
				Image imagem_selecionada = SwingFXUtils.toFXImage(buffer_imagem, null);
				imagem_alvo.setImage( imagem_selecionada );

				if( imagens_publicacao.containsKey(chave_identificadora_imagem) ) {
					imagens_publicacao.remove( chave_identificadora_imagem );
				}

				imagens_publicacao.put( chave_identificadora_imagem, arquivoImagem );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println( imagens_publicacao );
		}

	}

	private class AcaoTipoVeiculo implements EventHandler<Event> {

		@Override
		public void handle(Event event) {
			// TODO Auto-generated method stub
			TipoVeiculo tipo_selecionado = (TipoVeiculo) cbTipoVeiculo.getSelectionModel().getSelectedItem();
			if( tipo_selecionado == null ) return;

			id_tipo_selecionado = tipo_selecionado.getId();

			ThreadTask<List<CombustivelVeiculo>> task_combustiveis = new ThreadTask<List<CombustivelVeiculo>>(new Callable<List<CombustivelVeiculo>>() {

				@Override
				public List<CombustivelVeiculo> call() throws Exception {
					// TODO Auto-generated method stub
					return new CombustivelVeiculo().getCombustiveis(id_tipo_selecionado);
				}

			}, new CustomCallable<List<CombustivelVeiculo>>() {

				@Override
				public List<CombustivelVeiculo> call() throws Exception {
					// TODO Auto-generated method stub
					ComboBoxUtils.popular_combobox(cbCombustivel, (List<CombustivelVeiculo>) this.getParametro());
					mudar_status_combobox(cbCombustivel);
					return super.call();
				}

			});

			ThreadTask<List<FabricanteVeiculo>> task_fabricantes = new ThreadTask<List<FabricanteVeiculo>>( new Callable<List<FabricanteVeiculo>>() {

				@Override
				public List<FabricanteVeiculo> call() throws Exception {
					// TODO Auto-generated method stub
					return new FabricanteVeiculo().getFabricantes(id_tipo_selecionado);
				}

			}, new CustomCallable<List<FabricanteVeiculo>>() {

				@Override
				public List<FabricanteVeiculo> call() throws Exception {
					// TODO Auto-generated method stub
					ComboBoxUtils.popular_combobox(cbFabricante, (List<FabricanteVeiculo>) this.getParametro());
					mudar_status_combobox(cbFabricante);

					mudar_status_node(rdManual, true);
					mudar_status_node(rdAutomatico, true);
					mudar_status_node(sliderPortas, true);
					return super.call();
				}

			});

			task_combustiveis.run();
			task_fabricantes.run();
		}
	}

	private class AcaoFabricanteVeiculo implements EventHandler<Event> {

		@Override
		public void handle(Event arg0) {
			// TODO Auto-generated method stub
			FabricanteVeiculo fabricante_selecionado = (FabricanteVeiculo) cbFabricante.getSelectionModel().getSelectedItem();
			if( fabricante_selecionado == null ) return;

			id_fabricante = fabricante_selecionado.getId();

			filtrar_veiculos();
		}
	}

	private class AcaoCombustivelVeiculo implements EventHandler<Event> {

		@Override
		public void handle(Event arg0) {
			// TODO Auto-generated method stub
			CombustivelVeiculo tipo_combustivel = (CombustivelVeiculo) cbCombustivel.getSelectionModel().getSelectedItem();

			if( tipo_combustivel == null ) return;

			id_tipo_combustivel = tipo_combustivel.getId();

			filtrar_veiculos();
		}
	}

	private class AcaoTransmissaoVeiculo implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent arg0) {
			// TODO Auto-generated method stub
			Toggle toggle_selecionado = groupTransmissao.getSelectedToggle();

			if( toggle_selecionado == rdAutomatico ) {

				if( id_transmissao == AUTOMATICO ) {
					rdAutomatico.setSelected(false);
					id_transmissao = NAO_DEFINIDO;
				} else {
					id_transmissao = AUTOMATICO;
				}

			} else {

				if( id_transmissao == MANUAL ) {
					rdManual.setSelected(false);
					id_transmissao = NAO_DEFINIDO;
				} else {
					id_transmissao = MANUAL;
				}

			}

			filtrar_veiculos();
		}
	}

	private class AcaoQtdPortas implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			// TODO Auto-generated method stub
			numero_portas = (int) sliderPortas.getValue();

			if( sliderPortas.getValue() % 1 == 0 ) {
				filtrar_veiculos();
			}
		}
	}

	@FXML public void abrirSelecaoAcessorios(ActionEvent event) {
		int id_tipo_veiculo = ((TipoVeiculo) cbTipoVeiculo.getSelectionModel().getSelectedItem()).getId();

		Context.addData("idTipoVeiculo", id_tipo_veiculo);
		Context.addData("listaIdAcessorios", lista_acessorios);
		WindowManager.abrirModal("/view/FormularioSelecaoAcessorios.fxml", getClass());
	}

	@FXML public void salvarPublicacao(ActionEvent event) {

		String titulo = txtTitulo.getText().trim();
		String descricao = txtDescricao.getText().trim();
		Integer id_veiculo = ((Veiculo) cbVeiculo.getSelectionModel().getSelectedItem()).getId();
		Boolean disponivelOnline = get_disponibilizacao_online();

        BigDecimal precoMedio;
        int quilometragemAtual;
        BigDecimal valorQuilometragemExcedida;
        int limiteQuilometragem;
        BigDecimal valorDiaria;
        //BigDecimal valorCombustivel = null;

		try {

			precoMedio = BigDecimal.valueOf( FormValidator.get_input_as_double( txtPrecoMedio ) );
			quilometragemAtual = Integer.valueOf( txtQuilometragem.getText().trim() );
			valorQuilometragemExcedida = BigDecimal.valueOf( FormValidator.get_input_as_double( txtQuilometragemExcedida ) );
			limiteQuilometragem = Integer.valueOf( txtLimiteQuilometragem.getText().trim() );
			valorDiaria = BigDecimal.valueOf( FormValidator.get_input_as_double( txtValorDiaria ) );

			if( !txtValorCombustivel.getText().isEmpty() ) {
				//valorCombustivel = BigDecimal.valueOf( FormValidator.get_input_as_double( txtValorCombustivel ) );
			}

		} catch( NumberFormatException e ) {

			return;
		}

		Agencia agencia_alvo;
		if( Login.get_tipo_conta() == Login.JURIDICO ) {
			agencia_alvo = (Agencia) cbAgencia.getSelectionModel().getSelectedItem();
		} else {
			Funcionario funcionario_atual = new Funcionario().buscar("id = ?", Arrays.asList( Login.get_id_usuario() ), Funcionario.class ).get(0);
			agencia_alvo = new Agencia().buscar("id = ?", Arrays.asList( funcionario_atual.getIdAgencia() ), Agencia.class).get(0);
		}

		if( agencia_alvo == null ) return;

		Integer id_agencia = agencia_alvo.getId();

		lista_acessorios = Context.getListData("listaIdAcessorios");

		Login.get_id_juridico(new CustomCallable<Integer>() {

			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				Integer id_usuario_juridico = (Integer) this.getParametro();

				boolean modo_insercao = false;
				if( publicacao_alvo == null ) {
					publicacao_alvo = new Publicacao();
					modo_insercao = true;
				}

				publicacao_alvo.setTitulo( titulo );
				publicacao_alvo.setDescricao( descricao );
				publicacao_alvo.setPrecoMedio( precoMedio );
				publicacao_alvo.setQuilometragemAtual( quilometragemAtual );
				publicacao_alvo.setValorQuilometragem( valorQuilometragemExcedida );
				publicacao_alvo.setLimiteQuilometragem( limiteQuilometragem );
				publicacao_alvo.setValorDiaria( valorDiaria );

				if( !txtValorCombustivel.getText().isEmpty() ) {
					publicacao_alvo.setValorCombustivel( BigDecimal.valueOf( FormValidator.get_input_as_double( txtValorCombustivel ) ) );
				}

				publicacao_alvo.setDisponivelOnline( disponivelOnline );
				publicacao_alvo.setIdStatusPublicacao( STATUS_PUBLICACAO_DISPONIVEL );
				publicacao_alvo.setIdAgencia(id_agencia);
				publicacao_alvo.setIdUsuario( id_usuario_juridico );
				publicacao_alvo.setIdVeiculo(id_veiculo);

				if( Login.get_tipo_conta() == Login.FUNCIONARIO ) {
					publicacao_alvo.setIdFuncionario( Login.get_id_usuario() );
				} else {
					publicacao_alvo.setIdFuncionario(null);
				}

				Date data_atual = new Date();

				Timestamp timestamp = new Timestamp( data_atual.getTime() );
				publicacao_alvo.setDataPublicacao( timestamp );

				ThreadTask<Void> task_relacionamentos = new ThreadTask<Void>( new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						// TODO Auto-generated method stub
						publicacao_alvo.eliminar_relacionamentos_a_acessorios();
						return realizar_relacionamentos(publicacao_alvo, lista_acessorios);
					}

				}, new CustomCallable<Void>() {

					@Override
					public Void call() throws Exception {
						// TODO Auto-generated method stub
						WindowManager.fecharJanela( WindowManager.get_stage_de_componente(txtTitulo) );
						return super.call();
					}

				});

				ThreadTask<Boolean> task_atualizar_publicacao = new ThreadTask<Boolean>( new Callable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						// TODO Auto-generated method stub

						if( imagens_publicacao != null && imagens_publicacao.size() > 0 )
							upload_imagens_publicacao();

						return publicacao_alvo.atualizar();
					}

				}, new CustomCallable<Boolean>() {

					@Override
					public Boolean call() throws Exception {
						// TODO Auto-generated method stub
						task_relacionamentos.run();
						return super.call();
					}

				});

				ThreadTask<Integer> task_inserir_publicacao = new ThreadTask<Integer>( new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						// TODO Auto-generated method stub

						Integer id_publicacao_inserida = publicacao_alvo.inserir();
						publicacao_alvo.setId( id_publicacao_inserida );

						if( imagens_publicacao.size() > 0 )
							upload_imagens_publicacao();

						return id_publicacao_inserida;
					}
				}, new CustomCallable<Integer>() {
					@Override
					public Integer call() throws Exception {
						// TODO Auto-generated method stub
						Integer id_publicacao_inserida = (Integer) this.getParametro();

						publicacao_alvo.setId( id_publicacao_inserida );
						task_relacionamentos.run();
						imagens_publicacao = null;
						task_atualizar_publicacao.run();

						return super.call();
					}
				});

				if( modo_insercao ) {
					task_inserir_publicacao.runWithProgress( apConteudo );
				} else {
					task_atualizar_publicacao.runWithProgress( apConteudo );
				}

				return super.call();
			}
		});
	}

	@FXML public void removerPublicacao(ActionEvent event) {
		if( !AlertDialog.show("Remover", "Remover publicação", "Você tem certeza?", AlertType.CONFIRMATION) ) return;

		ThreadTask<Boolean> remover_publicacao = new ThreadTask<Boolean>(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				publicacao_alvo.eliminar_relacionamentos_a_acessorios();
				return publicacao_alvo.remover();
			}

		}, new CustomCallable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				WindowManager.fecharJanela( WindowManager.get_stage_de_componente(txtTitulo) );
				return super.call();
			}

		});

		remover_publicacao.runWithProgress( apConteudo );
	}

	@FXML public void cancelar(ActionEvent event) {
		WindowManager.fecharJanela( WindowManager.get_stage_de_componente(txtTitulo) );
	}

}
