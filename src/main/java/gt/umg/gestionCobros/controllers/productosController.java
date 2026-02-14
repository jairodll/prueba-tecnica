package gt.umg.gestionCobros.controllers;

import gt.umg.gestionCobros.dtos.reciboDto;
import gt.umg.gestionCobros.projections.productoProjection;
import gt.umg.gestionCobros.services.productoSvcImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/productos")
public class productosController {

    @Autowired
    private productoSvcImpl productoSvc;

    @GetMapping("/listProductos")
    public ResponseEntity<List<productoProjection>> listUsers() {
        List<productoProjection> lista = productoSvc.listProducto();
        return ResponseEntity.ok(lista);
    }

    @PostMapping(path = "/generar-recibo")
    public void generarRecibo(@RequestBody reciboDto generarReciboDto, HttpServletResponse response) {
        productoSvc.generarRecibo(generarReciboDto, response);
    }

}
