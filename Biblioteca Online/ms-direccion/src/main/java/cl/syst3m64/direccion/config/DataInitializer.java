package cl.syst3m64.direccion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Direccion;
import cl.syst3m64.direccion.repository.RegionRepository;
import cl.syst3m64.direccion.repository.ComunaRepository;
import cl.syst3m64.direccion.repository.DireccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;
    private final DireccionRepository direccionRepository;

    @Override
    public void run(String... args) {
        log.info("Iniciando DataInitializer de ms-direccion...");

        if (regionRepository.count() == 0) {
            log.info("Inicializando regiones y comunas semilla...");

            Region metropolitana = regionRepository.save(new Region(null, "Región Metropolitana"));
            Region valparaiso = regionRepository.save(new Region(null, "Valparaíso"));

            Comuna santiago = comunaRepository.save(new Comuna(null, "Santiago", metropolitana));
            Comuna providencia = comunaRepository.save(new Comuna(null, "Providencia", metropolitana));
            Comuna vina = comunaRepository.save(new Comuna(null, "Viña del Mar", valparaiso));
            Comuna valpo = comunaRepository.save(new Comuna(null, "Valparaíso", valparaiso));

            log.info("Regiones y comunas inicializadas correctamente.");

            if (direccionRepository.count() == 0) {
                log.info("Inicializando dirección de prueba...");
                direccionRepository.save(new Direccion(null, "Av. Providencia", 1234, 1L, providencia, 1L));
                log.info("Dirección de prueba inicializada.");
            }
        } else {
            log.info("ms-direccion ya cuenta con datos. Omitiendo inicialización.");
        }
    }
}
