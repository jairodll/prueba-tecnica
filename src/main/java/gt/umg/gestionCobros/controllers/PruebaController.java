
package gt.umg.gestionCobros.controllers;

import gt.umg.gestionCobros.exceptions.ErrorEnum;
import gt.umg.gestionCobros.exceptions.MSRinconException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Oscar
 */

@RestController
@Slf4j
    @RequestMapping("/prueba")
public class PruebaController {


    @GetMapping(value = "/saludar")
    public ResponseEntity<?> findCatalogoByTipo() {

        throw new MSRinconException(ErrorEnum.WS_ERROR_CONEXION_WS);
    }
}
