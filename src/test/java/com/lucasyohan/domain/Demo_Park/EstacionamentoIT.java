package com.lucasyohan.domain.Demo_Park;

import com.lucasyohan.domain.Demo_Park.web.dto.EstacionamentoCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.PageableDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/estacionamentos/estacionamento-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamento-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EstacionamentoIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void criarCheckin_ComDadosValidos_RetornarStatus201() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234").marca("Volkswagen").modelo("Fusca")
                .cor("Azul").clienteCpf("09191773016")
                .build();

        webTestClient.post().uri("api/v2/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("placa").isEqualTo("ABC-1234")
                .jsonPath("marca").isEqualTo("Volkswagen")
                .jsonPath("modelo").isEqualTo("Fusca")
                .jsonPath("cor").isEqualTo("Azul")
                .jsonPath("clienteCpf").isEqualTo("09191773016")
                .jsonPath("recibo").exists()
                .jsonPath("dataEntrada").exists()
                .jsonPath("vagaCodigo").exists();
    }

    @Test
    public void criarCheckin_ComRoleCliente_RetornarErrorMessage403() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234").marca("Volkswagen").modelo("Fusca")
                .cor("Azul").clienteCpf("98401203015")
                .build();

        webTestClient.post().uri("/api/v2/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bob@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void criarCheckin_ComDadosInvalidos_RetornarErrorMessage422() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABCD-0011").marca("").modelo("")
                .cor("").clienteCpf("9840120301")
                .build();

        webTestClient.post().uri("/api/v2/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bob@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");

        createDto = EstacionamentoCreateDto.builder()
                .placa("").marca("").modelo("")
                .cor("").clienteCpf("")
                .build();

        webTestClient.post().uri("/api/v2/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bob@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void criarCheckin_ComCpfInexistente_RetornarErrorMessage404() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234").marca("Volkswagen").modelo("Fusca")
                .cor("Azul").clienteCpf("32863933043")
                .build();

        webTestClient.post().uri("/api/v2/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Sql(scripts = "/sql/estacionamentos/estacionamento-ocupado-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos/estacionamento-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void criarCheckin_ComVagasOcupadas_RetornarErrorMessage404() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("ABC-1234").marca("Volkswagen").modelo("Fusca")
                .cor("Azul").clienteCpf("09191773016")
                .build();

        webTestClient.post().uri("/api/v2/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void buscarCheckin_ComPerfilAdmin_RetornarStatus200() {
        webTestClient
                .get()
                .uri("/api/v2/estacionamentos/check-in/{recibo}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("recibo").exists()
                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("vagaCodigo").isEqualTo("A-01");
    }

    @Test
    public void buscarCheckin_ComReciboInvalido_RetornarErrorMessage404() {
        webTestClient
                .get()
                .uri("/api/v2/estacionamentos/check-in/{recibo}", "20230313-101999")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos/check-in/20260313-101999")
                .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void criarCheckout_ConReciboExistente_RetornarStatus200() {
        //'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00'
        webTestClient
                .put()
                .uri("/api/v2/estacionamentos/check-out/{recibo}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("recibo").exists()
                .jsonPath("dataEntrada").isEqualTo("2026-03-13 10:15:00")
                .jsonPath("dataSaida").exists()
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("valor").exists()
                .jsonPath("desconto").exists()
                .jsonPath("vagaCodigo").isEqualTo("A-01");


    }

    @Test
    public void criarCheckout_ConReciboInexistente_RetornarErrorMessage404() {
        //'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00'
        webTestClient
                .put()
                .uri("/api/v2/estacionamentos/check-out/{recibo}", "20260313-101999")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos/check-out/20260313-101999")
                .jsonPath("method").isEqualTo("PUT");


    }

    @Test
    public void criarCheckout_ComRoleCliente_RetornarErrorMessage403() {
        //'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00'
        webTestClient
                .put()
                .uri("/api/v2/estacionamentos/check-out/{recibo}", "20260313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos/check-out/20260313-101300")
                .jsonPath("method").isEqualTo("PUT");


    }

    @Test
    public void buscarEstacionamentos_PorClienteCpf_RetornarStatus200() {
        PageableDto response = webTestClient
                .get()
                .uri("/api/v2/estacionamentos/cpf/{cpf}?size=1&page=0", "98401203015")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getContent()).size().isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(response.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(response.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(response.getSize()).isEqualTo(1);

        response = webTestClient
                .get()
                .uri("/api/v2/estacionamentos/cpf/{cpf}?size=1&page=1", "98401203015")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getContent()).size().isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(response.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(response.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(response.getSize()).isEqualTo(1);

    }

    @Test
    public void buscarEstacionamentos_PorClienteCpfComRoleCliente_RetornarErrorMessage403() {
        //'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00'
        webTestClient
                .get()
                .uri("/api/v2/estacionamentos/cpf/{cpf}?size=1&page=0", "98401203015")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos/cpf/98401203015")
                .jsonPath("method").isEqualTo("GET");

    }

    @Test
    public void buscarEstacionamentos_PorClienteIdLogado_RetornarStatus200() {
        PageableDto response = webTestClient
                .get()
                .uri("/api/v2/estacionamentos?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bob@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getContent()).size().isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(response.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(response.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(response.getSize()).isEqualTo(1);
    }

    @Test
    public void buscarEstacionamentos_PorClienteIdLogadoNaoAutorizado_RetornarErroMessage403() {
        webTestClient
                .get()
                .uri("/api/v2/estacionamentos?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("path").isEqualTo("/api/v2/estacionamentos")
                .jsonPath("method").isEqualTo("GET");
    }

}

