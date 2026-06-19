package cl.syst3m64.usuario.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import cl.syst3m64.usuario.model.Rol;
import cl.syst3m64.usuario.model.Usuario;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test del repositorio de usuarios en memoria")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private TestEntityManager entityManager;

    // Variables para datos insertados en memoria antes de cada test
    private Usuario usuarioJuan;
    private Usuario usuarioMaria;

    // insertar datos antes de cada test
    @BeforeEach
    void setUp() {
        Rol rolAdmin = entityManager.persistAndFlush(
            new Rol(null, "ADMIN")
        );

        usuarioJuan = entityManager.persistAndFlush(
            new Usuario(null, "12345678-9", "Juan", "Perez", "01-01-1990", "juan@mail.com", "clave123", rolAdmin, 1L)
        );

        usuarioMaria = entityManager.persistAndFlush(
            new Usuario(null, "98765432-1", "Maria", "Lopez", "15-06-1995", "maria@mail.com", "clave456", rolAdmin, 1L)
        );
    }

    // TEST para findAll() -- Heredado del JpaRepository
    @Test
    @DisplayName("findAll() debe retornar todos los usuarios insertados")
    void findAll_debeRetornarTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
    }

    // TEST para findById()
    @Test
    @DisplayName("findById() debe retornar Optional con el usuario cuando existe")
    void findById_debeRetornarUsuario_cuandoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findById(usuarioJuan.getId());

        assertTrue(resultado.isPresent());
        assertEquals("12345678-9", resultado.get().getRut());
    }

    @Test
    @DisplayName("findById() debe retornar Optional vacio cuando el ID no existe")
    void findById_debeRetornarVacio_cuandoNoExiste() {
        Optional<Usuario> resultado = usuarioRepository.findById(99999L);

        assertFalse(resultado.isPresent());
    }

    // TEST para save()
    @Test
    @DisplayName("save() debe persistir un nuevo usuario y asignarle un ID")
    void save_debeGuardarUsuario_yAsignarId() {
        Rol rolCliente = entityManager.persistAndFlush(
            new Rol(null, "CLIENTE")
        );

        Usuario nuevoUsuario = new Usuario(null, "11111111-1", "Carlos", "Gonzalez", "20-03-2000", "carlos@mail.com", "clave789", rolCliente, 2L);
        Usuario guardado = usuarioRepository.save(nuevoUsuario);

        assertNotNull(guardado.getId());
        assertEquals("11111111-1", guardado.getRut());
    }

    // TEST para deleteById()
    @Test
    @DisplayName("deleteById() debe eliminar el usuario correctamente")
    void deleteById_debeEliminarUsuario_cuandoExiste() {
        usuarioRepository.deleteById(usuarioJuan.getId());

        Optional<Usuario> resultado = usuarioRepository.findById(usuarioJuan.getId());

        assertFalse(resultado.isPresent());
    }
}
