package com.lucasyohan.domain.Demo_Park.web.controllers;

import com.lucasyohan.domain.Demo_Park.entities.ClienteVaga;
import com.lucasyohan.domain.Demo_Park.jwt.JwtUserDetails;
import com.lucasyohan.domain.Demo_Park.repositories.projection.ClienteVagaProjection;
import com.lucasyohan.domain.Demo_Park.services.ClienteVagaService;
import com.lucasyohan.domain.Demo_Park.services.EstacionamentoService;
import com.lucasyohan.domain.Demo_Park.web.dto.EstacionamentoCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.EstacionamentoResponseDto;
import com.lucasyohan.domain.Demo_Park.web.dto.PageableDto;
import com.lucasyohan.domain.Demo_Park.web.exception.ErrorMessage;
import com.lucasyohan.domain.Demo_Park.web.mapper.ClienteVagaMapper;
import com.lucasyohan.domain.Demo_Park.web.mapper.PageableMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Estacionamentos", description = "Endpoints para gerenciamento de estacionamentos")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/estacionamentos")
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;

    @Operation(summary = "Consultar vaga por código", description = "Consulta uma vaga pelo seu código",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Check-in realizado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URI do recurso criado"),
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso restrito a ADMINS",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente com CPF ou nenhuma vaga livre encontrados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "409", description = "Vaga ocupada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados do veículo ou CPF inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkIn(@RequestBody @Valid EstacionamentoCreateDto request) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toCliente(request);
        estacionamentoService.checkIn(clienteVaga);

        EstacionamentoResponseDto response = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Consulta por código", description = "Consulta uma vaga pelo seu código",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "URI do recibo encontrado",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URI do recibo"),
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso restrito a ADMINS",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recibo não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados do recibo inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> getByRecibo(@PathVariable String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Realizar check-out", description = "Realiza o check-out de uma vaga pelo seu código",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Check-out realizado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URI do recibo"),
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso restrito a ADMINS",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recibo não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados do recibo inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PutMapping("/check-out/{recibo}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkOut(@PathVariable String recibo) {
        ClienteVaga clienteVaga = estacionamentoService.checkOut(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar estacionamentos por CPF", description = "Busca todos os estacionamentos de um cliente pelo seu CPF",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estacionamentos encontrados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso restrito a ADMINS",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageableDto> getAllEstacionamentoByCpf(@PathVariable String cpf,
                                                       @PageableDefault(size = 5, sort = "dataEntrada",
                                                               direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorClienteCpf(cpf, pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar estacionamentos por ID", description = "Busca todos os estacionamentos de um cliente pelo seu ID",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estacionamentos encontrados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso restrito a CLIENTE",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    public ResponseEntity<PageableDto> getAllEstacionamentoPorCliente(@AuthenticationPrincipal JwtUserDetails user,
                                                                      @Parameter(hidden = true) @PageableDefault(size = 5, sort = "dataEntrada",
                                                                              direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorUsuarioId(user.getId(), pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }


}
