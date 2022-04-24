package ch.openweb.boatservice.repository;

import ch.openweb.boatservice.config.SecurityConfiguration;
import ch.openweb.boatservice.entity.BoatEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import reactor.test.StepVerifier;

import java.util.Objects;

@Slf4j
@SpringBootTest
@Import({SecurityConfiguration.class})
class BoatRepositoryIntegrationTest {
  @Autowired
  private BoatRepository boatRepository;

  @Test
  @DisplayName("should find all boat paginated")
  void findAllOrderByName() {

    StepVerifier.create(boatRepository.findAllBy(PageRequest.of(0, 10)))
      .expectNextMatches(boatEntity -> "Zodiac Medline".equals(boatEntity.getName()) && 1 == boatEntity.getId())
      .expectNextMatches(boatEntity -> "Zodiac eJET".equals(boatEntity.getName()) && 2 == boatEntity.getId())
      .expectNextCount(8)
      .verifyComplete();
  }

  @Test
  @DisplayName("should save boatEntity ")
  void findAllBy() {

    BoatEntity boatEntityToSave = BoatEntity.builder()
      .name("Mercury A540")
      .description("New boat made by mercury")
      .build();
    StepVerifier.create(boatRepository.save(boatEntityToSave))
      .expectNextMatches(boatEntitySaved -> Objects.nonNull(boatEntitySaved.getId()) && "Mercury A540".equals(boatEntitySaved.getName()))
      .verifyComplete();
  }
}
