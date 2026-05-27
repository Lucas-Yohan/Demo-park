package com.lucasyohan.domain.Demo_Park.web.controllers;

import com.lucasyohan.domain.Demo_Park.entities.Usuarios;
import com.lucasyohan.domain.Demo_Park.services.UsuarioService;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioPasswordDto;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioResponseDto;
import com.lucasyohan.domain.Demo_Park.web.exception.ErrorMessage;
import com.lucasyohan.domain.Demo_Park.web.mapper.UsuarioMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("Health check ok");
    }

    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário com as informações fornecidas no corpo da requisição",
        responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Username já cadastrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
        }
    )
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody @Valid UsuarioCreateDto usuarioDto){
        Usuarios usuario = usuarioService.salvar(UsuarioMapper.toUsuario(usuarioDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(usuario));
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna as informações de um usuário com base no ID fornecido",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(UsuarioMapper.toDto(usuarioService.buscarPorId(id)));
    }

    @Operation(summary = "Atualizar senha do usuário", description = "Atualiza a senha de um usuário com base no ID fornecido",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UsuarioPasswordDto passwordDto, @PathVariable Long id){
        usuarioService.updatePassword(passwordDto.getNewPassword(), passwordDto.getActualPassword(), passwordDto.getConfPassword(), id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar todos os usuários", description = "Retorna a lista de todos os usuários",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários encontrados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class)))
            }
    )
    @GetMapping()
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){
        List<Usuarios> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(users.stream().map(UsuarioMapper::toDto).toList());
    }



}
