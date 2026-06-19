package cl.syst3m64.libros.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.syst3m64.libros.model.Foto;
import cl.syst3m64.libros.model.Libro;
import cl.syst3m64.libros.repository.FotoRepository;
import cl.syst3m64.libros.repository.LibroRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FotoService {

    private final FotoRepository fotoRepository;
    private final LibroRepository libroRepository;

    public Libro obtenerLibro(Long idLibro){
        return libroRepository.findById(idLibro)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    public List<Foto> obtenerFotos(){
        return fotoRepository.findAll();
    }

    public Optional<Foto> obtenerPorId(Long id){
        return fotoRepository.findById(id);
    }

    public Foto guardarFoto(Foto foto, Long idLibro){

        Libro libro = obtenerLibro(idLibro);

        foto.setLibro(libro);

        return fotoRepository.save(foto);
    }

    public Foto actualizarFoto(Long id, Foto foto){
        return fotoRepository.findById(id).map(existing -> {
            existing.setUrl(foto.getUrl());
            existing.setNombre(foto.getNombre());
            existing.setPortada(foto.getPortada());
            return fotoRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Foto no encontrada con ID: " + id));
    }

    public void eliminarFoto(Long id){
        fotoRepository.deleteById(id);
    }
}