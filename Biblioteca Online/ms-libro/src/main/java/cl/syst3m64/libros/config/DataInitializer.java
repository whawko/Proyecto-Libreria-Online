package cl.syst3m64.libros.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.syst3m64.libros.model.Libro;
import cl.syst3m64.libros.repository.LibroRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final LibroRepository libroRepository;
    @Override
    public void run(String... args){
        if(libroRepository.count() > 0){
            log.info("Libro ya cargados, se omite este archivo");
        }
        log.info("Cargando libros...");
        libroRepository.save(new Libro(null, "El principito", "Un libro de niños", "Antoine de Saint-Exupéry", "978-01560", 10.99f, "1943", "Harcourt", 1L, 1L));
        libroRepository.save(new Libro(null, "Don Quijote de la Mancha", "Una obra maestra de la literatura española", "Miguel de Cervantes", "978-8424", 15.99f, "1605", "Editorial Planeta", 2L, 1L));
        libroRepository.save(new Libro(null, "La Odisea", "La épica de Homero ", "Homero", "978-01402", 12.99f, "800 a.C.", "Penguin Classics", 3L, 1L));
        log.info("Libros cargados");
    }
}
