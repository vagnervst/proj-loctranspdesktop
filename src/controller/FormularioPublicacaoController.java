package controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import dao.CategoriaVeiculo;
import dao.CombustivelVeiculo;
import dao.FabricanteVeiculo;
import dao.TipoVeiculo;
import dao.Veiculo;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import model.ComboBoxUtils;
import model.Context;
import view.WindowManager;

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
	@FXML ComboBox cbCategoria;
	@FXML ComboBox cbVeiculo;
	@FXML ComboBox cbAgencia;
	@FXML ComboBox cbCombustivel;
	@FXML ComboBox cbTipoVeiculo;
	@FXML ToggleGroup groupDisponibilizacao;
	@FXML ComboBox cbFabricante;

	int id_tipo_selecionado;
	int id_fabricante;
	int id_tipo_combustivel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ComboBoxUtils.popular_combobox( cbTipoVeiculo, new TipoVeiculo() );		
		ComboBoxUtils.popular_combobox( cbFabricante, new FabricanteVeiculo() );
		
		cbCategoria.setDisable(true);
		cbFabricante.setDisable(true);
		cbVeiculo.setDisable(true);
		cbCombustivel.setDisable(true);
		
		cbTipoVeiculo.setOnAction( new AcaoTipoVeiculo() );
		cbFabricante.setOnAction( new AcaoFabricanteVeiculo() );
		cbCombustivel.setOnAction( new AcaoCombustivelVeiculo() );
		
		sliderPortas.setMin(0);
		sliderPortas.setValue(0);
		sliderPortas.setShowTickLabels(true);
		sliderPortas.setShowTickMarks(true);
		sliderPortas.setMinorTickCount(0);
		sliderPortas.setMajorTickUnit(2);
		sliderPortas.setSnapToTicks(true);
		sliderPortas.setMax(8);
	}
	
	private void mudar_status_combobox(ComboBox cb) {
		if( cb.getItems().size() > 0 ) {
			cb.setDisable(false);
		} else {
			cb.setDisable(true);
		}
	}
	
	private class AcaoTipoVeiculo implements EventHandler<Event> {

		@Override
		public void handle(Event event) {
			// TODO Auto-generated method stub
			TipoVeiculo tipo_selecionado = (TipoVeiculo) cbTipoVeiculo.getSelectionModel().getSelectedItem();
			if( tipo_selecionado == null ) return;
			
			id_tipo_selecionado = tipo_selecionado.getId();
			
			ComboBoxUtils.popular_combobox(cbCategoria, new CategoriaVeiculo().buscar("idTipoVeiculo = ?", Arrays.asList( id_tipo_selecionado ), CategoriaVeiculo.class));
			ComboBoxUtils.popular_combobox(cbFabricante, new FabricanteVeiculo().getFabricantes(id_tipo_selecionado));
			
			mudar_status_combobox(cbCategoria);
			mudar_status_combobox(cbFabricante);
			
			cbCombustivel.setDisable( true );
		}		
	}
	
	private class AcaoFabricanteVeiculo implements EventHandler<Event> {

		@Override
		public void handle(Event arg0) {
			// TODO Auto-generated method stub
			FabricanteVeiculo fabricante_selecionado = (FabricanteVeiculo) cbFabricante.getSelectionModel().getSelectedItem();
			if( fabricante_selecionado == null ) return;
			
			id_fabricante = fabricante_selecionado.getId();
			
			ComboBoxUtils.popular_combobox(cbVeiculo, new Veiculo().getVeiculos("fv.id = " + id_fabricante));
			ComboBoxUtils.popular_combobox(cbCombustivel, new CombustivelVeiculo().getCombustiveis(id_tipo_selecionado));
			
			mudar_status_combobox(cbVeiculo);
			mudar_status_combobox(cbCombustivel);
		}
		
	}
	
	private class AcaoCombustivelVeiculo implements EventHandler<Event> {

		@Override
		public void handle(Event arg0) {
			// TODO Auto-generated method stub
			id_tipo_combustivel = ( (CombustivelVeiculo) cbCombustivel.getSelectionModel().getSelectedItem() ).getId();
			
			ComboBoxUtils.popular_combobox(cbVeiculo, new Veiculo().getVeiculos("fv.id = " + id_fabricante + " AND tc.id = " + id_tipo_combustivel));
			mudar_status_combobox(cbVeiculo);
		}
		
	}
	
	@FXML public void abrirSelecaoAcessorios(ActionEvent event) {
		int id_tipo_veiculo = ((TipoVeiculo) cbTipoVeiculo.getSelectionModel().getSelectedItem()).getId();
		
		Context.addData("idTipoVeiculo", id_tipo_veiculo);
		WindowManager.abrirModal("/view/FormularioSelecaoAcessorios.fxml", getClass());
	}
	
}
