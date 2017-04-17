package model;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

public class FormValidator {

	public static void limitar_caracteres( TextInputControl input, int limite ) {
		
		input.setTextFormatter( new TextFormatter<>(c -> {
			
			if( c.getControlNewText().isEmpty() ) {
				return c;
			}
			
			if( c.getControlNewText().length() >= limite ) {
				return null;
			} else {
				return c;
			}
			
		}));
							
	}
	
	public static void limitar_para_inteiro( TextInputControl input, int tamanho ) {
		
		Pattern integerPattern = Pattern.compile("\\d+");
		
		input.setTextFormatter( new TextFormatter<>( c -> {
			
			if( c.getControlNewText().isEmpty() ) {
				return c;
			}
			
			if( integerPattern.matcher( c.getControlNewText() ).matches() && c.getControlNewText().length() <= tamanho ) {
				return c;
			} else {
				return null;
			}
			
		}));
	}
	
	public static void limitar_para_decimal( TextInputControl input, int casas_inteiras, int casas_decimais ) {
		
		Pattern doublePattern = Pattern.compile("\\d+((\\,{1,1})?)+((\\d+)?)");
		
		input.setTextFormatter( new TextFormatter<>( c -> {
			
			if( c.getControlNewText().isEmpty() ) {
				return c;
			}
			
			int inteiras = -1;
			int decimais = -1;
			if( c.getControlNewText().indexOf(",") == -1 ) {
				inteiras = c.getControlNewText().length();
				decimais = 0;
			} else {			
				inteiras = c.getControlNewText().indexOf(",");
				decimais = c.getControlNewText().length() - inteiras - 1;
			}
			
			if( doublePattern.matcher( c.getControlNewText() ).matches() && inteiras <= casas_inteiras && decimais <= casas_decimais ) {
				return c;
			} else {
				return null;
			}
			
		}));
	}
	
	public static double get_input_as_double( TextInputControl input ) {
		String valor = input.getText().trim();
		valor = valor.replace(",", ".");
		
		return Double.valueOf( valor );
	}
	
	public static String double_to_string( BigDecimal valor ) {
		String double_formatado = String.valueOf( valor );
		return double_formatado.replace(".", ",");
	}
	
}