package controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import dao.Veiculo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import model.CustomCallable;
import model.Login;

public class RelatorioVeiculosController implements Initializable {

	@FXML BarChart grafico;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
							
		Login.get_id_empresa(new CustomCallable<Integer>() {
			
			@Override
			public Integer call() throws Exception {
				// TODO Auto-generated method stub
											
				Integer id_empresa = (Integer) this.getParametro();														
				
				List<Map> dados_relatorio = new Veiculo().relatorio_mais_publicados("e.id = " + id_empresa);
				
				XYChart.Series series = new XYChart.Series<>();
				series.setName("Veiculos");
								
				for( int i = 0; i < dados_relatorio.size(); ++i ) {
					String nome_veiculo = (String) dados_relatorio.get(i).get("nome");
					Long quantidade_publicada = (Long) dados_relatorio.get(i).get("qtd");
					
					series.getData().add( new XYChart.Data(nome_veiculo, quantidade_publicada) );					
				}
				
				grafico.setTitle("Veículos mais publicados");
				grafico.getData().add( series );
				
				return super.call();
			}
			
		});		
		
	}

}
