package cl.syst3m64.envio.service.impl;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.syst3m64.envio.dto.DireccionResponseDTO;
import cl.syst3m64.envio.dto.EnvioRequestDTO;
import cl.syst3m64.envio.dto.EnvioResponseDTO;
import cl.syst3m64.envio.dto.VentaResponseDTO;
import cl.syst3m64.envio.client.DireccionFeignClient;
import cl.syst3m64.envio.client.VentaFeignClient;
import cl.syst3m64.envio.exception.RecursoNoEncontradoException;
import cl.syst3m64.envio.exception.ServicioExternoNoDisponibleException;
import cl.syst3m64.envio.model.Envio;
import cl.syst3m64.envio.repository.EnvioRepository;
import cl.syst3m64.envio.service.EnvioService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;
    private final VentaFeignClient ventaFeignClient;
    private final DireccionFeignClient direccionFeignClient;

    @Override
    @Transactional
    public EnvioResponseDTO registrarEnvio(EnvioRequestDTO request) {
        // 1. Validar que la venta exista en ms_venta
        VentaResponseDTO venta;
        try {
            venta = ventaFeignClient.obtenerVentaPorId(request.getIdVenta());
            if (venta == null) {
                throw new RecursoNoEncontradoException("La venta con ID " + request.getIdVenta() + " no fue encontrada.");
            }
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("La venta con ID " + request.getIdVenta() + " no existe.");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms-venta: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al validar la venta con ID " + request.getIdVenta() + ": " + e.getMessage());
        }

        // 1.5 Validar que la venta esté PAGADA (idEstado = 3L)
        if (venta.getIdEstado() == null || !venta.getIdEstado().equals(3L)) {
            throw new IllegalArgumentException("La venta con ID " + request.getIdVenta() + " no está pagada (estado actual: " + venta.getIdEstado() + "). No se puede registrar el envío.");
        }

        // 2. Validar que la dirección exista en ms_direccion
        DireccionResponseDTO direccion;
        try {
            direccion = direccionFeignClient.obtenerDireccionPorId(request.getIdDireccion());
            if (direccion == null) {
                throw new RecursoNoEncontradoException("La dirección con ID " + request.getIdDireccion() + " no fue encontrada.");
            }
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("La dirección con ID " + request.getIdDireccion() + " no existe.");
        } catch (FeignException e) {
            throw new ServicioExternoNoDisponibleException("No se puede conectar con el microservicio ms-direccion: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al validar la dirección con ID " + request.getIdDireccion() + ": " + e.getMessage());
        }

        // 3. Verificar si ya existe un despacho registrado para esta venta
        Optional<Envio> envioExistente = envioRepository.findByIdVenta(request.getIdVenta());
        if (envioExistente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un despacho registrado para la venta con ID: " + request.getIdVenta());
        }

        // 4. Crear y guardar el registro de envío
        Envio envio = new Envio();
        envio.setIdVenta(request.getIdVenta());
        envio.setIdDireccion(request.getIdDireccion());
        envio.setNumeroSeguimiento(request.getNumeroSeguimiento());
        envio.setEmpresaEnvio(request.getEmpresaEnvio());
        envio.setFechaDespacho(request.getFechaDespacho());
        envio.setIdEstado(1L); // 1L represents 'PENDING' or 'PREPARING' shipping state

        Envio saved = envioRepository.save(envio);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnvioResponseDTO> obtenerEnvioPorVenta(Long idVenta) {
        return envioRepository.findByIdVenta(idVenta).map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnvioResponseDTO> obtenerEnvioPorId(Long id) {
        return envioRepository.findById(id).map(this::mapToResponse);
    }

    private EnvioResponseDTO mapToResponse(Envio envio) {
        return new EnvioResponseDTO(
            envio.getId(),
            envio.getIdVenta(),
            envio.getIdDireccion(),
            envio.getNumeroSeguimiento(),
            envio.getEmpresaEnvio(),
            envio.getFechaDespacho(),
            envio.getIdEstado()
        );
    }
}
