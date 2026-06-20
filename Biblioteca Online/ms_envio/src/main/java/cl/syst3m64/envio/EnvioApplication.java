package cl.syst3m64.envio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EnvioApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnvioApplication.class, args);
    }
}
