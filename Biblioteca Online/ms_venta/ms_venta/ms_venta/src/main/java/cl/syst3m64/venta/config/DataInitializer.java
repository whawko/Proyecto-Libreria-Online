package cl.syst3m64.venta.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.syst3m64.venta.model.Venta;
import cl.syst3m64.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final VentaRepository ventaRepository;
    @Override
    public void run(String... args){
        if(ventaRepository.count() > 0){
            log.info("Ventas ya cargadas, se omite este archivo");
        }
        log.info("Cargando ventas...");

        ventaRepository.save(new Venta(null, "12/04/2026", new BigDecimal(3), 1L, 1L));
        ventaRepository.save(new Venta(null, "22/05/2015", new BigDecimal(6), 2L, 2L));
        ventaRepository.save(new Venta(null, "20/05/2026", new BigDecimal(7), 3L, 3L));

        log.info("Ventas cargadas");
    }
}