package cl.syst3m64.carrito.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import cl.syst3m64.carrito.model.Carrito;
import cl.syst3m64.carrito.repository.CarritoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CarritoRepository carritoRepository;

    @Override
    public void run(String... args) {
        log.info("Iniciando DataInitializer de ms-carrito...");

        if (carritoRepository.count() == 0) {
            log.info("Inicializando carrito semilla...");

            BigDecimal price1 = new BigDecimal("15000.00");
            BigDecimal price2 = new BigDecimal("20000.00");

            carritoRepository.save(new Carrito(null, 1L, 1L, 2, price1, price1.multiply(BigDecimal.valueOf(2))));
            carritoRepository.save(new Carrito(null, 1L, 2L, 1, price2, price2.multiply(BigDecimal.valueOf(1))));

            log.info("Carrito de prueba inicializado correctamente.");
        } else {
            log.info("ms-carrito ya cuenta con datos. Omitiendo inicialización.");
        }
    }
}
