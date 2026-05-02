package com.lucasyohan.domain.Demo_Park.web.mapper;

import com.lucasyohan.domain.Demo_Park.entities.Usuarios;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class UsuarioMapper {

    public static Usuarios toUsuario(UsuarioCreateDto createDto){
        return new ModelMapper().map(createDto, Usuarios.class);
    }

    public static UsuarioResponseDto toDto(Usuarios usuario){
        String role = usuario.getRole().name().substring("ROLE_".length());
        PropertyMap<Usuarios, UsuarioResponseDto> props = new PropertyMap<Usuarios, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);

        return new ModelMapper().map(usuario, UsuarioResponseDto.class);
    }
}
