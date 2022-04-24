package ch.openweb.boatservice.service;

import ch.openweb.boatservice.entity.BoatEntity;
import ch.openweb.boatservice.exception.BoatNotFoundException;
import ch.openweb.boatservice.mapper.BoatEntityToModelMapper;
import ch.openweb.boatservice.mock.BoatEntityMock;
import ch.openweb.boatservice.mock.BoatModelMock;
import ch.openweb.boatservice.model.BoatModel;
import ch.openweb.boatservice.repository.BoatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

@ExtendWith(SpringExtension.class)
class BoatServiceImplTest {

  @InjectMocks
  private BoatServiceImpl boatService;

  @Mock
  private BoatRepository boatRepository;

  @Mock
  private R2dbcEntityTemplate r2dbcEntityTemplate;

  @Mock
  private BoatEntityToModelMapper boatEntityToModelMapper;

  @Test
  @DisplayName("getBoats with pagination, should success")
  void getBoats() {

    int SIZE = 10;
    int PAGE = 0;

    Mockito.when(boatRepository.findAllBy(ArgumentMatchers.any(Pageable.class)))
      .thenReturn(Flux.fromIterable(BoatEntityMock.list()));
    Mockito.when(boatRepository.count())
      .thenReturn(Mono.just(2L));
    Mockito.when(boatEntityToModelMapper.convert(ArgumentMatchers.any(BoatEntity.class)))
      .thenReturn(BoatModelMock.createA())
      .thenReturn(BoatModelMock.createB());

    StepVerifier.create(boatService.getBoats(PageRequest.of(0, SIZE)))
      .expectNextMatches(boatModels -> boatModels.getTotalElements() == BoatEntityMock.list().size()
        && boatModels.getSize() == SIZE
        && boatModels.getNumber() == PAGE
        && boatModels.stream().count() == 2
        && boatModels.stream().allMatch(boatModel -> Objects.nonNull(boatModel.getId())))
      .verifyComplete();
  }

  @Test
  @DisplayName("save boat, should return saved entity")
  void save() {
    Mockito.when(boatEntityToModelMapper.convert(ArgumentMatchers.any(BoatModel.class)))
      .thenReturn(BoatEntityMock.createA());
    Mockito.when(boatRepository.save(ArgumentMatchers.any(BoatEntity.class)))
      .thenReturn(Mono.just(BoatEntityMock.createA()));
    Mockito.when(boatEntityToModelMapper.convert(ArgumentMatchers.any(BoatEntity.class)))
      .thenReturn(BoatModelMock.createA());

    StepVerifier.create(boatService.save(BoatModelMock.createA()))
      .expectNextMatches(boatModel -> BoatModelMock.createA().getId().equals(boatModel.getId()) &&
        BoatModelMock.createA().getName().equals(boatModel.getName()) &&
        BoatModelMock.createA().getDescription().equals(boatModel.getDescription()))
      .verifyComplete();
  }

  @Test
  @DisplayName("update with ID not exists, should return success")
  void update() {
    Mockito.when(boatRepository.existsById(ArgumentMatchers.anyLong()))
      .thenReturn(Mono.just(Boolean.TRUE));
    Mockito.when(boatEntityToModelMapper.convert(ArgumentMatchers.any(BoatModel.class)))
      .thenReturn(BoatEntityMock.createA());
    Mockito.when(r2dbcEntityTemplate.update(ArgumentMatchers.any(BoatEntity.class)))
      .thenReturn(Mono.just(BoatEntityMock.createA()));
    Mockito.when(boatEntityToModelMapper.convert(ArgumentMatchers.any(BoatEntity.class)))
      .thenReturn(BoatModelMock.createA());

    StepVerifier.create(boatService.update(BoatModelMock.createA()))
      .expectNextMatches(boatModel -> BoatModelMock.createA().getId().equals(boatModel.getId()) &&
        BoatModelMock.createA().getName().equals(boatModel.getName()) &&
        BoatModelMock.createA().getDescription().equals(boatModel.getDescription()))
      .verifyComplete();
  }

  @Test
  @DisplayName("update with ID not exists, should throw error")
  void updateIdNotExits() {
    Mockito.when(boatRepository.existsById(ArgumentMatchers.anyLong()))
      .thenReturn(Mono.just(Boolean.FALSE));

    StepVerifier.create(boatService.update(BoatModelMock.createA()))
      .expectError(BoatNotFoundException.class);
  }

  @Test
  @DisplayName("delete, should return success")
  void delete() {
    Mockito.when(boatRepository.existsById(ArgumentMatchers.anyLong()))
      .thenReturn(Mono.just(Boolean.TRUE));
    Mockito.when(boatRepository.deleteById(ArgumentMatchers.anyLong()))
      .thenReturn(Mono.empty().then());

    StepVerifier.create(boatService.delete(1L))
      .verifyComplete();
  }

  @Test
  @DisplayName("delete with ID not exists, should throw error")
  void deleteIdNotExits() {
    Mockito.when(boatRepository.existsById(ArgumentMatchers.anyLong()))
      .thenReturn(Mono.just(Boolean.FALSE));

    StepVerifier.create(boatService.delete(9999L))
      .expectError(BoatNotFoundException.class);
  }

}
