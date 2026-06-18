package com.lucasyohan.domain.Demo_Park.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token  = request.getHeader(JwtUtils.JWT_AUTHORIZATION);
        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
            log.warn("Token nulo, vazio ou não iniciado com 'Bearer '.");
            filterChain.doFilter(request, response);
            return;
        }

        if (!JwtUtils.isTokenValid(token)) {
            log.warn("Jwt inválido ou expirado");
            filterChain.doFilter(request, response);
            return;
        }
        try{
            String username = JwtUtils.getUsernameFromToken(token);
            toAuthentication(request, username);
            filterChain.doFilter(request, response);
        }catch (JwtException ex) {
            log.warn("Token inválido: {}", ex.getMessage());
            filterChain.doFilter(request, response);
            return;
        }
    }

    private void toAuthentication(HttpServletRequest request, String username) {
        UserDetails userDetails = detailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        log.info("Usuário autenticado: {}", username);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
