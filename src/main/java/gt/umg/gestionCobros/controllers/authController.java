package gt.umg.gestionCobros.controllers;

import gt.umg.gestionCobros.dtos.AuthenticationRequest;
import gt.umg.gestionCobros.dtos.AuthenticationResponse;
import gt.umg.gestionCobros.models.usuarios;
import gt.umg.gestionCobros.repositories.usuarioRepository;
import gt.umg.gestionCobros.security.JWTUtil;
import gt.umg.gestionCobros.security.UsuarioDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/auth")
public class authController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioDetailService usuarioDetailService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    usuarioRepository userRepo;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest request) {
        try {
            // Autenticar las credenciales proporcionadas
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
            );

            // Cargar los detalles del usuario
            UserDetails userDetails = usuarioDetailService.loadUserByUsername(request.getCorreo());

            // Extraer roles como una lista de cadenas
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.toList());

            // Generar el token incluyendo los roles
            String jwt = jwtUtil.generateToken(userDetails.getUsername(), roles);

            // Buscar información adicional del usuario
            Optional<usuarios> usuarioOpt = userRepo.findByCorreo(request.getCorreo());
            if (usuarioOpt.isPresent()) {
                usuarios usuario = usuarioOpt.get();

                // Construir la respuesta con todos los datos
                AuthenticationResponse response = new AuthenticationResponse(
                        jwt,
                        usuario.getCorreo(),
                        usuario.getCui(),
                        usuario.getNombre(),
                        roles.get(0)
                );

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new AuthenticationResponse("Usuario no encontrado", null, null, null, null));
            }
        } catch (BadCredentialsException e) {
            log.error("Credenciales inválidas para el usuario: {}", request.getCorreo());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthenticationResponse("Credenciales inválidas", null, null, null, null));
        } catch (Exception e) {
            log.error("Error inesperado durante la autenticación", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthenticationResponse("Error interno", null, null, null, null));
        }
    }



}


