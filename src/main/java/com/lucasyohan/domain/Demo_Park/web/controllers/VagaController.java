package com.lucasyohan.domain.Demo_Park.web.controllers;

import com.lucasyohan.domain.Demo_Park.entities.Vaga;
import com.lucasyohan.domain.Demo_Park.services.VagaService;
import com.lucasyohan.domain.Demo_Park.web.dto.VagaCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.VagaResponseDto;
import com.lucasyohan.domain.Demo_Park.web.exception.ErrorMessage;
import com.lucasyohan.domain.Demo_Park.web.mapper.VagaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Vagas", description = "Endpoints para gerenciamento de vagas")
@RestController
@RequestMapping("/api/v2/vagas")
@RequiredArgsConstructor
public class VagaController {

    private final VagaService vagaService;

    @Operation(summary = "Criar um nova vaga", description = "Cria uma nova vaga com as informações fornecidas no corpo da requisição",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Vaga criada com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URI da vaga criada", schema = @Schema(type = "URL do recurso criado"))),
                    @ApiResponse(responseCode = "409", description = "Código da vaga já cadastrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDto dto) {
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Consultar vaga por código", description = "Consulta uma vaga pelo seu código",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vaga encontrada com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URI da vaga criada", schema = @Schema(type = "URL do recurso criado"))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Vaga não encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto> getByCodigo(@PathVariable String codigo) {
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.status(HttpStatus.OK).body(VagaMapper.toDto(vaga));
    }

}
