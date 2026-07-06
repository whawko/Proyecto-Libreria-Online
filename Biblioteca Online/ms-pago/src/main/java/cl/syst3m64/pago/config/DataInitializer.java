package cl.syst3m64.pago.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import cl.syst3m64.pago.model.Pago;
import cl.syst3m64.pago.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PagoRepository pagoRepository;

    @Override
    public void run(String... args) {
        log.info("Iniciando DataInitializer de ms-pago...");

        if (pagoRepository.count() == 0) {
            log.info("Inicializando pago semilla...");

            pagoRepository.save(new Pago(
                null,
                1L,
                new BigDecimal("30000.00"),
                "TARJETA",
                "TRANS-987654",
                "2026-07-04",
                3L
            ));

            log.info("Pago de prueba inicializado correctamente.");
        } else {
            log.info("ms-pago ya cuenta con datos. Omitiendo inicialización.");
        }
    }
}
