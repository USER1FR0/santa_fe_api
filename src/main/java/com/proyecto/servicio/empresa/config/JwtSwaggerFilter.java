package com.proyecto.servicio.empresa.config;

import com.proyecto.servicio.empresa.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtSwaggerFilter extends OncePerRequestFilter {

    private static final String SWAGGER_PATH = "/swagger-ui";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Permitir libremente login y documentación de OpenAPI
        if (path.startsWith("/auth") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Si es Swagger, verificar header Authorization
//        if (path.startsWith(SWAGGER_PATH)) {
//            String authHeader = request.getHeader("Authorization");
//
////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
////                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////                response.getWriter().write("Acceso no autorizado: se requiere token JWT para Swagger");
////                return;
////            }
//
//            String token = authHeader.substring(7);
//
//            if (!jwtTokenUtil.isTokenValid(token)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Token JWT inválido o expirado");
//                return;
//            }
//        }

        // Si todo está bien, continúa la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
