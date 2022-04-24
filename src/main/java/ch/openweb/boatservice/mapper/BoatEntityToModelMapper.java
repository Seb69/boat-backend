package ch.openweb.boatservice.mapper;

import ch.openweb.boatservice.entity.BoatEntity;
import ch.openweb.boatservice.model.BoatModel;
import org.springframework.stereotype.Component;

@Component
public class BoatEntityToModelMapper {

  public BoatModel convert(BoatEntity boatEntity) {
    return BoatModel.builder()
      .id(boatEntity.getId())
      .name(boatEntity.getName())
      .description(boatEntity.getDescription())
      .build();
  }

  public BoatEntity convert(BoatModel boatEntity) {
    return BoatEntity.builder()
      .id(boatEntity.getId())
      .name(boatEntity.getName())
      .description(boatEntity.getDescription())
      .build();
  }

}
