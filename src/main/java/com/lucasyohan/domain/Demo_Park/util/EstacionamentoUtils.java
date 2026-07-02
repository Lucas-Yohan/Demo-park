package com.lucasyohan.domain.Demo_Park.util;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    // 2026-07-01T19:22:01.45000000000

    public static String gerarRecibo(){
        LocalDateTime dataEntrada = LocalDateTime.now();
        String recibo = dataEntrada.toString().substring(0,19);
        return recibo
                .replace("-", "")
                .replace(":", "")
                .replace("T", "-");
    }

}
