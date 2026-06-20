package cl.syst3m64.envio.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.syst3m64.envio.dto.DireccionResponseDTO;
import cl.syst3m64.envio.dto.EnvioRequestDTO;
import cl.syst3m64.envio.dto.EnvioResponseDTO;
import cl.syst3m64.envio.dto.VentaResponseDTO;
import cl.syst3m64.envio.feign.DireccionFeignClient;
import cl.syst3m64.envio.feign.VentaFeignClient;
import cl.syst3m64.envio.model.Envio;
import cl.syst3m64.envio.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final VentaFeignClient ventaFeignClient;
    private final DireccionFeignClient direccionFeignClient;

    @Transactional
    public EnvioResponseDTO registrarEnvio(EnvioRequestDTO request) {
        // 1. Validar que la venta exista en ms_venta
        VentaResponseDTO venta;
        try {
            venta = ventaFeignClient.obtenerVentaPorId(request.getIdVenta());
            if (venta == null) {
                throw new RuntimeException("La venta con ID " + request.getIdVenta() + " no fue encontrada.");
            }
        } catch (Exception e) {
            throw new RuntimeException("La venta con ID " + request.getIdVenta() + " no existe o no pudo ser validada. Error: " + e.getMessage());
        }

        // 2. Validar que la dirección exista en ms_direccion
        DireccionResponseDTO direccion;
        try {
            direccion = direccionFeignClient.obtenerDireccionPorId(request.getIdDireccion());
            if (direccion == null) {
                throw new RuntimeException("La dirección con ID " + request.getIdDireccion() + " no fue encontrada.");
            }
        } catch (Exception e) {
            throw new RuntimeException("La dirección con ID " + request.getIdDireccion() + " no existe o no pudo ser validada. Error: " + e.getMessage());
        }

        // 3. Verificar si ya existe un despacho registrado para esta venta
        Optional<Envio> envioExistente = envioRepository.findByIdVenta(request.getIdVenta());
        if (envioExistente.isPresent()) {
            throw new RuntimeException("Ya existe un despacho registrado para la venta con ID: " + request.getIdVenta());
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

    @Transactional(readOnly = true)
    public Optional<EnvioResponseDTO> obtenerEnvioPorVenta(Long idVenta) {
        return envioRepository.findByIdVenta(idVenta).map(this::mapToResponse);
    }

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
