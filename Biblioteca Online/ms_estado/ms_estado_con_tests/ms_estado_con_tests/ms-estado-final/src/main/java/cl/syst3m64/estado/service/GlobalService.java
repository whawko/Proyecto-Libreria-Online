package cl.syst3m64.estado.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.syst3m64.estado.model.Estado;
import cl.syst3m64.estado.model.TipoEstado;
import cl.syst3m64.estado.repository.EstadoRepository;
import cl.syst3m64.estado.repository.TipoEstadoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GlobalService {
    private final EstadoRepository estadoRepository;
    private final TipoEstadoRepository tipoEstadoRepository;

    public List<Estado> obtenerTodosEstados(){
        return estadoRepository.findAll();
    }

    public Estado guardarEstado(Estado estado){
        return estadoRepository.save(estado);
    }

    public Optional<Estado> obtenerEstadoPorId(Long id){
        return estadoRepository.findById(id);
    }
    
    public void eliminarEstado(Long id){
        estadoRepository.deleteById(id);
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public List<TipoEstado> obtenerTipoEstados(){
        return tipoEstadoRepository.findAll();
    }

    public Optional<TipoEstado> obtenerTipoEstadosPorId(Long idTipo){
        return tipoEstadoRepository.findById(idTipo);
    }

    public TipoEstado guardarTipoEstado(TipoEstado tipoEstado){
        return tipoEstadoRepository.save(tipoEstado);
    }

    public void eliminarTipoEstado(Long idTipo){
        tipoEstadoRepository.deleteById(idTipo);
    }

    public Estado actualizarEstado(Long id, Estado estado){
        return estadoRepository.findById(id).map(existing -> {
            existing.setNombre(estado.getNombre());
            existing.setDescripcion(estado.getDescripcion());
            existing.setTipoEstado(estado.getTipoEstado());
            return estadoRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + id));
    }

    public TipoEstado actualizarTipoEstado(Long id, TipoEstado tipoEstado){
        return tipoEstadoRepository.findById(id).map(existing -> {
            existing.setNombre(tipoEstado.getNombre());
            return tipoEstadoRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("TipoEstado no encontrado con ID: " + id));
    }
}
