package br.com.exaple.NotaFiscal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotaFiscalApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotaFiscalApplication.class, args);
	}

	//@Bean
	public void inicioPrograma()
	{
		Inicio inicio = new Inicio();
		inicio.start();
	}

	@Bean
	public void inicioProgramagoogle()
	{
		ApiGoogleSheets apiGoogleSheets = new ApiGoogleSheets();
		apiGoogleSheets.start();

	}

}
