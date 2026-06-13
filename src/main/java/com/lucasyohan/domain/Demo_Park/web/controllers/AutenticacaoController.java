package com.lucasyohan.domain.Demo_Park.web.controllers;

import com.lucasyohan.domain.Demo_Park.jwt.JwtToken;
import com.lucasyohan.domain.Demo_Park.jwt.JwtUserDetailsService;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioLoginDto;
import com.lucasyohan.domain.Demo_Park.web.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto loginDto, HttpServletRequest request) {
        log.info("Requisição de autenticação recebida para o username: {}", loginDto.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
            JwtToken token = detailsService.getTokenAuthenticated(authentication.getName());
            log.info("Autenticação bem-sucedida para o username: {}", loginDto.getUsername());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException ex) {
            log.warn("Falha na autenticação para o username: {}. Erro: {}", loginDto.getUsername(), ex.getMessage());
        }
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));
    }
}
