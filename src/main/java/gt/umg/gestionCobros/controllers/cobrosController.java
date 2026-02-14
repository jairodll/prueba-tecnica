package gt.umg.gestionCobros.controllers;
import gt.umg.gestionCobros.dtos.cobrodto;
import gt.umg.gestionCobros.dtos.loteCobrodto;
import gt.umg.gestionCobros.models.cobros;
import gt.umg.gestionCobros.services.cobroSvcImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/cobros")
public class cobrosController {
    @Autowired
    private cobroSvcImpl cobroService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> registrarCobro(@RequestBody cobrodto data) {
        return ResponseEntity.ok(cobroService.registrarCobro(data));
    }

    @PostMapping("/{id}/procesar")
    public ResponseEntity<?> procesarCobro(@PathVariable Integer id) {
        return ResponseEntity.ok(cobroService.procesarCobro(id));
    }

    @GetMapping("/listCobros")
    public ResponseEntity<List<cobros>> listarCobros() {
        List<cobros> lista = cobroService.listarCobros();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCobro(@PathVariable Integer id) {
        return ResponseEntity.ok(cobroService.obtenerCobroPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<cobros>> listarCobrosPorUsuario(@PathVariable Integer idUsuario) {
        List<cobros> lista = cobroService.listarCobrosPorUsuario(idUsuario);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<cobros>> listarCobrosPorEstado(@PathVariable String estado) {
        List<cobros> lista = cobroService.listarCobrosPorEstado(estado);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{idUsuario}/filtros")
    public ResponseEntity<List<cobros>> consultarCobrosPorUsuario(
            @PathVariable Integer idUsuario,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta
    ) {
        List<cobros> lista = cobroService.consultarCobrosPorUsuario(idUsuario, estado, desde, hasta);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/lotes/procesar")
    public ResponseEntity<?> procesarLote(@RequestBody loteCobrodto data) {
        return ResponseEntity.ok(cobroService.procesarLote(data));
    }
}
