package cl.syst3m64.categorias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CategoriasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CategoriasApplication.class, args);
	}

}
