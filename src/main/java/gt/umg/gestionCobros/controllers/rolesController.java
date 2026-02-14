package gt.umg.gestionCobros.controllers;


import gt.umg.gestionCobros.dtos.AssignRoleDto;
import gt.umg.gestionCobros.projections.rolesProjection;
import gt.umg.gestionCobros.services.rolesSvcImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/roles")
public class rolesController {
    @Autowired
    private rolesSvcImpl roles;

    @GetMapping("/listRoles")
    public ResponseEntity<List<rolesProjection>> listUsers() {
        List<rolesProjection> lista = roles.listaRol();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/assignRole")
    public ResponseEntity<String> assignRoleToUser(@RequestBody AssignRoleDto dto) {
        try {
            roles.assignRoleToUser(dto.getIdUsuario(), dto.getIdRol());
            return ResponseEntity.ok("Rol asignado correctamente.");
        } catch (Exception e) {
            log.error("Error al asignar rol: ", e);
            return ResponseEntity.status(500).body("Error al asignar rol.");
        }
    }
}
