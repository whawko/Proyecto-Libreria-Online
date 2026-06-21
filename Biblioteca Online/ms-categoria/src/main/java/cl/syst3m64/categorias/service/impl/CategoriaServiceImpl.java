package cl.syst3m64.categorias.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import cl.syst3m64.categorias.dto.CategoriaRequestDTO;
import cl.syst3m64.categorias.dto.CategoriaResponseDTO;
import cl.syst3m64.categorias.model.Categoria;
import cl.syst3m64.categorias.repository.CategoriaRepository;
import cl.syst3m64.categorias.service.CategoriaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    private CategoriaResponseDTO mapToResponse(Categoria cat) {
        return new CategoriaResponseDTO(cat.getId(), cat.getNombre(), cat.getDescripcion());
    }

    @Override
    public List<CategoriaResponseDTO> obtenerTodas() {
        return categoriaRepository.findAll().stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CategoriaResponseDTO> obtenerNombres(String nombre) {
        return categoriaRepository.findAllNombres(nombre).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public CategoriaResponseDTO guardar(CategoriaRequestDTO catDto) {
        Categoria cat = new Categoria(null, catDto.getNombre(), catDto.getDescripcion());
        Categoria saved = categoriaRepository.save(cat);
        return mapToResponse(saved);
    }

    @Override
    public Optional<CategoriaResponseDTO> obtenerPorId(Long id) {
        return categoriaRepository.findById(id).map(this::mapToResponse);
    }

    @Override
    public Optional<CategoriaResponseDTO> actualizar(Long id, CategoriaRequestDTO catDto) {
        return categoriaRepository.findById(id).map(existing -> {
            existing.setNombre(catDto.getNombre());
            existing.setDescripcion(catDto.getDescripcion());
            Categoria saved = categoriaRepository.save(existing);
            return mapToResponse(saved);
        });
    }

    @Override
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}

