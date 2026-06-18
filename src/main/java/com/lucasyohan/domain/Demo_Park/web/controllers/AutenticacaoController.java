package com.lucasyohan.domain.Demo_Park.web.controllers;

import com.lucasyohan.domain.Demo_Park.jwt.JwtToken;
import com.lucasyohan.domain.Demo_Park.jwt.JwtUserDetailsService;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioLoginDto;
import com.lucasyohan.domain.Demo_Park.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
@RequestMapping("/api/v2")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Autenticar usuário", description = "Autentica um usuário e retorna um token JWT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
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
