package gt.umg.gestionCobros.exceptions;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author amvraccot
 * @date 2/10/2024
 */

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControlExcepcion {

    /*@ExceptionHandler({JsonParseException.class, JsonMappingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleJsonParseException(Exception ex) {
        String responseBody = String.format("{\"codigo\": %d, \"estado\": %d, \"descripcion\": \"%s\"}",
                ErrorEnum.J_ERROR_PROCESAMIENTO_jSON.getCodigo(),
                ErrorEnum.J_ERROR_PROCESAMIENTO_jSON.getEstadoHttp(),
                ErrorEnum.J_ERROR_PROCESAMIENTO_jSON.getDescripcion());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }*/



    @ExceptionHandler(value = {MSRinconException.class})
    @ResponseBody
    public ResponseEntity WsAopException(MSRinconException pException) {

        ErrorEnum error = pException.getError();
        int estadoHttp = error.getEstadoHttp();
        String message = pException.getMessageOverwrite();

        if(CollectionUtils.isEmpty(pException.getErrores())) {
            return ResponseEntity
                    .status(estadoHttp)
                    .body(Response.error(error, pException.getParamError(), message));
        } else {
            return ResponseEntity
                    .status(estadoHttp)
                    .body(Response.error(error, pException.getErrores(), message));
        }
    }
}