package cl.syst3m64.estado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EstadoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstadoApplication.class, args);
	}

}
