package gt.umg.gestionCobros.services;

import gt.umg.gestionCobros.dtos.usuariodto;
import gt.umg.gestionCobros.models.usuarios;
import gt.umg.gestionCobros.projections.usuariosProjection;
import org.springframework.beans.factory.annotation.Autowired;
import gt.umg.gestionCobros.repositories.usuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import gt.umg.gestionCobros.exceptions.ErrorEnum;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class usuarioSvcImpl {

    @Autowired
    usuarioRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private rolesSvcImpl roles;

    public usuarios guardarUsuario(usuariodto datos) {
        usuarios user;
        Timestamp fechaAhora = Timestamp.valueOf(LocalDateTime.now());

        Optional<usuarios> usuarioPorCui = userRepo.findByCui(datos.getCui());
        if (usuarioPorCui.isPresent()) {
            throw new IllegalArgumentException(ErrorEnum.C_CUI_DUPLICADO.getDescripcion());
        }

        Optional<usuarios> usuarioExistente = userRepo.findByCorreo(datos.getCorreo());
        if (!usuarioExistente.isPresent()) {
            Date fechaActual = new Date();
            String hashedPassword = passwordEncoder.encode(datos.getPassword());

            user = new usuarios();
            user.setCui(datos.getCui());
            user.setNombre(datos.getNombre());
            user.setCorreo(datos.getCorreo());
            user.setEstado("Activo");
            user.setFechaCreacion(fechaActual);
            user.setPassword(hashedPassword);
        } else {
            user = usuarioExistente.get();
            user.setFechaModificacion(fechaAhora);
        }

        // Guardar usuario
        usuarios usuarioGuardado = userRepo.save(user);
        Long idUsuario = Long.valueOf(usuarioGuardado.getIdUsuario());

        // Asignar rol
        roles.assignRoleToUser(idUsuario, datos.getRol());

        return usuarioGuardado;
    }

    public List<usuariosProjection> listaUser() {
        return userRepo.showUsers();
    }
}