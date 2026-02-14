package gt.umg.gestionCobros.services;

import gt.umg.gestionCobros.models.auditoria;
import gt.umg.gestionCobros.repositories.auditoriaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class auditoriaSvcImpl {

    @Autowired
    private auditoriaRepository auditoriaRepo;

    /**
     * Registrar evento en auditoría
     */
    public void registrar(String evento, String resumenPayload, String entidad, Integer idEntidad, String usuario) {
        try {
            auditoria audit = new auditoria();
            audit.setEvento(evento);
            audit.setResumenPayload(resumenPayload);
            audit.setEntidad(entidad);
            audit.setIdEntidad(idEntidad);
            audit.setUsuarioSistema(usuario);
            audit.setFecha(new Date());

            auditoriaRepo.save(audit);
            log.info("Evento de auditoría registrado: {}", evento);
        } catch (Exception e) {
            log.error("Error al registrar auditoría: {}", e.getMessage());
            // No lanzamos excepción para no interrumpir el flujo principal
        }
    }
}