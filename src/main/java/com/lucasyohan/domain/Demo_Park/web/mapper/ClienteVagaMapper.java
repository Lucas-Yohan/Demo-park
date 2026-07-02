package com.lucasyohan.domain.Demo_Park.web.mapper;

import com.lucasyohan.domain.Demo_Park.entities.ClienteVaga;
import com.lucasyohan.domain.Demo_Park.web.dto.EstacionamentoCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.EstacionamentoResponseDto;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toCliente(EstacionamentoCreateDto dto){
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDto toDto(ClienteVaga vaga){
        return new ModelMapper().map(vaga, EstacionamentoResponseDto.class);
    }
}
