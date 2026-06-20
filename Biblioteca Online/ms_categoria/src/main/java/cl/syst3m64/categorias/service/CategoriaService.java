package cl.syst3m64.categorias.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.syst3m64.categorias.model.Categoria;
import cl.syst3m64.categorias.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerTodas(){
        return categoriaRepository.findAll();
    }
    public List<Categoria> obtenerNombres(String nombre){
        return categoriaRepository.findAllNombres(nombre);
    }
    public Categoria guardar(Categoria cat){
        return categoriaRepository.save(cat);
    }
    public Optional<Categoria> obtenerPorId(Long id){
        return categoriaRepository.findById(id);
    }

    public Optional<Categoria> actualizar(Long id, Categoria cat){
        return categoriaRepository.findById(id).map(existing -> {
            existing.setNombre(cat.getNombre());
            existing.setDescripcion(cat.getDescripcion());
            return categoriaRepository.save(existing);
        });
    }

    public void eliminar(Long id){
        categoriaRepository.deleteById(id);
    }
}
