package gt.umg.gestionCobros.security;

import gt.umg.gestionCobros.models.usuarios;
import gt.umg.gestionCobros.repositories.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioDetailService implements UserDetailsService {

    @Autowired
    private usuarioRepository repositorio;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Recupera el usuario por correo
        usuarios usuario = repositorio.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        // Recupera los roles asociados al usuario
        List<String> roles = Collections.singletonList(repositorio.findRolById(usuario.getIdUsuario()));
        // Convierte los roles a GrantedAuthority
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Retorna un objeto UserDetails con los roles incluidos
        return new User(usuario.getCorreo(), usuario.getPassword(), authorities);
    }

}



