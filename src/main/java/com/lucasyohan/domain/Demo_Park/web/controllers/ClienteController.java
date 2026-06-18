package com.lucasyohan.domain.Demo_Park.web.controllers;

import com.lucasyohan.domain.Demo_Park.entities.Cliente;
import com.lucasyohan.domain.Demo_Park.jwt.JwtUserDetails;
import com.lucasyohan.domain.Demo_Park.services.ClienteService;
import com.lucasyohan.domain.Demo_Park.services.UsuarioService;
import com.lucasyohan.domain.Demo_Park.web.dto.ClienteCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.ClienteResponseDto;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioResponseDto;
import com.lucasyohan.domain.Demo_Park.web.exception.ErrorMessage;
import com.lucasyohan.domain.Demo_Park.web.mapper.ClienteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/clientes")
public class ClienteController {
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo cliente", description = "Cria um novo cliente com as informações fornecidas no corpo da requisição",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "CPF já cadastrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "CPF já cadastrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @PostMapping
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto clienteDto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(clienteDto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna as informações de um cliente com base no ID fornecido",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PreAuthorize("(hasRole('ROLE_CLIENTE') AND #id == authentication.principal.id) OR hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

}
