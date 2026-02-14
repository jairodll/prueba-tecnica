package gt.umg.gestionCobros.services;

import gt.umg.gestionCobros.dtos.infoReciboDto;
import gt.umg.gestionCobros.dtos.reciboDto;
import gt.umg.gestionCobros.projections.productoProjection;
import gt.umg.gestionCobros.repositories.productoRepository;
import gt.umg.gestionCobros.repositories.ventasRepository;
import gt.umg.gestionCobros.utils.ReportUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class productoSvcImpl {

    @Autowired
    productoRepository productoRepo;

    @Autowired
    private ventasRepository ventasRepo;

    public List<productoProjection> listProducto() {
        return productoRepo.showProducto();
    }

    @Transactional(rollbackFor = Exception.class)
    public void generarRecibo(reciboDto generarReciboDto, HttpServletResponse response) {
        List<infoReciboDto> infoReciboDtoList = new ArrayList<>();

        // Datos generales del recibo
        infoReciboDto info = new infoReciboDto();
        info.setNoRecibo(generarReciboDto.getNoRecibo());
        info.setCorreo(generarReciboDto.getDatosFacturacion().getCorreo());
        info.setCui(generarReciboDto.getDatosFacturacion().getCui());
        info.setNombreCliente(generarReciboDto.getDatosFacturacion().getNombreCliente());
        info.setNombreCatalogo(generarReciboDto.getMetodoPago().getNombreCatalogo());

        // Genera la fecha actual como Timestamp
        Timestamp fechaActual = Timestamp.valueOf(LocalDateTime.now());
        System.out.println("Fecha actual enviada a la base de datos: " + fechaActual);

        // Iterar sobre la lista de productos
        generarReciboDto.getProductos().forEach(producto -> {
            // Agregar la información del producto al recibo
            info.setDescripcion(producto.getDescripcion());
            info.setCantidad(producto.getCantidad());
            info.setPrecio(producto.getPrecio());
            info.setSubtotal(producto.getSubtotal());
            infoReciboDtoList.add(info);

        });

        if (infoReciboDtoList.isEmpty()) {
            throw new IllegalStateException("La lista infoReciboDtoList está vacía.");
        }

        // Generar el reporte con los datos actualizados
        ReportUtil.crearReporte(infoReciboDtoList, "recibo.jrxml", false, response, "pdf");
    }


}
