package gt.umg.gestionCobros.controllers;

import gt.umg.gestionCobros.projections.catalogoProjection;
import gt.umg.gestionCobros.projections.compraProjection;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import gt.umg.gestionCobros.services.catalogoSvcImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/catalogos")
public class catalogoController {

    @Autowired
    private catalogoSvcImpl catalogoSvcImpl;

    @GetMapping(path = "/catalogoSearch/{tipo}")
    public ResponseEntity<List<catalogoProjection>> catalogo(
            @PathVariable @Parameter(description = "tipo") String tipo) {
        return ResponseEntity.ok(catalogoSvcImpl.catalogoProducto(tipo));
    }

    @GetMapping(path = "/catalogoCompras/{idUsuario}")
    public ResponseEntity<List<compraProjection>> compra(
            @PathVariable @Parameter(description = "idUsuario") Integer idUsuario) {
        return ResponseEntity.ok(catalogoSvcImpl.compra(idUsuario));
    }

}
