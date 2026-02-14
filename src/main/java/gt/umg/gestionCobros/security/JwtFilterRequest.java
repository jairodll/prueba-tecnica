package gt.umg.gestionCobros.security;

import gt.umg.gestionCobros.exceptions.ErrorEnum;
import gt.umg.gestionCobros.exceptions.MSRinconException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilterRequest extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UsuarioDetailService usuarioDetailService;

    public JwtFilterRequest(JWTUtil jwtUtil, UsuarioDetailService usuarioDetailService) {
        this.jwtUtil = jwtUtil;
        this.usuarioDetailService = usuarioDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Extraer el token después de "Bearer "

            try {
                // Validar el token y obtener el username
                String username = jwtUtil.extractUsername(token);

                if (username != null && jwtUtil.isTokenValid(token, usuarioDetailService.loadUserByUsername(username))) {
                    // Configurar la autenticación en el contexto de seguridad
                    UserDetails userDetails = usuarioDetailService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Manejo de errores de token
                throw new MSRinconException(ErrorEnum.TOKEN_INVALIDO);
            }
        }

        // Continuar con el siguiente filtro
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Excluir rutas públicas como /auth/**
        String path = request.getRequestURI();
        return path.startsWith("/auth/") || path.startsWith("/v3/api-docs/") || path.startsWith("/swagger-ui/");
    }
}
