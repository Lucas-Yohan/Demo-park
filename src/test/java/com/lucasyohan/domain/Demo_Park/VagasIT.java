package com.lucasyohan.domain.Demo_Park;

import com.lucasyohan.domain.Demo_Park.web.dto.VagaCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/vagas-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vagas-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagasIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void criarVaga_ComDadosValidos_RetornarLocationStatus201() {
        webTestClient
                .post()
                .uri("api/v2/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new VagaCreateDto("Y-05", "LIVRE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void criarVaga_ComCodigoExistente_RetornarErrorMessage409() {
        webTestClient
                .post()
                .uri("api/v2/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new VagaCreateDto("Y-01", "OCUPADA"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v2/vagas");
    }

    @Test
    public void criarVaga_ComDadosInvalidos_RetornarErrorMessage422() {
        webTestClient
                .post()
                .uri("api/v2/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new VagaCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v2/vagas");

        webTestClient
                .post()
                .uri("api/v2/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .bodyValue(new VagaCreateDto("Y-099", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v2/vagas");
    }

    @Test
    public void buscarVaga_ComCodigoExistente_RetornarStatus200() {
        webTestClient
                .get()
                .uri("/api/v2/vagas/{codigo}", "Y-01")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo("10")
                .jsonPath("codigo").isEqualTo("Y-01")
                .jsonPath("status").isEqualTo("LIVRE");
    }

    @Test
    public void buscarVaga_ComCodigoInexistente_RetornarStatus404() {
        webTestClient
                .get()
                .uri("/api/v2/vagas/{codigo}", "Y-99")
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v2/vagas/Y-99");
    }
}
