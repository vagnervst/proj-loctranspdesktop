package controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import dao.Agencia;
import dao.CNH;
import dao.Funcionario;
import dao.Pedido;
import dao.Publicacao;
import dao.Usuario;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.AlertDialog;
import model.ComboBoxUtils;
import model.Context;
import model.CustomCallable;
import model.Login;
import model.ThreadTask;
import view.WindowManager;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;

public class FormularioSolicitacaoController implements Initializable {
	
	@FXML TextField txtEmailLocatario;
	@FXML ComboBox cbAnuncios;
	@FXML ComboBox cbAgencias;
	@FXML ComboBox cbCnhLocatario;
	@FXML DatePicker dtRetirada;
	@FXML TextField txtHoraRetirada;
	@FXML TextField txtMinutoRetirada;
	@FXML DatePicker dtEntrega;
	@FXML TextField txtHoraEntrega;
	@FXML TextField txtMinutoEntrega;
	@FXML Label txtDiarias;
	@FXML Label txtValorTotal;	
	@FXML ToggleGroup groupFormaPagamento;
	@FXML RadioButton rdCartaoCredito;
	@FXML RadioButton rdDinheiro;	
	@FXML VBox conteudo;
	@FXML VBox vbConteudo;
	@FXML AnchorPane apConteudo;
	@FXML Button btnDefinirPendencias;
	@FXML TitledPane tpPagamento;
	
	private final int CARTAO_CREDITO = 1, DINHEIRO = 2;
	private final int ANO = 0, MES = 1, DIA = 2, HORA = 3, MINUTO = 4, SEGUNDO = 5;
	
	int idUsuario = Login.get_id_usuario();
	int idUsuarioJuridico;
	int idUsuarioLocatario;
	int forma_pagamento;
	
	Pedido pedido;
	@FXML Button btnConfirmarRetirada;
	@FXML Button btnConfirmarEntrega;
	@FXML Button btnPagarPendencias;
	@FXML Button btnDefinirLocatario;	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
					
		Login.get_id_juridico(new CustomCallable(){
			
			@Override
			public Object call() throws Exception {
				// TODO Auto-generated method stub
				int idJuridico = (int) getParametro();
				idUsuarioJuridico = idJuridico;
				return super.call();
			}
			
		});
		
		int idPedido = Context.getIntData("idPedido");
		if( idPedido != -1 ) {
			capturar_informacoes_pedido( idPedido );
		} else {
			preencher_agencias();
		}
		
		cbAgencias.setOnAction( new OnAgenciaChange() );
		
		dtRetirada.setOnAction( e -> {					
			calcular_diarias();		
		});
		
		dtEntrega.setOnAction( e -> {
			calcular_diarias();
		});
		
		rdCartaoCredito.setOnAction( e -> {				
			forma_pagamento = CARTAO_CREDITO;
		});
		
		rdDinheiro.setOnAction( e -> {
			forma_pagamento = DINHEIRO;
		});					
	}
			
	private String[] split_time(Timestamp time) {
		String string_time = time.toString();		
		string_time = string_time.replace(":", "-");	
		string_time = string_time.replace(" ", "-");
		String[] splitted_time = string_time.split("-");
		
		return splitted_time;
	}
	
	private LocalDate timestamp_to_localdate(Timestamp time) {				
		String[] splitted_time = split_time(time);	
		
		LocalDate data_retirada_picker = LocalDate.of(
				Integer.valueOf(splitted_time[ANO]), 
				Integer.valueOf(splitted_time[MES]), 
				Integer.valueOf(splitted_time[DIA]));
		
		return data_retirada_picker;
	}
	
	private void adaptar_componentes_status_pedido() {
		final int id_agendado = 1, id_aguardando_confirmacao_retirada = 3, id_aguardando_definicao_pendencias = 6, id_aguardando_pagamento_pendencias = 8, id_concluido = 9;
		int statusPedido = pedido.getIdStatusPedido();
		
		btnConfirmarRetirada.setDisable(true);
		btnConfirmarEntrega.setDisable(true);		
		btnDefinirPendencias.setDisable(true);
		btnPagarPendencias.setDisable(true);
		
		if( statusPedido >= id_agendado ) {
			btnDefinirLocatario.setDisable(true);
		}
		
		if( statusPedido >= id_agendado && statusPedido < id_aguardando_confirmacao_retirada ) {
			btnConfirmarRetirada.setDisable(false);
		}
		
		if( statusPedido >= id_aguardando_confirmacao_retirada && statusPedido < id_aguardando_definicao_pendencias ) {
			btnConfirmarEntrega.setDisable(false);
		}
		
		if( statusPedido >= id_aguardando_definicao_pendencias ) {
			btnDefinirPendencias.setDisable(false);			
			
			tpPagamento.setText("Pagamento de Pendências");
			
			if( pedido.getCombustivelRestante() != null && pedido.getQuilometragemExcedida() != null ) {
				btnDefinirPendencias.setText("Redefinir Pendências");
				btnPagarPendencias.setDisable(false);
			}
		}
		
		if( statusPedido == id_concluido ) {
			btnConfirmarRetirada.setDisable(true);
			btnConfirmarEntrega.setDisable(true);		
			btnDefinirPendencias.setDisable(true);
			btnPagarPendencias.setDisable(true);
		}
	}
	
	private void capturar_informacoes_pedido(int idPedido) {
		
		ThreadTask<Usuario> task_get_locatario = new ThreadTask<Usuario>( new Callable<Usuario>() {
			
			@Override
			public Usuario call() throws Exception {
				// TODO Auto-generated method stub
				return new Usuario().buscar("id = ?", Arrays.asList( pedido.getIdUsuarioLocatario() ), Usuario.class).get(0);
			}
			
		}, new CustomCallable<Usuario>() {
			
			@Override
			public Usuario call() throws Exception {
				// TODO Auto-generated method stub
				Usuario locatario = (Usuario) getParametro();
				txtEmailLocatario.setText( locatario.getEmail() );
				
				ComboBoxUtils.selecionar_item_combobox(cbCnhLocatario, "id = ?", Arrays.asList( pedido.getIdCnh() ), new CNH());
				cbCnhLocatario.setDisable(true);
				
				ComboBoxUtils.selecionar_item_combobox(cbAgencias, "id = ?", Arrays.asList( pedido.getIdAgencia() ), new Agencia());
				cbAgencias.setDisable(true);
				
				ComboBoxUtils.selecionar_item_combobox(cbAnuncios, "id = ?", Arrays.asList( pedido.getIdPublicacao() ), new Publicacao());
				cbAnuncios.setDisable(true);
				
				return super.call();
			}
			
		});
		
		ThreadTask<Pedido> task_get_pedido = new ThreadTask<Pedido>( new Callable<Pedido>() {
			
			@Override
			public Pedido call() throws Exception {
				// TODO Auto-generated method stub
				pedido = new Pedido();		
				return pedido.buscar("id = ?", Arrays.asList( idPedido ), Pedido.class).get(0);
			}
			
		}, new CustomCallable<Pedido>() {
			
			@Override
			public Pedido call() throws Exception {
				// TODO Auto-generated method stub
				pedido = (Pedido) getParametro();
				dtEntrega.setValue( timestamp_to_localdate(pedido.getDataRetirada()) );
				dtRetirada.setValue( timestamp_to_localdate(pedido.getDataEntrega()) );
				
				txtHoraRetirada.setText( split_time(pedido.getDataRetirada())[HORA] );
				txtMinutoRetirada.setText( split_time(pedido.getDataRetirada())[MINUTO] );
				txtHoraEntrega.setText( split_time(pedido.getDataEntrega())[HORA] );
				txtMinutoEntrega.setText( split_time(pedido.getDataEntrega())[MINUTO] );
				
				adaptar_componentes_status_pedido();
				task_get_locatario.run();
				return super.call();
			}
			
		});	
		
		task_get_pedido.run();
	}
	
	private void preencher_agencias() {
		Login.get_id_empresa(new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				int idEmpresa = (int) getParametro();
				
				ThreadTask<List<Agencia>> task_preencher_agencias = new ThreadTask<List<Agencia>>( new Callable<List<Agencia>>() {
					
					@Override
					public List<Agencia> call() throws Exception {
						// TODO Auto-generated method stub
						Agencia buscaAgencias = new Agencia();
						List<Agencia> lista_agencias = buscaAgencias.buscar("idEmpresa = ?", Arrays.asList( idEmpresa ), Agencia.class);
						
						return lista_agencias;
					}
					
				}, new CustomCallable<List<Agencia>>() {
					
					@Override
					public List<Agencia> call() throws Exception {
						// TODO Auto-generated method stub
						List<Agencia> lista_agencias = (List<Agencia>) getParametro();
						ComboBoxUtils.popular_combobox(cbAgencias, lista_agencias);
						return super.call();
					}
					
				});
				
				task_preencher_agencias.run();
				
				return super.call();
			}
			
		});
	}
	
	private void preencher_anuncios(int idAgencia) {
		
		ThreadTask<List<Publicacao>> task_preencher_anuncios = new ThreadTask<List<Publicacao>>( new Callable<List<Publicacao>>() {
			@Override
			public List<Publicacao> call() throws Exception {
				// TODO Auto-generated method stub
				Publicacao buscaPublicacao = new Publicacao();
				List<Publicacao> lista_anuncios = buscaPublicacao.buscar("idAgencia = ?", Arrays.asList( idAgencia ), Publicacao.class);
				
				return lista_anuncios;
			}
		}, new CustomCallable<List<Publicacao>>() {
			@Override
			public List<Publicacao> call() throws Exception {
				// TODO Auto-generated method stub
				List<Publicacao> lista_anuncios = (List<Publicacao>) getParametro();
				
				if( lista_anuncios.size() > 0 ) {	
					cbAnuncios.setDisable(false);
					ComboBoxUtils.popular_combobox(cbAnuncios, lista_anuncios);				
				} else {
					cbAnuncios.setDisable(true);
				}
				
				return super.call();
			}
		});
		
		task_preencher_anuncios.run();
	}
	
	private void preencher_email_locatario( int idLocatario ) {
		
		ThreadTask<Usuario> task_get_locatario = new ThreadTask<Usuario>( new Callable<Usuario>() {
			
			@Override
			public Usuario call() throws Exception {
				// TODO Auto-generated method stub
				Usuario buscaUsuario = new Usuario();				
				return buscaUsuario.buscar("id = ?", Arrays.asList( idLocatario ), Usuario.class).get(0);
			}
			
		}, new CustomCallable<Usuario>() {
			
			@Override
			public Usuario call() throws Exception {
				// TODO Auto-generated method stub
				Usuario locatario = (Usuario) getParametro();
				
				if( locatario != null ) {
					txtEmailLocatario.setText( locatario.getEmail() );
				}
				
				return super.call();
			}
			
		});
		
		task_get_locatario.run();
	}
	
	private void preencher_cnh_locatario(int idLocatario) {
		
		ThreadTask<List<CNH>> task_cnh_locatario = new ThreadTask<List<CNH>>(new Callable<List<CNH>>() {
			
			@Override
			public List<CNH> call() throws Exception {
				// TODO Auto-generated method stub
				CNH buscaCnh = new CNH();
				List<CNH> lista_cnh = buscaCnh.buscar("idUsuario = ? AND visivel = 1", Arrays.asList( idLocatario ), CNH.class);
				return lista_cnh;
			}
			
		}, new CustomCallable<List<CNH>>() {
			
			@Override
			public List<CNH> call() throws Exception {
				// TODO Auto-generated method stub
				List<CNH> lista_cnh = (List<CNH>) getParametro();
				ComboBoxUtils.popular_combobox(cbCnhLocatario, lista_cnh);				
				return super.call();
			}
			
		});
		
		task_cnh_locatario.run();
	}
	
	private void calcular_diarias() {

		if( dtRetirada.getValue() != null && dtRetirada.isDisabled() == false && 
				dtEntrega.getValue() != null && dtEntrega.isDisabled() == false &&
				!cbAnuncios.getSelectionModel().isEmpty() ) {
		
			Date data_retirada = Date.from( dtRetirada.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant() );
			Date data_entrega = Date.from( dtEntrega.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant() );				
			long diferenca_datas = data_entrega.getTime() - data_retirada.getTime();
			
			long diarias = TimeUnit.DAYS.convert(diferenca_datas, TimeUnit.MILLISECONDS)+1;
			
			BigDecimal valorDiaria = ( (Publicacao) cbAnuncios.getSelectionModel().getSelectedItem()).getValorDiaria();
			
			double valorTotal = diarias * valorDiaria.doubleValue();
			
			txtDiarias.setText( String.format("%d", diarias) );
			txtValorTotal.setText( String.format("R$%.2f", valorTotal) );	
		}
	}
	
	@FXML public void confirmarRetirada(ActionEvent event) {
		Context.addData("formaPagamento", forma_pagamento);
		Stage modalPagamentoDiaria = WindowManager.abrirModal("/view/PagamentoDiarias.fxml", getClass());
		
		modalPagamentoDiaria.setOnCloseRequest( e -> {
						
			boolean pagamentoConfirmado = Context.getBooleanData("isPagamentoConfirmado");
			System.out.println("Pagamento: " + pagamentoConfirmado);
			
			if( pagamentoConfirmado == true ) {							
				
				Publicacao publicacao_selecionada = (Publicacao) cbAnuncios.getSelectionModel().getSelectedItem();
				Agencia agencia_selecionada = (Agencia) cbAgencias.getSelectionModel().getSelectedItem();
				
				if( publicacao_selecionada == null || agencia_selecionada == null ) return;
				
				int horaRetirada, minutoRetirada, horaEntrega, minutoEntrega;				
				try {
					horaRetirada = Integer.valueOf(txtHoraRetirada.getText().trim());
					minutoRetirada = Integer.valueOf(txtMinutoRetirada.getText().trim());
					 
					horaEntrega = Integer.valueOf(txtHoraEntrega.getText().trim());
					minutoEntrega = Integer.valueOf(txtMinutoEntrega.getText().trim());
										 
					LocalDateTime ld_retirada = dtRetirada.getValue().atTime(horaRetirada, minutoRetirada);								
					LocalDateTime ld_entrega = dtEntrega.getValue().atTime(horaEntrega, minutoEntrega);
					
					Timestamp data_hora_retirada = Timestamp.valueOf( ld_retirada );
					Timestamp data_hora_entrega = Timestamp.valueOf( ld_entrega );														
					
					pedido = new Pedido();
					pedido.setValorDiaria( publicacao_selecionada.getValorDiaria() );
					pedido.setValorCombustivel( publicacao_selecionada.getValorCombustivel() );
					pedido.setValorQuilometragem( publicacao_selecionada.getValorQuilometragem() );
					pedido.setDataRetirada( data_hora_retirada );
					pedido.setDataEntrega( data_hora_entrega );
					pedido.setLocalRetiradaLocador( true );
					pedido.setLocalDevolucaoLocador( true );
					pedido.setLocalRetiradaLocatario( true );
					pedido.setLocalDevolucaoLocatario( true );
					pedido.setSolicitacaoRetiradaLocador( true );
					pedido.setSolicitacaoRetiradaLocatario( true );
					pedido.setIdPublicacao( publicacao_selecionada.getId() );
					pedido.setIdAgencia( agencia_selecionada.getId() );
					pedido.setIdUsuarioLocador( idUsuarioJuridico );
					pedido.setIdUsuarioLocatario( idUsuarioLocatario );						
					
					int id_status_aguardando_confirmacao_entrega = 5;
					pedido.setIdStatusPedido(id_status_aguardando_confirmacao_entrega);
					
					int id_pedido_local = 2;
					pedido.setIdTipoPedido( id_pedido_local );
					pedido.setIdFormaPagamento( forma_pagamento );
					
					if( Login.get_tipo_conta() == Login.FUNCIONARIO ) {
						pedido.setIdFuncionario( idUsuario );			
					}
					
					int id_cnh_selecionada = ( (CNH) cbCnhLocatario.getSelectionModel().getSelectedItem()).getId();
					pedido.setIdCnh( id_cnh_selecionada );
					
					pedido.setIdVeiculo( publicacao_selecionada.getIdVeiculo() );																		
					
					ThreadTask<Integer> task_salvar_pedido = new ThreadTask<Integer>( new Callable<Integer>() {

						@Override
						public Integer call() throws Exception {
							// TODO Auto-generated method stub
							return pedido.inserir();
						}
						
					}, new CustomCallable<Integer>() {
						
						@Override
						public Integer call() throws Exception {
							// TODO Auto-generated method stub
							int id_pedido_inserido = (int) getParametro();
														
							if( id_pedido_inserido == -1 ) {
								AlertDialog.show("Erro", "Houve um erro", "Ocorreu uma falha na operação de inserção do pedido.", AlertType.ERROR);
							} else {
								AlertDialog.show("Sucesso", "Operação bem sucedida", "Pedido inserido com sucesso.", AlertType.INFORMATION);
								WindowManager.fecharJanela( WindowManager.get_stage_de_componente( txtDiarias ) );
							}
							
							return null;
						}						
					});
										
					task_salvar_pedido.runWithProgress( apConteudo );
					
				} catch( NumberFormatException erro ) {
					AlertDialog.show("Erro!", "Ocorreu um erro", "Horário invalido", AlertType.ERROR);
				}							
			}
			
		});
	}

	@FXML public void confirmarDevolucao(ActionEvent event) {
		Context.addData("idPedido", pedido.getId());
		Stage janelaDevolucao = WindowManager.abrirModal("/view/ConfirmarEntrega.fxml", getClass());
		
		janelaDevolucao.setOnCloseRequest( e -> {
			
			boolean confirmacaoEntrega = Context.getBooleanData("confirmacaoEntrega");
			if( confirmacaoEntrega == false ) return;
			
			int id_status_aguardando_definicao_pendencias = 6;
			pedido.setSolicitacaoDevolucaoLocador( true );
			pedido.setSolicitacaoDevolucaoLocatario( true );
			pedido.setIdStatusPedido( id_status_aguardando_definicao_pendencias );			
			
			ThreadTask<Boolean> task_entrega_pedido = new ThreadTask<Boolean>( new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					return !pedido.atualizar();
				}
											
			}, new CustomCallable<Boolean>() {
				
				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					boolean resultado = (boolean) getParametro();					
					
					if( resultado ) {
						AlertDialog.show("Sucesso", "Operação bem sucedida", "Entrega efetuada com sucesso", AlertType.INFORMATION);					
					} else {
						AlertDialog.show("Erro", "Falha na operação", "Houve um erro na execução da entrega", AlertType.ERROR);
					}
					
					return super.call();
				}
				
			});
									
			task_entrega_pedido.runWithProgress( apConteudo );
		});
	}

	@FXML public void definirPendencias(ActionEvent event) {
		Context.addData("idPedido", pedido.getId());
		Stage modalPendencias = WindowManager.abrirModal("/view/FormularioDefinirPendencias.fxml", getClass());
		
		modalPendencias.setOnCloseRequest( e -> {
			capturar_informacoes_pedido( pedido.getId() );
		});
	}
	
	@FXML public void definirLocatario(ActionEvent event) {
		Stage modalLoginLocatario = WindowManager.abrirModal("/view/LoginLocatario.fxml", getClass());
		
		modalLoginLocatario.setOnCloseRequest( e -> {
			
			idUsuarioLocatario = Context.getIntData("idUsuarioLocatario");			
			
			if( idUsuarioLocatario != -1 ) {
				
				preencher_email_locatario(idUsuarioLocatario);
				preencher_cnh_locatario(idUsuarioLocatario);
			}
		});
	}
	
	@FXML public void pagarPendencias(ActionEvent event) {
		Context.addData("idPedido", pedido.getId());
		Context.addData("formaPagamento", forma_pagamento);
		Stage modalPagamentoPendencias = WindowManager.abrirModal("/view/PagamentoPendencias.fxml", getClass());
		
		modalPagamentoPendencias.setOnCloseRequest( e -> {
			Boolean is_pendencia_paga = Context.getBooleanData("pagamentoPendencia");
			
			if( is_pendencia_paga != null && is_pendencia_paga == true ) {
				
			}
		});
	}
	
	private class OnAgenciaChange implements EventHandler<Event> {

		@Override
		public void handle(Event event) {
			// TODO Auto-generated method stub
			
			Agencia agenciaSelecionada = (Agencia) cbAgencias.getSelectionModel().getSelectedItem();
			preencher_anuncios( agenciaSelecionada.getId() );
			
		}
		
	}
}
