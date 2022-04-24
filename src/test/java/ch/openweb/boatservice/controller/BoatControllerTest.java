package ch.openweb.boatservice.controller;

import ch.openweb.boatservice.config.SecurityConfiguration;
import ch.openweb.boatservice.exception.BoatNotFoundException;
import ch.openweb.boatservice.mock.BoatModelMock;
import ch.openweb.boatservice.model.BoatModel;
import ch.openweb.boatservice.service.BoatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@WebFluxTest(controllers = BoatController.class)
@Import({SecurityConfiguration.class})
class BoatControllerTest {

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private BoatService boatService;

  @Test
  @WithMockUser
  @DisplayName("GET /v1/boats with negative size, validation should fails")
  void getBoatsWithWrongSize() {

    webClient.mutateWith(csrf())
      .get()
      .uri(uriBuilder -> uriBuilder.path("/v1/boats").queryParam("size", -2).build())
      .exchange()
      .expectStatus().isBadRequest();
  }

  @Test
  @WithMockUser
  @DisplayName("GET /v1/boats with negative page, validation should fails")
  void getBoatsWithWrongPage() {

    webClient.mutateWith(csrf())
      .get()
      .uri(uriBuilder -> uriBuilder.path("/v1/boats").queryParam("page", -2).build())
      .exchange()
      .expectStatus().isBadRequest();
  }

  @Test
  @WithMockUser
  @DisplayName("GET /v1/boats, should return success")
  void getBoatsSuccess() {

    int PAGE = 0;
    int SIZE = 10;

    Mockito.when(boatService.getBoats(ArgumentMatchers.any(PageRequest.class)))
      .thenReturn(Mono.just(new PageImpl<>(BoatModelMock.list(), PageRequest.of(PAGE, SIZE), BoatModelMock.list().size())));

    webClient.mutateWith(csrf())
      .get()
      .uri(uriBuilder -> uriBuilder.path("/v1/boats").queryParam("page", PAGE).queryParam("size", SIZE).build())
      .exchange()
      .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.size").isEqualTo(SIZE)
      .jsonPath("$.number").isEqualTo(PAGE)
      .jsonPath("$.numberOfElements").isEqualTo(BoatModelMock.list().size())
      .jsonPath("$.content[0].id").isEqualTo(BoatModelMock.createA().getId())
      .jsonPath("$.content[0].name").isEqualTo(BoatModelMock.createA().getName())
      .jsonPath("$.content[0].description").isEqualTo(BoatModelMock.createA().getDescription())
      .jsonPath("$.content[1].id").isEqualTo(BoatModelMock.createB().getId())
      .jsonPath("$.content[1].name").isEqualTo(BoatModelMock.createB().getName())
      .jsonPath("$.content[1].description").isEqualTo(BoatModelMock.createB().getDescription());

  }

  @Test
  @WithMockUser
  @DisplayName("POST /v1/boats, should validation fails")
  void postBoatsFailed() {

    BoatModel invalidBoatModel = BoatModel.builder()
      .name("") // name is invalid, should not be blank
      .description("Description is not blank")
      .build();
    Mockito.when(boatService.save(ArgumentMatchers.any(BoatModel.class)))
      .thenReturn(Mono.just(BoatModelMock.createA()));

    webClient.mutateWith(csrf())
      .post()
      .uri("/v1/boats")
      .bodyValue(invalidBoatModel)
      .exchange()
      .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
      .expectStatus().isBadRequest();

  }

  @Test
  @WithMockUser
  @DisplayName("POST /v1/boats, should return success")
  void postBoatsSuccess() {

    BoatModel boatModel = BoatModel.builder()
      .name("Boat TEST")
      .description("Description of boat")
      .build();
    Mockito.when(boatService.save(ArgumentMatchers.any(BoatModel.class)))
      .thenReturn(Mono.just(BoatModelMock.createA()));

    webClient.mutateWith(csrf())
      .post()
      .uri("/v1/boats")
      .bodyValue(boatModel)
      .exchange()
      .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
      .expectStatus().isCreated()
      .expectBody()
      .jsonPath("$.id").isEqualTo(BoatModelMock.createA().getId())
      .jsonPath("$.name").isEqualTo(BoatModelMock.createA().getName())
      .jsonPath("$.description").isEqualTo(BoatModelMock.createA().getDescription());

  }

  @Test
  @WithMockUser
  @DisplayName("PUT /v1/boats, should return success")
  void putBoatsSuccess() {

    BoatModel boatModel = BoatModel.builder()
      .id(1L)
      .name("Boat TEST")
      .description("Description of boat")
      .build();
    Mockito.when(boatService.update(ArgumentMatchers.any(BoatModel.class)))
      .thenReturn(Mono.just(BoatModelMock.createA()));

    webClient.mutateWith(csrf())
      .put()
      .uri("/v1/boats/1")
      .bodyValue(boatModel)
      .exchange()
      .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.id").isEqualTo(BoatModelMock.createA().getId())
      .jsonPath("$.name").isEqualTo(BoatModelMock.createA().getName())
      .jsonPath("$.description").isEqualTo(BoatModelMock.createA().getDescription());

  }

  @Test
  @WithMockUser
  @DisplayName("PUT /v1/boats with id not existing, should return failed")
  void putBoatsFailed() {

    BoatModel boatModel = BoatModel.builder()
      .id(1L)
      .name("Boat TEST")
      .description("Description of boat")
      .build();
    Mockito.when(boatService.update(ArgumentMatchers.any(BoatModel.class)))
      .thenReturn(Mono.error(new BoatNotFoundException("id of boat not exists")));

    webClient.mutateWith(csrf())
      .put()
      .uri("/v1/boats/999")
      .bodyValue(boatModel)
      .exchange()
      .expectStatus().isNotFound();

  }

  @Test
  @WithMockUser
  @DisplayName("DELETE /v1/boats/id with id not existing, should return failed")
  void deleteBoatsFailed() {

    Mockito.when(boatService.delete(ArgumentMatchers.anyLong()))
      .thenReturn(Mono.error(new BoatNotFoundException("id of boat not exists")));

    webClient.mutateWith(csrf())
      .delete()
      .uri("/v1/boats/999")
      .exchange()
      .expectStatus().isNotFound();

  }

  @Test
  @WithMockUser
  @DisplayName("DELETE /v1/boats should return success")
  void deleteBoatsSuccess() {

    Mockito.when(boatService.delete(ArgumentMatchers.anyLong()))
      .thenReturn(Mono.empty().then());

    webClient.mutateWith(csrf())
      .delete()
      .uri("/v1/boats/1")
      .exchange()
      .expectStatus().isNoContent();

  }

  @Test
  @DisplayName("GET /v1/boats, should return 401")
  void getAuthFailed() {

    webClient.mutateWith(csrf())
      .get()
      .uri("/v1/boats")
      .exchange()
      .expectStatus().isUnauthorized();

  }
}
