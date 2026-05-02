package com.lucasyohan.domain.Demo_Park.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UsuarioResponseDto {

    private String id;
    private String username;
    private String role;
}
