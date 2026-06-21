package cl.syst3m64.libros.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import cl.syst3m64.libros.dto.FotoRequestDTO;
import cl.syst3m64.libros.dto.FotoResponseDTO;
import cl.syst3m64.libros.model.Foto;
import cl.syst3m64.libros.model.Libro;
import cl.syst3m64.libros.repository.FotoRepository;
import cl.syst3m64.libros.repository.LibroRepository;
import cl.syst3m64.libros.service.FotoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FotoServiceImpl implements FotoService {

    private final FotoRepository fotoRepository;
    private final LibroRepository libroRepository;

    @Override
    public Libro obtenerLibro(Long idLibro){
        return libroRepository.findById(idLibro)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    @Override
    public List<FotoResponseDTO> obtenerFotos(){
        return fotoRepository.findAll().stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<FotoResponseDTO> obtenerPorId(Long id){
        return fotoRepository.findById(id).map(this::mapToResponseDTO);
    }

    @Override
    public FotoResponseDTO guardarFoto(FotoRequestDTO fotoDto, Long idLibro){
        Libro libro = obtenerLibro(idLibro);
        Foto foto = new Foto();
        foto.setUrl(fotoDto.getUrl());
        foto.setNombre(fotoDto.getNombre());
        foto.setPortada(fotoDto.getPortada());
        foto.setLibro(libro);
        return mapToResponseDTO(fotoRepository.save(foto));
    }

    @Override
    public FotoResponseDTO actualizarFoto(Long id, FotoRequestDTO fotoDto){
        return fotoRepository.findById(id).map(existing -> {
            existing.setUrl(fotoDto.getUrl());
            existing.setNombre(fotoDto.getNombre());
            existing.setPortada(fotoDto.getPortada());
            return mapToResponseDTO(fotoRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Foto no encontrada con ID: " + id));
    }

    @Override
    public void eliminarFoto(Long id){
        fotoRepository.deleteById(id);
    }

    private FotoResponseDTO mapToResponseDTO(Foto foto) {
        if (foto == null) return null;
        Long idLibro = (foto.getLibro() != null) ? foto.getLibro().getId() : null;
        return new FotoResponseDTO(
            foto.getId(),
            foto.getUrl(),
            foto.getNombre(),
            foto.getPortada(),
            idLibro
        );
    }
}
