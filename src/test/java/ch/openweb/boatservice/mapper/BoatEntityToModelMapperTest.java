package ch.openweb.boatservice.mapper;

import ch.openweb.boatservice.entity.BoatEntity;
import ch.openweb.boatservice.mock.BoatEntityMock;
import ch.openweb.boatservice.mock.BoatModelMock;
import ch.openweb.boatservice.model.BoatModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class BoatEntityToModelMapperTest {

  @InjectMocks
  private BoatEntityToModelMapper boatEntityToModelMapper;

  @Test
  @DisplayName("Convert boat entity to boat model")
  void convertToModel() {

    BoatEntity boatEntity = BoatEntityMock.createA();
    BoatModel convert = boatEntityToModelMapper.convert(boatEntity);
    Assertions.assertAll(
      () -> Assertions.assertEquals(boatEntity.getId(), convert.getId()),
      () -> Assertions.assertEquals(boatEntity.getName(), convert.getName()),
      () -> Assertions.assertEquals(boatEntity.getDescription(), convert.getDescription())
    );
  }

  @Test
  @DisplayName("Convert boat model to boat entity")
  void convertToEntity() {

    BoatModel boatModel = BoatModelMock.createA();
    BoatEntity convert = boatEntityToModelMapper.convert(boatModel);
    Assertions.assertAll(
      () -> Assertions.assertEquals(boatModel.getId(), convert.getId()),
      () -> Assertions.assertEquals(boatModel.getName(), convert.getName()),
      () -> Assertions.assertEquals(boatModel.getDescription(), convert.getDescription())
    );
  }
}
