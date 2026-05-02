package com.lucasyohan.domain.Demo_Park.web.controllers;

import com.lucasyohan.domain.Demo_Park.entities.Usuarios;
import com.lucasyohan.domain.Demo_Park.services.UsuarioService;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioCreateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v3/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public ResponseEntity<UsuarioCreateDto> create(@RequestBody @Valid UsuarioCreateDto usuarioDto){
        Usuarios usuario =
    }

}
