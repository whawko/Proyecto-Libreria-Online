package cl.syst3m64.direccion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DireccionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DireccionApplication.class, args);
	}

}
