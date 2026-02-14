package gt.umg.gestionCobros.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommonMethod {

    /**
     * Realiza la conversion de la Projection List a el objeto DTO List
     *
     * @param <T>
     * @param <U>
     * @param listInfo
     * @param dtoClass
     * @return
     */
    public <T, U> List<U> convertListProjectionToListDto(List<T> listInfo, Class<U> dtoClass) {
        List<U> listDto = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        listInfo.forEach((infoProjection) -> {
            try {
                listDto.add(objectMapper.readValue(objectMapper.writeValueAsString(infoProjection), dtoClass));
            } catch (JsonProcessingException ex) {
                log.error("Error Parseo Projection Dto: {}", ex);
            }
        });
        return listDto;
    }
}
