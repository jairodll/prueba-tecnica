package gt.umg.gestionCobros.services;

import gt.umg.gestionCobros.projections.rolesProjection;
import gt.umg.gestionCobros.repositories.rolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class rolesSvcImpl {

    @Autowired
    rolesRepository roles;

    public List<rolesProjection> listaRol() {
        return roles.showRoles();
    }

    @Transactional
    public void assignRoleToUser(Long idUsuario, Long idRol) {
        roles.assignRoleToUser(idUsuario, idRol);
    }
}
