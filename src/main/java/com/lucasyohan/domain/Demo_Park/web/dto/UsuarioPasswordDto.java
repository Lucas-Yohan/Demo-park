package com.lucasyohan.domain.Demo_Park.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UsuarioPasswordDto {
    @Size(min = 6, max = 6)
    @NotBlank
    private String actualPassword;
    @Size(min = 6, max = 6)
    @NotBlank
    private String newPassword;
    @Size(min = 6, max = 6)
    @NotBlank
    private String confPassword;
}
