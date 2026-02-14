package gt.umg.gestionCobros.services;

import gt.umg.gestionCobros.projections.catalogoProjection;
import gt.umg.gestionCobros.projections.compraProjection;
import gt.umg.gestionCobros.repositories.catalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class catalogoSvcImpl {
    @Autowired
    catalogoRepository catalogoRepo;

    public List<catalogoProjection> catalogoProducto(String tipoCatalogo) {
        return catalogoRepo.findCatalogoByTipo(tipoCatalogo);
    }

    public List<compraProjection> compra(Integer idUsuario) {
        return catalogoRepo.findCompraById(idUsuario);
    }
}
