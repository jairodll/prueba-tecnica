package gt.umg.gestionCobros.services;

import gt.umg.gestionCobros.dtos.cobrodto;
import gt.umg.gestionCobros.dtos.loteCobrodto;
import gt.umg.gestionCobros.dtos.loteCobroResponsedto;
import gt.umg.gestionCobros.models.cobros;
import gt.umg.gestionCobros.models.usuarios;
import gt.umg.gestionCobros.repositories.cobroRepository;
import gt.umg.gestionCobros.repositories.usuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CobroSvcImplTest {

    @Mock
    private cobroRepository cobroRepo;

    @Mock
    private usuarioRepository usuarioRepo;

    @Mock
    private auditoriaSvcImpl auditoriaService;

    @InjectMocks
    private cobroSvcImpl cobroService;

    private cobrodto requestDTO;
    private usuarios usuario;
    private cobros cobro;

    @BeforeEach
    void setUp() {
        requestDTO = new cobrodto();
        requestDTO.setIdUsuario(1);
        requestDTO.setMonto(new BigDecimal("500.00"));
        requestDTO.setMoneda("GTQ");

        usuario = new usuarios();
        usuario.setIdUsuario(1);
        usuario.setNombre("Juan Pérez");

        cobro = new cobros();
        cobro.setIdCobro(1);
        cobro.setIdUsuario(1);
        cobro.setMonto(new BigDecimal("500.00"));
        cobro.setMoneda("GTQ");
        cobro.setEstado("PENDIENTE");
    }

    @Test
    void registrarCobro_Exitoso() {
        // Arrange
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(cobroRepo.save(any(cobros.class))).thenReturn(cobro);
        doNothing().when(auditoriaService).registrar(anyString(), anyString(), anyString(), any(), anyString());

        // Act
        cobros resultado = cobroService.registrarCobro(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals(new BigDecimal("500.00"), resultado.getMonto());
        verify(usuarioRepo, times(1)).findById(1);
        verify(cobroRepo, times(1)).save(any(cobros.class));
    }

    @Test
    void registrarCobro_MontoInvalido_LanzaExcepcion() {
        // Arrange
        requestDTO.setMonto(new BigDecimal("-100"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cobroService.registrarCobro(requestDTO)
        );

        assertTrue(exception.getMessage().contains("monto debe ser mayor a 0"));
        verify(cobroRepo, never()).save(any(cobros.class));
    }

    @Test
    void registrarCobro_UsuarioNoExiste_LanzaExcepcion() {
        // Arrange
        when(usuarioRepo.findById(999)).thenReturn(Optional.empty());
        requestDTO.setIdUsuario(999);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cobroService.registrarCobro(requestDTO)
        );

        assertTrue(exception.getMessage().contains("usuario"));
        verify(cobroRepo, never()).save(any(cobros.class));
    }

    @Test
    void procesarCobro_MontoBajo_EstadoProcesado() {
        // Arrange
        cobro.setMonto(new BigDecimal("500.00"));
        when(cobroRepo.findById(1)).thenReturn(Optional.of(cobro));
        when(cobroRepo.save(any(cobros.class))).thenAnswer(invocation -> {
            cobros c = invocation.getArgument(0);
            c.setEstado("PROCESADO");
            return c;
        });
        doNothing().when(auditoriaService).registrar(anyString(), anyString(), anyString(), any(), anyString());

        // Act
        cobros resultado = cobroService.procesarCobro(1);

        // Assert
        assertNotNull(resultado);
        assertEquals("PROCESADO", resultado.getEstado());
        verify(cobroRepo, times(1)).save(any(cobros.class));
    }

    @Test
    void procesarCobro_MontoAlto_EstadoFallido() {
        // Arrange
        cobro.setMonto(new BigDecimal("1500.00"));
        when(cobroRepo.findById(1)).thenReturn(Optional.of(cobro));
        when(cobroRepo.save(any(cobros.class))).thenAnswer(invocation -> {
            cobros c = invocation.getArgument(0);
            c.setEstado("FALLIDO");
            return c;
        });
        doNothing().when(auditoriaService).registrar(anyString(), anyString(), anyString(), any(), anyString());

        // Act
        cobros resultado = cobroService.procesarCobro(1);

        // Assert
        assertNotNull(resultado);
        assertEquals("FALLIDO", resultado.getEstado());
    }

    @Test
    void procesarCobro_YaProcesado_LanzaExcepcion() {
        // Arrange
        cobro.setEstado("PROCESADO");
        when(cobroRepo.findById(1)).thenReturn(Optional.of(cobro));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> cobroService.procesarCobro(1)
        );

        assertTrue(exception.getMessage().contains("ya fue procesado"));
        verify(cobroRepo, never()).save(any(cobros.class));
    }

    @Test
    void procesarLote_Idempotente() {
        // Arrange
        cobro.setEstado("PROCESADO");
        when(cobroRepo.findById(1)).thenReturn(Optional.of(cobro));
        doNothing().when(auditoriaService).registrar(anyString(), anyString(), anyString(), any(), anyString());

        loteCobrodto loteRequest = new loteCobrodto();
        loteRequest.setIdsCobros(Arrays.asList(1));

        // Act
        loteCobroResponsedto resultado = cobroService.procesarLote(loteRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotal());
        assertEquals(1, resultado.getProcesados());
        verify(cobroRepo, never()).save(any(cobros.class)); // No se vuelve a guardar
    }
}