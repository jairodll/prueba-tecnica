package gt.umg.gestionCobros.controllers;

import gt.umg.gestionCobros.dtos.usuariodto;
import gt.umg.gestionCobros.projections.usuariosProjection;
import gt.umg.gestionCobros.services.usuarioSvcImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/usuarios")
public class usuarioController {

    @Autowired
    private usuarioSvcImpl usuarioService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> crearUsuario(@RequestBody usuariodto data) {
        return ResponseEntity.ok(usuarioService.guardarUsuario(data));
    }

    @GetMapping("/listUsers")
    public ResponseEntity<List<usuariosProjection>> listUsers() {
        List<usuariosProjection> lista = usuarioService.listaUser();
        return ResponseEntity.ok(lista);
    }

}
