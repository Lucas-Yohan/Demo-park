package com.lucasyohan.domain.Demo_Park.web.controllers;

import com.lucasyohan.domain.Demo_Park.entities.Usuarios;
import com.lucasyohan.domain.Demo_Park.services.UsuarioService;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioPasswordDto;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioResponseDto;
import com.lucasyohan.domain.Demo_Park.web.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("Health check ok");
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody @Valid UsuarioCreateDto usuarioDto){
        Usuarios usuario = usuarioService.salvar(UsuarioMapper.toUsuario(usuarioDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(UsuarioMapper.toDto(usuarioService.buscarPorId(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UsuarioPasswordDto passwordDto, @PathVariable Long id){
        usuarioService.updatePassword(passwordDto.getNewPassword(), passwordDto.getActualPassword(), passwordDto.getConfPassword(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){
        List<Usuarios> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(users.stream().map(UsuarioMapper::toDto).toList());
    }



}
