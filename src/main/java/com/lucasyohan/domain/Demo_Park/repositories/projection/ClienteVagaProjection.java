package com.lucasyohan.domain.Demo_Park.repositories.projection;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ClienteVagaProjection {

    String getPlaca();
    String getMarca();
    String getModelo();
    String getCor();
    String getClienteCpf();
    String getRecibo();
    String getVagaCodigo();
    String getValor();
    String getDesconto();
    String getDataEntrada();
    String getDataSaida();
}
