package model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;

public class Arquivo {
	
	public static void upload( String pasta, String nome_arquivo, File imagem ) {
		if( imagem == null ) return;
		
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost( Server.address + "apis/upload_imagem.php" );
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addPart("imagem", new FileBody( imagem ));
		builder.addTextBody("dir", pasta);
		builder.addTextBody("filename", nome_arquivo);
		
		post.setEntity( builder.build() );	
		
		try {
			HttpResponse resposta = client.execute( post );							
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void replace( String pasta, String arquivo_antigo, String nome_arquivo, File imagem ) {
		if( imagem == null ) return;
		
		System.out.println( nome_arquivo + " : " + arquivo_antigo );
		
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost( Server.address + "apis/upload_imagem.php" );
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addPart("imagem", new FileBody( imagem ));
		builder.addTextBody("dir", pasta);
		
		if( arquivo_antigo != null )
			builder.addTextBody("oldfile", arquivo_antigo);
		
		builder.addTextBody("filename", nome_arquivo);		
		
		post.setEntity( builder.build() );	
		
		try {
			HttpResponse resposta = client.execute( post );
			
			System.out.println( IOUtils.toString( resposta.getEntity().getContent(), "UTF-8") );
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String httpUrlFromFile(File file) {
		String result = file.toURI().toString();
		result = result.substring( result.indexOf("http") );		
		result = result.replace("http:/", "http://");
		
		return result;
	}
	
}
