package cl.syst3m64.envio.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import cl.syst3m64.envio.model.Envio;
import cl.syst3m64.envio.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EnvioRepository envioRepository;

    @Override
    public void run(String... args) {
        log.info("Iniciando DataInitializer de ms-envio...");

        if (envioRepository.count() == 0) {
            log.info("Inicializando envio semilla...");

            envioRepository.save(new Envio(
                null,
                1L,
                1L,
                "TRACK-777666",
                "Starken",
                "2026-07-04",
                4L
            ));

            log.info("Envio de prueba inicializado correctamente.");
        } else {
            log.info("ms-envio ya cuenta con datos. Omitiendo inicialización.");
        }
    }
}
