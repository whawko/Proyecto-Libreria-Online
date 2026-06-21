package cl.syst3m64.estado.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.syst3m64.estado.model.Estado;
import cl.syst3m64.estado.model.TipoEstado;
import cl.syst3m64.estado.repository.EstadoRepository;
import cl.syst3m64.estado.repository.TipoEstadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final EstadoRepository estadoRepository;
    private final TipoEstadoRepository tipoEstadoRepository;

    @Override
    public void run(String... args){
        if(estadoRepository.count() > 0){
            //log.error("Log Problema");
            //log.warn("Log Warn");
            log.info("Estados cargados. No se ejecuta el archivo");
            //log.debug("Log Debug");
            //log.trace("Log Trace");
            return;
        }
        log.info("Cargando estados preconfigurados...");

        TipoEstado venta = tipoEstadoRepository.save(new TipoEstado(null, "VENTA"));

        TipoEstado arriendo = tipoEstadoRepository.save(new TipoEstado(null, "ARRIENDO"));

        estadoRepository.save(new Estado(null, "ACTIVO", "Venta activa", venta));

        estadoRepository.save(new Estado(null, "CANCELADA", "Venta cancelada", venta));

        estadoRepository.save(new Estado(null, "PAGADA", "Venta pagada", venta));

        estadoRepository.save(new Estado(null, "EN_CURSO", "Arriendo en proceso", arriendo));

        estadoRepository.save(new Estado(null, "FINALIZADO", "Arriendo terminado", arriendo));


        log.info("Estados preconfigurados cargados exitosamente");
    }
}
