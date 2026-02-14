package gt.umg.gestionCobros.services;

import gt.umg.gestionCobros.dtos.usuariodto;
import gt.umg.gestionCobros.models.usuarios;
import gt.umg.gestionCobros.repositories.usuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioSvcImplTest {

    @Mock
    private usuarioRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private rolesSvcImpl roles;

    @InjectMocks
    private usuarioSvcImpl usuarioService;

    private usuariodto requestDTO;
    private usuarios usuario;

    @BeforeEach
    void setUp() {
        requestDTO = new usuariodto();
        requestDTO.setNombre("Juan Pérez");
        requestDTO.setCui("1234567890101");
        requestDTO.setCorreo("juan@email.com");
        requestDTO.setPassword("123");
        requestDTO.setRol(1L);

        usuario = new usuarios();
        usuario.setIdUsuario(1);
        usuario.setNombre("Juan Pérez");
        usuario.setCui("1234567890101");
        usuario.setCorreo("juan@email.com");
        usuario.setEstado("Activo");
    }

    @Test
    void guardarUsuario_Exitoso() {
        // Arrange
        when(userRepo.findByCui(anyString())).thenReturn(Optional.empty());
        when(userRepo.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(userRepo.save(any(usuarios.class))).thenReturn(usuario);
        doNothing().when(roles).assignRoleToUser(any(Long.class), (long) anyInt());

        // Act
        usuarios resultado = usuarioService.guardarUsuario(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(userRepo, times(1)).findByCui("1234567890101");
        verify(userRepo, times(1)).save(any(usuarios.class));
    }

    @Test
    void guardarUsuario_CuiDuplicado_LanzaExcepcion() {
        // Arrange
        when(userRepo.findByCui(anyString())).thenReturn(Optional.of(usuario));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.guardarUsuario(requestDTO)
        );

        assertTrue(exception.getMessage().contains("Ya existe un usuario con el CUI/DPI"));
        verify(userRepo, never()).save(any(usuarios.class));
    }
}