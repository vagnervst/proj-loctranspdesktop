package controller;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import dao.Veiculo;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import model.CustomCallable;
import model.Login;
import model.Relatorios;
import model.ThreadTask;
import javafx.scene.layout.AnchorPane;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Label;

public class RelatorioPublicacaoController implements Initializable {

	@FXML BarChart graficoVeiculo;	
	@FXML AnchorPane apVeiculos;
	@FXML Tab tabVeiculo;
	@FXML BarChart graficoFuncionario;	
	@FXML BarChart graficoAgencia;
	
	int id_empresa;
	@FXML PieChart graficoPublicacoes;
	@FXML Label lblTotalPublicacoes;	
	@FXML Label lblPublicacoesIndisponiveis;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		Login.get_id_empresa(new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				id_empresa = (int) this.getParametro();
				
				preparar_grafico_publicacao_veiculo();
				preparar_grafico_publicacao_agencia();
				preparar_grafico_publicacao_funcionario();
				preparar_grafico_publicacoes_disponiveis();
				return super.call();
			}
			
		});			
	}
		
	
	public void preparar_grafico_publicacao_veiculo() {				
		
		ThreadTask<List<Map>> task_veiculo = new ThreadTask<List<Map>>( new Callable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return new Relatorios().relatorio_veiculos_mais_publicados("e.id = " + id_empresa);
			}
			
		}, new CustomCallable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				List<Map> dados_relatorio = (List<Map>) this.getParametro();
				
				XYChart.Series estatisticas_veiculos = new XYChart.Series<>();
				estatisticas_veiculos.setName("Veiculos");
				
				for( int i = 0; i < dados_relatorio.size(); ++i ) {
					String nome_veiculo = (String) dados_relatorio.get(i).get("nome");
					Long quantidade_publicada = (Long) dados_relatorio.get(i).get("qtd");
					
					estatisticas_veiculos.getData().add( new XYChart.Data(nome_veiculo, quantidade_publicada) );					
				}
		
				graficoVeiculo.getData().add( estatisticas_veiculos );
				
				return super.call();
			}
			
		});
						
		task_veiculo.run();	
		
	}
	
	public void preparar_grafico_publicacao_agencia() {
		
		ThreadTask<List<Map>> task = new ThreadTask<List<Map>>(new Callable<List<Map>>() {

			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return new Relatorios().relatorio_publicacoes_agencia("e.id = " + id_empresa);
			}
			
		}, new CustomCallable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				List<Map> dados_relatorio_agencia = (List<Map>) this.getParametro();				
				
				XYChart.Series estatisticas_agencias = new XYChart.Series<>();
				estatisticas_agencias.setName("Agências");
											
				for( int i = 0; i < dados_relatorio_agencia.size(); ++i ) {
					String titulo_agencia = (String) dados_relatorio_agencia.get(i).get("titulo");
					Long qtd_publicacoes = (Long) dados_relatorio_agencia.get(i).get("qtdPublicacoes");					
					
					XYChart.Data dados_agencia = new XYChart.Data(titulo_agencia, qtd_publicacoes);
					System.out.println( titulo_agencia + " : " + qtd_publicacoes );
					estatisticas_agencias.getData().add( dados_agencia );
				}							
								
				graficoAgencia.getData().add( estatisticas_agencias );
				
				return super.call();
			}
			
		});
		
		task.run();
	}
	
	public void preparar_grafico_publicacao_funcionario() {
		
		ThreadTask<List<Map>> task = new ThreadTask<List<Map>>(new Callable<List<Map>>() {

			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return new Relatorios().relatorio_publicacoes_funcionario("f.idEmpresa = " + id_empresa);
			}
			
		}, new CustomCallable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				List<Map> dados_relatorio_funcionario = (List<Map>) this.getParametro();				
				
				XYChart.Series estatisticas_funcionarios = new XYChart.Series<>();
				estatisticas_funcionarios.setName("Funcionários");
											
				for( int i = 0; i < dados_relatorio_funcionario.size(); ++i ) {
					String nome_funcionario = (String) dados_relatorio_funcionario.get(i).get("nome");
					Long qtd_publicacoes = (Long) dados_relatorio_funcionario.get(i).get("qtdPublicacoes");					
					
					XYChart.Data dados_funcionario = new XYChart.Data(nome_funcionario, qtd_publicacoes);
					System.out.println( nome_funcionario + " : " + qtd_publicacoes );
					estatisticas_funcionarios.getData().add( dados_funcionario );
				}							
								
				graficoFuncionario.getData().add( estatisticas_funcionarios );
				
				return super.call();
			}
			
		});
		
		task.run();		
	}
	
	public void preparar_grafico_publicacoes_disponiveis() {
		
		ThreadTask<Map<Long, Long>> task = new ThreadTask<Map<Long, Long>>( new Callable<Map<Long, Long>>() {

			@Override
			public Map<Long, Long> call() throws Exception {
				// TODO Auto-generated method stub
				return new Relatorios().relatorio_publicacoes_disponiveis_indisponiveis( "e.id = " + id_empresa );
			}
			
		}, new CustomCallable<Map<Long, Long>>() {
			
			@Override
			public Map<Long, Long> call() throws Exception {
				// TODO Auto-generated method stub
				Map<Long, Long> dados_publicacoes = (Map<Long, Long>) this.getParametro();
				
				ObservableList<PieChart.Data> dados_grafico = FXCollections.observableArrayList();
								
				PieChart.Data publicacoes_disponiveis = new PieChart.Data("Publicações Disponíveis", dados_publicacoes.get("publicacoesDisponiveis"));								
				
				PieChart.Data publicacoes_indisponiveis = new PieChart.Data("Publicações Indisponíveis", dados_publicacoes.get("publicacoesIndisponiveis"));				
				
				dados_grafico.addAll( publicacoes_disponiveis, publicacoes_indisponiveis );
								
				graficoPublicacoes.setData( dados_grafico );
				graficoPublicacoes.setTitle("Publicações Disponíveis");				
				
				lblTotalPublicacoes.setText( String.valueOf( dados_publicacoes.get("publicacoesDisponiveis") ) );
				lblPublicacoesIndisponiveis.setText( String.valueOf( dados_publicacoes.get("publicacoesIndisponiveis") ) );
				
				return super.call();
			}
			
		});
		
		task.run();
	}
	
}
