package com.lucasyohan.domain.Demo_Park.web.mapper;

import com.lucasyohan.domain.Demo_Park.entities.Vaga;
import com.lucasyohan.domain.Demo_Park.web.dto.VagaCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.VagaResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga(VagaCreateDto dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

        public static VagaResponseDto toDto(Vaga vaga) {
            return new ModelMapper().map(vaga, VagaResponseDto.class);
        }

}
