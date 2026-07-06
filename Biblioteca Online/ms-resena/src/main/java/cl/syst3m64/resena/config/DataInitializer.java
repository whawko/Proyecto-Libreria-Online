package cl.syst3m64.resena.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import cl.syst3m64.resena.model.Resena;
import cl.syst3m64.resena.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ResenaRepository resenaRepository;

    @Override
    public void run(String... args) {
        log.info("Iniciando DataInitializer de ms-resena...");

        if (resenaRepository.count() == 0) {
            log.info("Inicializando resenas semilla...");

            resenaRepository.save(new Resena(
                null,
                1L,
                1L,
                "Excelente libro",
                "Una lectura obligatoria. Me encantó la trama y el desarrollo de personajes.",
                5,
                LocalDate.now(),
                "ACTIVO"
            ));

            resenaRepository.save(new Resena(
                null,
                2L,
                1L,
                "Muy bueno",
                "El ritmo del libro es rápido y entretenido, ideal para leer el fin de semana.",
                4,
                LocalDate.now(),
                "ACTIVO"
            ));

            log.info("Resenas de prueba inicializadas correctamente.");
        } else {
            log.info("ms-resena ya cuenta con datos. Omitiendo inicialización.");
        }
    }
}
