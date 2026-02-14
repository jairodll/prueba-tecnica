package gt.umg.gestionCobros.services;

import gt.umg.gestionCobros.dtos.cobrodto;
import gt.umg.gestionCobros.exceptions.ErrorEnum;
import gt.umg.gestionCobros.models.cobros;
import gt.umg.gestionCobros.models.usuarios;
import gt.umg.gestionCobros.repositories.cobroRepository;
import gt.umg.gestionCobros.repositories.usuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class cobroSvcImpl {

    @Autowired
    private cobroRepository cobroRepo;

    @Autowired
    private usuarioRepository usuarioRepo;

    @Autowired
    private auditoriaSvcImpl auditoriaService;

    /**
     * Registrar nuevo cobro
     */
    public cobros registrarCobro(cobrodto datos) {

        // Validar que el monto sea mayor a 0
        if (datos.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ErrorEnum.CO_MONTO_INVALIDO.getDescripcion());
        }

        // Validar que el usuario exista
        Optional<usuarios> usuario = usuarioRepo.findById(datos.getIdUsuario());
        if (!usuario.isPresent()) {
            throw new IllegalArgumentException(ErrorEnum.CO_USUARIO_NO_EXISTE.getDescripcion());
        }

        // Crear el cobro
        cobros cobro = new cobros();
        cobro.setIdUsuario(datos.getIdUsuario());
        cobro.setMonto(datos.getMonto());
        cobro.setMoneda(datos.getMoneda() != null ? datos.getMoneda() : "GTQ");
        cobro.setEstado("PENDIENTE");
        cobro.setFechaCreacion(new Date());
        cobro.setDescripcion(datos.getDescripcion());
        cobro.setReferenciaExterna(
                datos.getReferenciaExterna() != null
                        ? datos.getReferenciaExterna()
                        : "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
        );

        cobros cobroGuardado = cobroRepo.save(cobro);

        // Registrar en auditoría
        auditoriaService.registrar(
                "COBRO_REGISTRADO",
                String.format("Cobro registrado por %.2f %s - Usuario: %s",
                        cobroGuardado.getMonto(),
                        cobroGuardado.getMoneda(),
                        usuario.get().getNombre()),
                "COBRO",
                cobroGuardado.getIdCobro(),
                "SYSTEM"
        );

        log.info("Cobro registrado exitosamente con ID: {}", cobroGuardado.getIdCobro());
        return cobroGuardado;
    }

    /**
     * Procesar cobro individual
     * Reglas:
     * - Monto ≤ 1000 → PROCESADO
     * - Monto > 1000 → FALLIDO
     */
    public cobros procesarCobro(Integer idCobro) {
        log.info("Procesando cobro ID: {}", idCobro);

        // Buscar el cobro
        cobros cobro = cobroRepo.findById(idCobro)
                .orElseThrow(() -> new IllegalArgumentException(ErrorEnum.CO_COBRO_NO_ENCONTRADO.getDescripcion()));

        // Solo procesar si está en estado PENDIENTE
        if (!"PENDIENTE".equals(cobro.getEstado())) {
            throw new IllegalStateException("El cobro ya fue procesado. Estado actual: " + cobro.getEstado());
        }

        // Aplicar reglas de simulación
        String nuevoEstado;
        String mensaje;
        BigDecimal limite = new BigDecimal("1000");

        if (cobro.getMonto().compareTo(limite) <= 0) {
            nuevoEstado = "PROCESADO";
            mensaje = "Cobro procesado exitosamente";
        } else {
            nuevoEstado = "FALLIDO";
            mensaje = "Cobro rechazado: monto supera el límite permitido";
        }

        // Actualizar el cobro
        cobro.setEstado(nuevoEstado);
        cobro.setFechaProceso(new Date());

        cobros cobroActualizado = cobroRepo.save(cobro);

        // Registrar en auditoría
        auditoriaService.registrar(
                "COBRO_" + nuevoEstado,
                String.format("%s - Monto: %.2f %s", mensaje, cobro.getMonto(), cobro.getMoneda()),
                "COBRO",
                cobro.getIdCobro(),
                "SYSTEM"
        );

        log.info("Cobro {} procesado con estado: {}", idCobro, nuevoEstado);
        return cobroActualizado;
    }

    // ... resto de métodos anteriores (obtenerCobroPorId, listarCobros, etc.)

    public cobros obtenerCobroPorId(Integer id) {
        return cobroRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorEnum.CO_COBRO_NO_ENCONTRADO.getDescripcion()));
    }

    public List<cobros> listarCobros() {
        return cobroRepo.findAll();
    }

    public List<cobros> listarCobrosPorUsuario(Integer idUsuario) {
        return cobroRepo.findByIdUsuario(idUsuario);
    }

    public List<cobros> listarCobrosPorEstado(String estado) {
        return cobroRepo.findByEstado(estado);
    }

    public List<cobros> consultarCobrosPorUsuario(
            Integer idUsuario,
            String estado,
            Date desde,
            Date hasta
    ) {
        usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException(ErrorEnum.C_USUARIO_NO_ENCONTRADO.getDescripcion()));

        return cobroRepo.findByUsuarioWithFilters(idUsuario, estado, desde, hasta);
    }
}