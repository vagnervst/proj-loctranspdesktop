package controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import model.CustomCallable;
import model.Login;
import model.Relatorios;
import model.ThreadTask;

public class RelatorioLocacoesController implements Initializable {

	@FXML BarChart graficoVeiculos;
	@FXML BarChart graficoValores;
	@FXML BarChart graficoPendencias;
	@FXML PieChart graficoLocacoes;
	
	int id_empresa;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		Login.get_id_empresa(new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
				id_empresa = (int) this.getParametro();
				
				preparar_grafico_locacoes_veiculo();
				preparar_grafico_locacoes_diaria();
				return super.call();
			}
			
		});
		
	}	
	
	public void preparar_grafico_locacoes_veiculo() {
		
		ThreadTask<List<Map>> task = new ThreadTask<List<Map>>( new Callable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return new Relatorios().relatorio_veiculos_alugados( "e.id = " + id_empresa );
			}
			
		}, new CustomCallable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				List<Map> lista_veiculos = (List<Map>) this.getParametro();
								
				XYChart.Series estatisticas_veiculos = new XYChart.Series<>();				
				estatisticas_veiculos.setName("Veículos");
				
				for( int i = 0; i < lista_veiculos.size(); ++i ) {
					String nome_veiculo = (String) lista_veiculos.get(i).get("nome");
					Long qtdLocacoes = (Long) lista_veiculos.get(i).get("qtdAlugados");
					
					estatisticas_veiculos.getData().add( new XYChart.Data(nome_veiculo, qtdLocacoes) );
				}
				
				graficoVeiculos.getData().add( estatisticas_veiculos );
				
				return super.call();
			}
			
		});
		
		task.run();
	}
	
	public void preparar_grafico_locacoes_diaria() {
		
		ThreadTask<List<Map>> task = new ThreadTask<List<Map>>( new Callable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				return new Relatorios().relatorio_locacao_diaria( "e.id = " + id_empresa );
			}
			
		}, new CustomCallable<List<Map>>() {
			
			@Override
			public List<Map> call() throws Exception {
				// TODO Auto-generated method stub
				List<Map> valores_diarias = (List<Map>) this.getParametro();
				
				XYChart.Series dados_grafico = new XYChart.Series<>();
				
				for( int i = 0; i < valores_diarias.size(); ++i ) {
					BigDecimal valor_diaria = (BigDecimal) valores_diarias.get(i).get("valorDiaria");
					Long quantidade_locacoes = (Long) valores_diarias.get(i).get("qtdPedidos");
										
					dados_grafico.getData().add( new XYChart.Data<>(valor_diaria.toString(), quantidade_locacoes) );
				}
				
				graficoValores.getData().add( dados_grafico );
				
				return super.call();
			}
			
		});
		
		task.run();
	}
	
}
