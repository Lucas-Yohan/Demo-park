package com.lucasyohan.domain.Demo_Park;

import com.lucasyohan.domain.Demo_Park.web.dto.ClienteCreateDto;
import com.lucasyohan.domain.Demo_Park.web.dto.ClienteResponseDto;
import com.lucasyohan.domain.Demo_Park.web.dto.PageableDto;
import com.lucasyohan.domain.Demo_Park.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void createCliente_ComCPFValido_ReturnCodeStatus201(){
        ClienteResponseDto responseBody = testClient
                .post()
                .uri("/api/v2/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"carla@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("Carla Ferreira", "42013694040"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Carla Ferreira");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("42013694040");
    }

    @Test
    public void createCliente_ComCPFJaCadastrado_ReturnErrorMessage409(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v2/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"carla@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("Carla Ferreira", "70853634017"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void createCliente_ComCPFInvalido_ReturnErrorMessage422(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v2/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"carla@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("Carla Ferreira", "99992345875"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        testClient
                .post()
                .uri("/api/v2/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"carla@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("Carla Ferreira", "987374653433"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        testClient
                .post()
                .uri("/api/v2/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"carla@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createCliente_ComUsuarioNaoPermitido_ReturnErrorMessage403(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v2/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .bodyValue(new ClienteCreateDto("Carla Ferreira", "49334360020"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void buscarCliente_ComClienteIdValido_ReturnStatus200(){
        ClienteResponseDto responseBody = testClient
                .get()
                .uri("/api/v2/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"bia@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo("10");
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Bianca Silva");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("70853634017");
    }

    @Test
    public void buscarCliente_ComUsuarioNaoPermitido_ReturnErrorMessage403(){
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v2/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"bob@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void buscarCliente_ClienteIdInexistente_ReturnErrorMessage404(){
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v2/clientes/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void buscarTodosClientes_ComPaginacaoPorAdmin_ReturnStatus200(){
        PageableDto responseBody = testClient
                .get()
                .uri("/api/v2/clientes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent()).size().isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);

        testClient
                .get()
                .uri("/api/v2/clientes?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent()).size().isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void buscarTodosClientes_ComRoleCliente_ReturnErrorMessage403(){
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v2/clientes?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"bia@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void buscarDetalhesCliente_ComoCliente_ReturnStatus200(){
        ClienteResponseDto responseBody = testClient
                .get()
                .uri("/api/v2/clientes/detalhes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"bia@email.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("70853634017");
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Bianca Silva  ");
    }

    @Test
    public void buscarDetalhesCliente_ComoAdmin_ReturnErrorMessage403(){
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v2/clientes/detalhes")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient,"ana@email.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }


}
