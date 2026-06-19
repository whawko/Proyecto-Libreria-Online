package cl.syst3m64.direccion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.syst3m64.direccion.feign.EstadoFeignClient;
import cl.syst3m64.direccion.feign.UsuarioFeignClient;
import cl.syst3m64.direccion.model.Comuna;
import cl.syst3m64.direccion.model.Direccion;
import cl.syst3m64.direccion.model.Region;
import cl.syst3m64.direccion.repository.ComunaRepository;
import cl.syst3m64.direccion.repository.DireccionRepository;
import cl.syst3m64.direccion.repository.RegionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GlobalService {

    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;
    private final DireccionRepository direccionRepository;
    private final UsuarioFeignClient usuarioFeignClient;
    private final EstadoFeignClient estadoFeignClient;

    public void validarUsuario(Long idUsuario) {
        try {
            usuarioFeignClient.obtenerUsuarioPorId(idUsuario);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El usuario con ID: " + idUsuario + " no existe");
        } catch (FeignException e) {
            throw new RuntimeException("No se puede conectar con el microservicio ms_usuario: " + e.getMessage());
        }
    }

    public void validarEstado(Long idEstado) {
        try {
            estadoFeignClient.obtenerEstadoPorId(idEstado);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("El estado con ID: " + idEstado + " no existe");
        } catch (FeignException e) {
            throw new RuntimeException("No se puede conectar con el microservicio ms_estado: " + e.getMessage());
        }
    }

    public List<Region> obtenerRegiones() {
        return regionRepository.findAll();
    }

    public Optional<Region> obtenerRegionPorId(Long id) {
        return regionRepository.findById(id);
    }

    public Region crearRegion(Region region) {
        return regionRepository.save(region);
    }

    public Region actualizarRegion(Long id, Region region) {
        return regionRepository.findById(id).map(existing -> {
            existing.setNombre(region.getNombre());
            return regionRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Region no encontrada con ID: " + id));
    }

    public void eliminarRegion(Long id) {
        regionRepository.deleteById(id);
    }

    public List<Comuna> obtenerComunas() {
        return comunaRepository.findAll();
    }

    public Optional<Comuna> obtenerComunaPorId(Long id) {
        return comunaRepository.findById(id);
    }

    public Comuna crearComuna(Comuna comuna) {
        return comunaRepository.save(comuna);
    }

    public Comuna actualizarComuna(Long id, Comuna comuna) {
        return comunaRepository.findById(id).map(existing -> {
            existing.setNombre(comuna.getNombre());
            existing.setIdRegion(comuna.getIdRegion());
            return comunaRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Comuna no encontrada con ID: " + id));
    }

    public void eliminarComuna(Long id) {
        comunaRepository.deleteById(id);
    }

    public List<Direccion> obtenerDirecciones() {
        return direccionRepository.findAll();
    }

    public Optional<Direccion> obtenerDireccionPorId(Long id) {
        return direccionRepository.findById(id);
    }

    public Direccion crearDireccion(Direccion direccion) {
        validarUsuario(direccion.getIdUsuario());
        validarEstado(direccion.getIdEstado());
        return direccionRepository.save(direccion);
    }

    public Direccion actualizarDireccion(Long id, Direccion direccion) {
        validarUsuario(direccion.getIdUsuario());
        validarEstado(direccion.getIdEstado());
        return direccionRepository.findById(id).map(existing -> {
            existing.setCalle(direccion.getCalle());
            existing.setNumero(direccion.getNumero());
            existing.setIdUsuario(direccion.getIdUsuario());
            existing.setIdComuna(direccion.getIdComuna());
            existing.setIdEstado(direccion.getIdEstado());
            return direccionRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Direccion no encontrada con ID: " + id));
    }

    public void eliminarDireccion(Long id) {
        direccionRepository.deleteById(id);
    }
}
