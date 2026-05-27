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
        String role = usuario.getRole().name().replaceFirst("^ROLE_", "");
        PropertyMap<Usuarios, UsuarioResponseDto> props = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);

        return mapper.map(usuario, UsuarioResponseDto.class);
    }
}
