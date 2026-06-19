package cl.syst3m64.usuario.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.syst3m64.usuario.model.Rol;
import cl.syst3m64.usuario.model.Usuario;
import cl.syst3m64.usuario.repository.RolRepository;
import cl.syst3m64.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    @Override
    public void run(String... args){
        if(usuarioRepository.count() > 0){
            log.info("Usuarios ya cargados, se omite este archivo");
        }
        log.info("Cargando usuarios...");

        Rol rol1 = rolRepository.save(new Rol(null, "ADMIN"));
        Rol rol2 = rolRepository.save(new Rol(null, "USER"));

        usuarioRepository.save(new Usuario(null, "12345678-9", "Juan", "Pérez", "1990-01-01", "juan.perez@example.com", "password123", rol1, 1L));

        usuarioRepository.save(new Usuario(null, "98765432-1", "María", "González", "1992-02-02", "maria.gonzalez@example.com", "password456", rol2, 2L));

        usuarioRepository.save(new Usuario(null, "11111111-1", "Carlos", "Sánchez", "1985-03-03", "carlos.sanchez@example.com", "password789", rol2, 2L));

        log.info("Usuarios cargados");
    }
}

